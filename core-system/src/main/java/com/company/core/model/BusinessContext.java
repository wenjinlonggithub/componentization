package com.company.core.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Universal Business Context - supports multiple business scenarios like order, medical, etc.
 */
public class BusinessContext {
    
    private String businessId;
    private String scenario;           // Business scenario: order, medical, etc
    private String businessType;       // Business type: ORDER_PROCESS, PRESCRIPTION, etc
    private String tenantId;
    private String operatorId;
    private LocalDateTime timestamp;
    
    // Universal business data container
    private Map<String, Object> data = new HashMap<>();
    private Map<String, String> attributes = new HashMap<>();
    private String status = "CREATED";

    public BusinessContext() {
        this.timestamp = LocalDateTime.now();
    }

    public BusinessContext(String scenario, String businessType) {
        this();
        this.scenario = scenario;
        this.businessType = businessType;
    }

    // Convenience methods
    public void putData(String key, Object value) {
        this.data.put(key, value);
    }
    
    public <T> T getData(String key, Class<T> type) {
        return type.cast(this.data.get(key));
    }
    
    public void setAttribute(String key, String value) {
        this.attributes.put(key, value);
    }
    
    public String getAttribute(String key) {
        return this.attributes.get(key);
    }

    // Getters and Setters
    public String getBusinessId() { return businessId; }
    public void setBusinessId(String businessId) { this.businessId = businessId; }

    public String getScenario() { return scenario; }
    public void setScenario(String scenario) { this.scenario = scenario; }

    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getOperatorId() { return operatorId; }
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Map<String, String> getAttributes() { return attributes; }
    public void setAttributes(Map<String, String> attributes) { this.attributes = attributes; }

    @Override
    public String toString() {
        return String.format("BusinessContext{scenario='%s', businessType='%s', businessId='%s', tenantId='%s'}", 
                           scenario, businessType, businessId, tenantId);
    }
}