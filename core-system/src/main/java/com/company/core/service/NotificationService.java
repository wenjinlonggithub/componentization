package com.company.core.service;

import com.company.core.model.Order;
import com.company.core.model.User;
// NOTE: Spring imports commented out for standalone compilation
// import org.springframework.stereotype.Service;

/**
 * Default Notification Service
 * Provides basic notification capabilities
 * NOTE: Spring annotations commented out for standalone compilation
 */
// @Service
public class NotificationService {
    
    /**
     * Send notification for order
     */
    public void notifyOrder(Order order) {
        System.out.println("NOTIFICATION: Order processed - " + order.getId());
        
        if (order.getUser() != null) {
            notifyUser(order.getUser(), "Your order " + order.getId() + " has been processed");
        }
    }
    
    /**
     * Send notification to user
     */
    public void notifyUser(User user, String message) {
        System.out.println("NOTIFICATION: Sending to user " + user.getId() + " - " + message);
        
        // Email notification simulation
        if (user.getEmail() != null) {
            sendEmail(user.getEmail(), message);
        }
        
        // SMS notification simulation  
        if (user.getPhone() != null) {
            sendSMS(user.getPhone(), message);
        }
    }
    
    /**
     * Send email notification
     */
    public void sendEmail(String email, String message) {
        System.out.println("EMAIL: To " + email + " - " + message);
    }
    
    /**
     * Send SMS notification
     */
    public void sendSMS(String phone, String message) {
        System.out.println("SMS: To " + phone + " - " + message);
    }
    
    /**
     * Send business context notification
     */
    public void notifyBusiness(String scenario, String businessId, String message) {
        System.out.println("BUSINESS NOTIFICATION: " + scenario + " [" + businessId + "] - " + message);
    }
}