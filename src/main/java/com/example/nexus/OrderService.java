package com.example.nexus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.nexus.h3.H3Service;
import com.example.nexus.kafka.OrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private static final Logger log =
            LoggerFactory.getLogger(OrderService.class);
    OrderProducer orderProducer;
    OrderRepository orderRepository;
    H3Service h3Service;
    public OrderService(OrderProducer orderProducer, OrderRepository orderRepository, H3Service h3Service) {
        this.orderProducer = orderProducer;
        this.orderRepository = orderRepository;
        this.h3Service = h3Service;
    }
    public void createOrder(String foodName, double lat, double lan) {
        String restaurantCell = h3Service.getH3Id(lat, lan);
        Order order = new Order(foodName, restaurantCell);
        orderRepository.save(order);
        log.info("Order created, id: " + order.id + ", restuarntCell: " + order.restaurantH3);
        OrderCreatedEvent event = new OrderCreatedEvent(order);
        orderProducer.publishOrder(event);
    }

    public void updateOrderStatus(Order order, Order.OrderStatus orderStatus) {

    }
}
