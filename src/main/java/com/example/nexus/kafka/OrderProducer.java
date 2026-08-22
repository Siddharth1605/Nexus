package com.example.nexus.kafka;

import com.example.nexus.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private static final Logger log =
            LoggerFactory.getLogger(OrderProducer.class);

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrder(OrderCreatedEvent event) {
        kafkaTemplate.send("order-details", event);
        log.info(
                "Order published to order-details, id: {}",
                event.getOrderId()
        );
    }
}