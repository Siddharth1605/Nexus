
## Week2

RiderStateStore → Wraps all redis operations. 

Uses StringRedisTemplate → for string operations

Flow : If a rider updates his location, h3cell will be calculated in O(1)

Then 3 keys will be updated in redis

rider:{riderid}:h3cell = newH3Cell

rider:{riderid}:status = idle

idle-riders:{h3cell} append riderid

claimrider() - to claim a rider if his status is not ON_Delivery, so orders can claim him 

We though of using `set rider:{riderid}:status ON_DELIVERY NX (atomicity)

But the catch is if status is “idle” it wont work, and we are using with sprint.data.redis we are going with lua script ( for atomicity - no redis commands will execute on the server, until this script is executed completely)

<img width="821" height="514" alt="week2-redis" src="https://github.com/user-attachments/assets/edd84bf2-3989-44d0-bf6c-0bd9b27f709f" />

Without redis and h3 the operation would be costly - “Check all riders → calculate distance → slow”

But now it is O(1) : Convert to h3cell, lookup in redis set

We are **grouping riders before order arrives**

Since these values are need to be instantly avaialble , we are not putting them in db  but in redis-cache to access them fastly.
