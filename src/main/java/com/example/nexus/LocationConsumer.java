package com.example.nexus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class LocationConsumer {
    @Autowired
    private final PositionProcessor positionProcessor;
    public LocationConsumer(PositionProcessor positionProcessor) {
        this.positionProcessor = positionProcessor;
    }

    @KafkaListener(topics="rider-location-co-ordinates", groupId="location-consumer-group")
    public void consume(RiderCoordinatesDTO riderCoordinatesDTO) {
        positionProcessor.updateRiderId(riderCoordinatesDTO);
    }
}
