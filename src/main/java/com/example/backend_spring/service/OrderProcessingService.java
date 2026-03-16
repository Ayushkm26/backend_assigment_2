package com.example.backend_spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend_spring.repository.ProductRepository;
import com.example.backend_spring.repository.OrderRepository;
import com.example.backend_spring.model.Product;
import com.example.backend_spring.model.Order;
import com.example.backend_spring.dto.OrderCreatedEvent;
import com.example.backend_spring.dto.OrderItemEvent;

@Service
public class OrderProcessingService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional  // <-- ensures all DB operations are atomic
    public void processOrder(OrderCreatedEvent event) {

        boolean failed = false;

        // Check stock availability
        for (OrderItemEvent item : event.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

            if (product.getStock() < item.getQty()) {
                failed = true;
                break;
            }
        }

        // Fetch the order
        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found: " + event.getOrderId()));

        if (failed) {
            order.setStatus("FAILED");
        } else {
            // Deduct stock
            for (OrderItemEvent item : event.getItems()) {
                Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

                product.setStock(product.getStock() - item.getQty());
                productRepository.save(product); // save stock changes
            }

            order.setStatus("COMPLETED");
        }

        orderRepository.save(order); // persist order status
    }
}