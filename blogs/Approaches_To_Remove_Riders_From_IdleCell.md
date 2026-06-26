Approach 1 — Lazy cleanup at read time (simplest, recommended for Nexus)
When your Matching Engine reads candidates from idle-riders:{cellId}, for each candidate rider ID it does a quick check — does rider:{id}:h3 still exist? If the key is gone (expired), skip that rider as a candidate and also issue a SREM to clean them out of the SET while you are there. This is sometimes called "lazy deletion" — you do not proactively clean up, you clean up the moment you discover staleness, which is exactly when it matters most (during matching).
This is the simplest to implement and the most natural fit for your project, because the cost of stale entries only matters at the moment of matching anyway. If nobody tries to match against rider 42, it does not matter that their stale ID sits in the SET for a while.

Approach 2 — Redis Keyspace Notifications (more advanced, real production pattern)
Redis can be configured to publish an event whenever a key expires. You would subscribe to these expiry events and run a cleanup handler that removes the rider from whatever idle-riders SET they were last known to be in. This is the technically complete solution, but it requires tracking which cell a rider was last in even after their primary key expires (since by the time you get the notification; you only know the key name rider:42:h3 expired, not which cell value it held).
This is good to know about and mention in interviews as the production-grade approach, but heavier to implement than your project needs.

Approach 3 — A periodic sweep
Run a scheduled job every minute that scans all idle-riders:* SETs, checks each member against their rider:{id}:h3 key, and removes stale entries. This works but is wasteful — you are scanning potentially thousands of entries on a timer regardless of whether anyone is matching against them.

What I Recommend for Nexus
Use Approach 1 — lazy cleanup at read time. Here is why it fits your project specifically:
It requires no new infrastructure. It naturally happens exactly when staleness would actually cause a problem — during matching. It is simple enough to implement in an hour. It is also a legitimate, defensible interview answer:

"I considered Redis keyspace notifications for proactive cleanup, but chose lazy cleanup at read time instead — when the Matching Engine reads candidates from the idle pool, it verifies the rider's position key still exists and self-heals the SET by removing stale entries it encounters. This avoids running background sweep jobs and only pays the cleanup cost exactly when staleness would otherwise cause an incorrect assignment."
