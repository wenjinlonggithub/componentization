package com.company.tenant1.app.dto;

import java.math.BigDecimal;

public class CreateOrderRequest {
    private String userId;
    private BigDecimal amount;
    private String userName;
    private String userEmail;
    private String userPhone;

    // 构造函数
    public CreateOrderRequest() {}

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserPhone() { return userPhone; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }
}