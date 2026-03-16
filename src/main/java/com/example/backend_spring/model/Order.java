package com.example.backend_spring.model;

import jakarta.persistence.*;

@Entity
@Table(name="orders")
public class Order {

    @Id //  mark as a primary
    @GeneratedValue(strategy = GenerationType.IDENTITY) //  use to auto-genrate the value
    private Long id;

    @Column(name="user_id")
    private Long userId;

    private String status;

    @Column(name="total_amount")
    private Double totalAmount;

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}


//@Entity
//@Table(name = "orders")
//public class Order {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "user_id", nullable = false)   // ← enforce NOT NULL
//    private Long userId;
//
//    @Column(nullable = false, length = 20)         // ← limit status length
//    private String status;
//
//    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
//    private BigDecimal totalAmount;                // ← BigDecimal for money!
//
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;              // ← track when order was made
//
//    @PrePersist
//    protected void onCreate() {
//        createdAt = LocalDateTime.now();           // ← auto-set on save
//    }
//}