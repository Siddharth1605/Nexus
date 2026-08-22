package com.example.nexus.h3;


import com.example.nexus.Order;
import com.example.nexus.OrderCreatedEvent;
import com.example.nexus.kafka.OrderConsumer;
import com.example.nexus.redis.RiderStateStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.lang.Thread.sleep;

@Service
public class MatchingEngine {

    private static final Logger log =
            LoggerFactory.getLogger(MatchingEngine.class);

    private final H3Service h3;
    private final RiderStateStore redis;

    public MatchingEngine(H3Service h3, RiderStateStore redis) {
        this.h3 = h3;
        this.redis = redis;
    }

    public Optional<String> findAndAssign(OrderCreatedEvent order)
            throws InterruptedException {
        String cell = order.getRestaurantH3();
        log.info("Matching order {} at H3 cell {}",
                order.getOrderId(), cell);
        for (int ring = 1; ring <= 3; ring++) {
            List<String> cells = h3.getRingCells(cell, ring);
            log.info("Ring {} contains {} cells", ring, cells.size());
            for (String c : cells) {
                Set<String> candidates = redis.getIdleRiders(c);
                log.info("Checking cell {}, candidates: {}", c, candidates);
                for (String riderId : candidates) {
                    log.info("Trying to claim rider {}", riderId);
                    boolean claimed = redis.claimRider(riderId);
                    log.info(
                            "Claim result for rider {} = {}",
                            riderId,
                            claimed
                    );
                    if (claimed) {
                        redis.removeFromIdlePool(c, riderId);
                        log.info(
                                "Rider {} claimed for order {}",
                                riderId,
                                order.getOrderId()
                        );
                        return Optional.of(riderId);
                    }
                }
            }
            if (ring < 3) {
                sleep(500);
            }
        }
        log.info("No rider found for order {}", order.getOrderId());
        return Optional.empty();
    }
}
/*
What will happen if restaurant of different orders belong to same h3cell,(multiple riders or 1 rider or 0 rider available)

 */