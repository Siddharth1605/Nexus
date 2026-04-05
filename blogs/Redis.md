## Redis - Remote Dictionary Server

1. In-Memory database

If an application uses multiple db-service like sql db, document db, graph db , cache - it will increase latency.

**Usage :** 

Redis provides all above db service in single platform.

Often used as cache to improve performance, but redis is a fully fledged primary database.

No need to maintain a separate cache service. 

Initializing schema and running redis db it way faster. 

## Data persistence and durability

We can use replicas of redis server - but all may have chance to go down at same time. 

Instead we use snapshotting(rdb) → dump.rdb :

Produces single-file at some time as backups. May lose the latest minutes of data.

Append only File(AOF):

Persist all operations one after the other in log file. 

Use combination of this to have better usage. 

#### Where are these persistence files stored ?

It is best to store these files in separate server in cloud from other main-application server. 

#### Redis on Flash:

Redis will store cache in memory before going to db. 

Redis on Flash : Stores frequently used key-values in-memory and other values in ssd

### Scaling Redis:

1. Clustering: 
    
    One primary redis-instance to write data
    
    Multiple secondary redis-instnace to read data
    
    For more cluster, we need more memory. 
    
    If we have all redis in single server, it is risk. So need to distribute it across servers
    
2. Sharding:
    
    Dividing dataset into smaller junks. 
    
    So instead of having one singel master instance to write data, we can have 4 sharded datasets with 4 master instances. So 4 master redis can write data to their part 
    
    Each shard will require less memory, by this we can scale horizontally.
    

Advantages: Multiple nodes, multiple replicas, Sharded

#### High Availabiltiy and Performance across multiple geographic locations: → By Active-Active Geo Distribution.

CRDT - Conflict Free Repliaced Data Types

Redis uses this mechanism to avoid conflict in dataset, if multiple servers from different locations change the data at same time.

#### Redis DataTypes and DataStructures:

Note that [**`SET`**](https://redis.io/docs/latest/commands/set/) will replace any existing value already stored into the key, 

In Redis, **TTL (Time To Live)** refers to **the remaining lifespan of a key before it is automatically deleted from the database**. It is a critical feature for managing memory and ensuring that stale data (like session tokens or temporary cache) does not persist indefinitely

TTL is the duration (usually in seconds) that a key will remain in Redis before it expires.
**Persistence**: By default, Redis keys are persistent and have no TTL 

When you run the [**TTL command**](https://redis.io/docs/latest/commands/ttl/), Redis returns an integer: [[1](https://pypi.org/project/redis/4.1.0/#:~:text=TTL%20and%20PTTL:%20The%20return%20value%20is,indicates%20that%20the%20key%20does%20not%20exist)), [2](https://www.tutorialspoint.com/redis/keys_ttl.htm)]
• **Positive Integer**: The remaining seconds until the key expires.
• **-1**: The key exists but has no associated expiration 
• **-2**: The key does not exist

 **How Expiration Works Internally**Redis does not delete a key the exact millisecond it expires. Instead, it uses two main strategies to reclaim memory: 
• **Passive Expiration**: The key is deleted only when a client attempts to access it and Redis discovers it has expired.
• **Active Expiration**: Redis periodically (10 times per second) tests a random sample of keys with TTLs. If more than 25% of the sampled keys are expired, it repeats the process to aggressively clear memory. 

**Overwriting**: Using the `SET` command to overwrite a key's value will **remove** any existing TTL unless the `KEEPTTL` flag is used.

 **Replication**: In a Redis Cluster, only the master node handles the expiration logic. When a key expires on the master, it sends a `DEL` command to its replicas.
