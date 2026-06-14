1. Why and how redis operations are o(n)
2. How to define riders id initially, crud operations for riders id
3. What to do if a non-existing rider id is claimed

Todo: 
20-06-2026
1. Understand springboot upto now - make a excalidraw / blog
2. Debug points, understand why rider is not able to be claimed - Whether it is an issue in kafka-consumer or redis or spring logic
3. Create blogs for redis, kafka, project, the issue facing now, along with springboot in medium

Week3 References(WIll delete later)
You actually covered quite a lot today. If I were writing a learning journal entry, it would look something like this:

# Week 3 – Kafka + Spring Boot Progress

## 1. Set up Kafka 4.2 in KRaft Mode

Learned that modern Kafka no longer requires ZooKeeper.

Completed:

```text
Kafka Installation
↓
Generate Cluster ID
↓
Format Kafka Storage
↓
Start Kafka Broker
↓
Verify Broker Running on 9092
```

Key takeaway:

> Kafka 4.x uses KRaft mode where the broker manages metadata itself.

---

## 2. Learned Kafka Core Concepts

Understood:

```text
Producer
↓
Topic
↓
Partition
↓
Consumer
↓
Consumer Group
```

Also understood why partitions exist:

```text
Parallel Processing
```

and why riderId should eventually be used as a Kafka key:

```text
Same Rider
↓
Same Partition
↓
Ordered Events
```

---

## 3. Created Kafka Topic

Learned that topics can be:

```text
Created Manually
Created Automatically
Created Through Spring Configuration
```

For the project, using manually created topics is sufficient.

Current topic:

```text
location-updates
```

(or your chosen rider-location topic)

---

## 4. Built Kafka Producer

Created:

```text
LocationProducer
```

Flow:

```text
LocationController
↓
LocationProducer
↓
Kafka Topic
```

Successfully published rider location messages to Kafka.

This was the first successful end-to-end Kafka integration.

---

## 5. Introduced DTO-Based Messaging

Created:

```java
RiderCoordinatesDTO
```

Containing:

```text
riderId
latitude
longitude
```

Learned:

> DTOs are used to move structured data between layers instead of passing multiple primitive values everywhere.

---

## 6. Learned Kafka Serialization

Initially encountered:

```text
ClassCastException
RiderCoordinatesDTO cannot be cast to String
```

Root cause:

```text
Producer configured with StringSerializer
but sending DTO objects
```

Solution:

```text
JsonSerializer
JsonDeserializer
```

Important concept learned:

```text
Java Object
↓
JSON
↓
Kafka
↓
JSON
↓
Java Object
```

This process is called:

```text
Serialization / Deserialization
```

---

## 7. Learned Why Jackson Is Required

Encountered:

```text
NoClassDefFoundError
com.fasterxml.jackson.databind.JavaType
```

Learned:

```text
JsonSerializer
↓
Uses Jackson
↓
Converts DTO ↔ JSON
```

Without Jackson, Kafka cannot deserialize DTO objects.

---

## 8. Understood Spring Dependency Injection Better

Worked through:

```text
@Service
@Component
@RestController
```

Learned:

```text
Spring Container
↓
Creates Objects
↓
Stores Beans
↓
Injects Dependencies
```

Understood why constructor injection is preferred over field injection.

Example:

```text
LocationConsumer
↓
PositionProcessor
↓
RiderStateStore
```

Spring automatically wires them together.

---

## 9. Designed Kafka Processing Flow

Current architecture:

```text
POST /location
        ↓
LocationController
        ↓
LocationProducer
        ↓
Kafka Topic
        ↓
LocationConsumer
        ↓
PositionProcessor
        ↓
RiderStateStore
        ↓
Redis
```

This aligns closely with your Nexus architecture.

---

## 10. Realization About Spring

Probably the biggest lesson today:

Before:

```text
Spring annotations felt magical
```

Now:

```text
Bean
Container
Dependency Injection
DTO
Serialization
```

are starting to make practical sense because you encountered real errors and fixed them.

---

### Next Saturday Goal

Focus only on:

```text
Producer
↓
Kafka Topic
↓
Consumer
↓
PositionProcessor
↓
Redis Update
```

Once you see:

```text
POST Location
↓
Kafka Message Produced
↓
Consumer Receives
↓
Redis Updated
```

you'll have completed the core Week 3 objective.

That's a solid day's work. You didn't just learn Kafka commands—you learned how Spring Boot, Kafka, DTOs, serialization, and dependency injection fit together in a real backend system.
