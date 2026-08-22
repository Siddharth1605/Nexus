package com.example.nexus.config;


import com.example.nexus.OrderCreatedEvent;
import com.example.nexus.RiderCoordinatesDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    private Map<String, Object> baseConsumerConfig() {

        Map<String, Object> config = new HashMap<>();

        config.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092"
        );

        config.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class
        );

        config.put(
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest"
        );

        return config;
    }

    // -------------------------------
    // Rider Location Consumer
    // -------------------------------

    @Bean
    public ConsumerFactory<String, RiderCoordinatesDTO>
    riderLocationConsumerFactory() {

        Map<String, Object> config = baseConsumerConfig();

        JsonDeserializer<RiderCoordinatesDTO> deserializer =
                new JsonDeserializer<>(RiderCoordinatesDTO.class);

        deserializer.addTrustedPackages("com.example.nexus");

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RiderCoordinatesDTO>
    riderLocationKafkaListenerContainerFactory(
            ConsumerFactory<String, RiderCoordinatesDTO> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, RiderCoordinatesDTO>
                factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        return factory;
    }


    // -------------------------------
    // Order Consumer
    // -------------------------------

    @Bean
    public ConsumerFactory<String, OrderCreatedEvent>
    orderConsumerFactory() {

        Map<String, Object> config = baseConsumerConfig();

        JsonDeserializer<OrderCreatedEvent> deserializer =
                new JsonDeserializer<>(OrderCreatedEvent.class);

        deserializer.addTrustedPackages("com.example.nexus.kafka");

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent>
    orderKafkaListenerContainerFactory(
            ConsumerFactory<String, OrderCreatedEvent> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent>
                factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        return factory;
    }
}