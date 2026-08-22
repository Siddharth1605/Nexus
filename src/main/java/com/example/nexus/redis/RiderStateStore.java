package com.example.nexus.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@Service
public class RiderStateStore {
    private final StringRedisTemplate redis;
    private static final Duration TTL = Duration.ofMinutes(30);

    public RiderStateStore(StringRedisTemplate redis) {
        this.redis = redis;
    }

    /**
     * Will update h3Cell of string rider:riderId:h3Cell
     * Will remove riders from old cell's set and add in new cell's set here
     * @param riderId
     * @param h3CellId
     */
    public void updateCell(String riderId, String h3CellId) {
        String cellKey = "rider:" + riderId + ":h3Cell";
        String oldCell = Objects.toString(redis.opsForValue().get(cellKey),"");
        redis.opsForValue().set(cellKey, h3CellId, TTL);
        String setKey = "idle-riders:";
        if(!oldCell.equals(h3CellId)) {
            if(!oldCell.isEmpty()) {
                redis.opsForSet().remove(setKey + oldCell, riderId);
            }
            redis.opsForSet().add(setKey + h3CellId, riderId);
        }
    }

    public String getCell(String riderId) {
        String cellKey = "rider:" + riderId + ":h3Cell";
        return redis.opsForValue().get(cellKey);
    }

    public void setIdle(String riderId) {
        String cellKey = "rider:" + riderId + ":status";
        redis.opsForValue().set(cellKey, "idle", TTL);
//        String currentCell = getCell(riderId);
//        if(!Objects.isNull(currentCell)) {
//            String setKey = "idle-riders:";
//            redis.opsForSet().add(setKey + currentCell, riderId);
//        }
    }

    public boolean claimRider(String riderId) {
        String cellKey = "rider:" + riderId + ":status";
        String setKey = "idle-riders:";
        String luaScript = """
                if redis.call('GET', KEYS[1]) == 'idle' then
                    redis.call('SET', KEYS[1], 'ON_DELIVERY')
                    return 1
                else
                    return 0
                end
                """;
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(luaScript);
        redisScript.setResultType(Long.class);
        long result = redis.execute(redisScript, Collections.singletonList(cellKey));
        if (result == 0) {
            System.out.println("Rider : " + riderId + ", Not available. Looking for others");
            return false;
        }
        String currentCell = getCell(riderId);
        if(!Objects.isNull(currentCell)) {
            redis.opsForSet().remove(setKey + currentCell, riderId);
        }
        System.out.println("Rider : " + riderId + " is claimed");
        return true;
    }

    public Set<String> getIdleRiders(String h3Cell) {
        String setKey = "idle-riders:" + h3Cell;
        return redis.opsForSet().members(setKey);
    }

    public void removeFromIdlePool(String h3Cell, String riderId) {
        String setKey = "idle-riders:" + h3Cell;
        redis.opsForSet().remove(setKey,riderId);
    }
}


/*
logicalissue:
1. Putting TTL as 30 sec , but when it is deleted - why not delete from idleriderset ?
2. When a rider is on_delivery when he/she will change again the status and now we are hitting api as rider's persepective - but rider should auto hit their location, and what will happen if they hit when they are in ON_DELIVERY
 */