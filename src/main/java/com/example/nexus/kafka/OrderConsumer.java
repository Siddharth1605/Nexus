package com.example.nexus.kafka;
import com.example.nexus.OrderCreatedEvent;
import com.example.nexus.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.nexus.Order;
import com.example.nexus.h3.MatchingEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {
    private static final Logger log =
            LoggerFactory.getLogger(OrderConsumer.class);
    @Autowired
    MatchingEngine matchingEngine;

    @KafkaListener(topics="order-details", groupId="order-consumer-group",
            containerFactory = "orderKafkaListenerContainerFactory"
    )
    public void consume(OrderCreatedEvent order) throws InterruptedException {
        log.info("Order consumed by kafka-consumer, id: " + order.getOrderId());
        matchingEngine.findAndAssign(order);

    }
}
