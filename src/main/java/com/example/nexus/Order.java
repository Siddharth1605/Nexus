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
    public UUID id;

    public String foodName;

    public String restaurantH3;

    @Enumerated(EnumType.STRING)
    public OrderStatus status;

    public LocalDateTime createdAt;

    public Order(String foodName, String restaurantH3) {
        this.foodName = foodName;
        this.restaurantH3 = restaurantH3;
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    // Getters & Setters
}