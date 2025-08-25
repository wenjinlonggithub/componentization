package com.company.core.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {
    private String id;
    private String userId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;
    private User user;

    public Order() {}

    public Order(String id, String userId, BigDecimal amount) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.status = "CREATED";
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}