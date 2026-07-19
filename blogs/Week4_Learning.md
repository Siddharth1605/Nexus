H3Cell of a restaurant is stored in order table. The cell is computed only once when order arrives and never need to recompute it. 
This is deliberate denormalization - storing computed data to avoid recomputation

Gpt : 
Week 4 is PostgreSQL + Flyway migrations. Here is exactly what you are building and why each piece exists.

---

## The Big Picture of Week 4

In Weeks 1–3 you built the live layer — H3 conversion, Redis state, Kafka pipeline. All of that is fast, in-memory, temporary. Redis keys expire. Kafka messages are consumed and gone.

Week 4 builds the permanent layer. PostgreSQL is where data survives a restart. If Redis dies and you restart it, how does it know which zones exist? It loads them from PostgreSQL. If you want to know the history of a rider's assignments, that is in PostgreSQL. If your entire system restarts from scratch, PostgreSQL is the source of truth that rebuilds everything else.

This week has two parts — Flyway and the data seeder. They solve completely different problems.

---

## Part 1 — Why Flyway Before Writing Any JPA Code

You have PostgreSQL running in Docker. Right now it is an empty database with no tables. You could create tables manually by connecting to psql and typing CREATE TABLE. But there is a problem with that — next time someone clones your repo and runs docker-compose up, they get an empty database again. They would have to manually recreate the tables. On GCP deployment, same problem. In interviews when you demo, same problem.

Flyway solves this. It is a database migration tool. You write SQL files with your table definitions, Flyway runs them automatically on startup, and your database is always in the correct state regardless of where or when you start the application.

The naming convention is strict — `V1__init.sql`, `V2__add_index.sql`. The V + number tells Flyway the order. The double underscore separates version from description. Flyway tracks which migrations it has already run in a table called `flyway_schema_history`, so it never runs the same migration twice.

Your `V1__init.sql` creates three tables — riders, orders, assignments. These are the three permanent records your system needs.

---

## The Three Tables and Why Each Exists

**riders table**

This stores the permanent profile of every rider in the system. Name, phone number, any metadata. This is written once when a rider registers and almost never changes. It is the source of truth for who your riders are.

This is separate from Redis because Redis only knows the rider's current live state — where they are right now and whether they are idle. PostgreSQL knows who they are permanently. If you wanted to send a rider a notification, you look up their phone number from PostgreSQL. Redis does not store that.

**orders table**

This stores every order that came into the system — restaurant location, status (PENDING → ASSIGNED → DELIVERED → FAILED), when it was created. This is the permanent record of demand. When you want to answer "how many orders did we process last Tuesday", you query this table.

It also stores the restaurant's H3 cell — computed once when the order arrives and stored so you never need to recompute it. This is a deliberate denormalization — storing computed data to avoid recomputation.

**assignments table**

This is the most important table for your project story. Every time a rider is assigned to an order — including rejected assignments and retry attempts — a row is inserted here. Each row records which rider, which order, which attempt number, when it happened, and the outcome (ASSIGNED, ACCEPTED, REJECTED, COMPLETED).

This table is your audit trail. It answers: how many attempts did order 42 need before finding a rider? Which rider rejected the most orders? What is the average time between order creation and successful assignment?

These are the numbers that become your performance story in interviews.

---

## Part 2 — The DataSeeder and Why It Matters

The DataSeeder is a Spring Boot component that runs automatically on startup. It inserts fake riders into PostgreSQL and pushes their initial location into Redis. This is not production code — it is your development scaffolding.

Here is why you need it.

Right now, if you start your application and want to test the matching engine, you have to manually POST 50 location updates before any idle-riders SET in Redis has any data. That is tedious and you would have to do it every single time Redis restarts (which happens every time Docker restarts).

The DataSeeder automates this. On startup it checks whether test riders already exist in PostgreSQL. If not, it inserts them and pushes their locations to Redis. Now every time you start the system, you immediately have 50 riders spread across Bangalore's H3 cells, ready for matching. You can test an order assignment within seconds of startup without any manual setup.

What the DataSeeder specifically does for each fake rider — inserts a row in the riders table, generates a random GPS coordinate within Bangalore's bounding box, converts it to an H3 cell, sets `rider:{id}:h3` in Redis, sets `rider:{id}:status` to IDLE in Redis, and adds the rider ID to `idle-riders:{cellId}` in Redis.

After DataSeeder runs, your Redis state looks exactly like it would in production with 50 real riders sending GPS updates. Your Week 5 matching engine can be tested immediately against this realistic state.

---
