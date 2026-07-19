package com.example.nexus;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    public enum OrderStatus {
        PENDING,
        ASSIGNED,
        DELIVERED,
        FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable =false)
    private UUID id;

    private String foodName;

    private Double restaurantLat;

    private Double restaurantLng;

    private String restaurantH3;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;

    public Order() {
    }

    // Getters & Setters
}