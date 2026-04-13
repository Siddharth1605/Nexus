package com.example.nexus.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;

@Service
public class RiderStateStore {
    private final StringRedisTemplate redis;
    private static final Duration TTL = Duration.ofSeconds(30);

    public RiderStateStore(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void updateCell(String riderId, String h3CellId) {
        String cellKey = "rider:" + riderId + ":h3Cell";
        String oldCell = redis.opsForValue().get(cellKey);

        redis.opsForValue().set(cellKey, h3CellId, TTL);
        String setKey = "idle-riders:";
        if(!Objects.isNull(oldCell) && !oldCell.equals(h3CellId)) {
            redis.opsForSet().remove(setKey + oldCell, riderId);
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
        String currentCell = getCell(riderId);
        if(!Objects.isNull(currentCell)) {
            String setKey = "idle-riders:";
            redis.opsForSet().add(setKey + currentCell, riderId);
        }
    }

    public String claimRider(String riderId) {
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
        if(result == 0)
            return "Rider : " + riderId + ", Not available. Looking for others";
        String currentCell = getCell(riderId);
        if(!Objects.isNull(currentCell)) {
            redis.opsForSet().remove(setKey + currentCell, riderId);
        }
        return "Rider : " + riderId + " is claimed";
    }
}
