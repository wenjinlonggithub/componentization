package com.company.tenant2.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDto {
    private String id;
    private String userId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;
    private String userName;
    private String userPhone;
    private String orderType;
    private String enterpriseId;
    private String workflowStatus;

    public OrderDto() {}

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

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserPhone() { return userPhone; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }

    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }

    public String getEnterpriseId() { return enterpriseId; }
    public void setEnterpriseId(String enterpriseId) { this.enterpriseId = enterpriseId; }

    public String getWorkflowStatus() { return workflowStatus; }
    public void setWorkflowStatus(String workflowStatus) { this.workflowStatus = workflowStatus; }
}