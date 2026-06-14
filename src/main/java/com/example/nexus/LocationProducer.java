package com.example.nexus;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LocationProducer {
    private final KafkaTemplate<String, RiderCoordinatesDTO> kafkaTemplate;

    public LocationProducer(KafkaTemplate<String, RiderCoordinatesDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishRiderId(RiderCoordinatesDTO riderCoordinatesDTO) {
        this.kafkaTemplate.send("rider-location-co-ordinates", riderCoordinatesDTO);
    }
}
