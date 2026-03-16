package com.example.backend_spring.kafka;

import com.example.backend_spring.dto.OrderCreatedEvent;
import com.example.backend_spring.service.OrderProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedConsumer {

    @Autowired
    private OrderProcessingService orderProcessingService;

    @KafkaListener(topics = "order-created", groupId = "order-group")
    public void consume(OrderCreatedEvent event) {

        System.out.println("=================================");
        System.out.println("KAFKA EVENT RECEIVED");
        System.out.println("Order ID: " + event.getOrderId());
        System.out.println("User ID: " + event.getUserId());
        System.out.println("=================================");

        orderProcessingService.processOrder(event);

        System.out.println("Order processed successfully!");
    }
}