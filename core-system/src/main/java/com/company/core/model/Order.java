package com.company.core.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Traditional Order model for compatibility
 */
public class Order {
    
    private String id;
    private String userId;
    private BigDecimal amount;
    private String status = "CREATED";
    private LocalDateTime createTime;
    private User user;

    public Order() {
        this.createTime = LocalDateTime.now();
    }

    public Order(String userId, BigDecimal amount) {
        this();
        this.userId = userId;
        this.amount = amount;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @Override
    public String toString() {
        return String.format("Order{id='%s', userId='%s', amount=%s, status='%s'}", 
                           id, userId, amount, status);
    }
}