package com.example.nexus.kafka;

import com.example.nexus.PositionProcessor;
import com.example.nexus.RiderCoordinatesDTO;
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

    @KafkaListener(topics="rider-location-co-ordinates", groupId="location-consumer-group",
            containerFactory = "riderLocationKafkaListenerContainerFactory"
    )
    public void consume(RiderCoordinatesDTO riderCoordinatesDTO) {
        positionProcessor.updateRiderId(riderCoordinatesDTO);
    }
}
