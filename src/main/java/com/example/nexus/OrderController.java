package com.example.nexus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    OrderService orderService;
    private static final Logger log =
            LoggerFactory.getLogger(OrderController.class);
    @PostMapping("/restaurantDetails")
    public String postOrder(@RequestParam String foodname, @RequestParam double lat, @RequestParam double lan) {
        log.info("/restaurantDetails hit : " + foodname + ", latitude: " + lat + ", longitude: " + lan);
        orderService.createOrder(foodname, lat, lan);
        return "Order will be completed";
    }
}
