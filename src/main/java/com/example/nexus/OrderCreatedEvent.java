package com.example.nexus;

import com.example.nexus.Order;

import java.util.UUID;

public class OrderCreatedEvent {

    private UUID orderId;
    private String foodName;
    private String restaurantH3;
    private Order.OrderStatus status;
    private String createdAt;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(Order order) {
        this.orderId = order.id;
        this.foodName = order.foodName;
        this.restaurantH3 = order.restaurantH3;
        this.status = order.status;
        this.createdAt = order.createdAt.toString();
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getRestaurantH3() {
        return restaurantH3;
    }

    public Order.OrderStatus getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}