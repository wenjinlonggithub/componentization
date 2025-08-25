package com.company.tenant1.listener;

import com.company.core.event.BusinessEvent;
import com.company.core.model.BusinessContext;
// NOTE: Spring imports commented out for standalone compilation
// import org.springframework.context.event.EventListener;
// import org.springframework.stereotype.Component;

/**
 * Tenant1 Order Event Listener - using unified BusinessEvent
 * NOTE: Spring annotations commented out for standalone compilation
 */
// @Component
public class OrderEventListener {

    // @EventListener
    public void handleBusinessEvent(BusinessEvent event) {
        if (event.getContext() == null || !"order".equals(event.getContext().getScenario())) {
            return; // Only handle order events
        }

        BusinessContext context = event.getContext();
        String phase = event.getPhase();
        
        System.out.println("Tenant1 Event Listener: Processing " + phase + " for order scenario");
        
        if ("BEFORE_PROCESS".equals(phase)) {
            handleBeforeProcess(context, event);
        } else if ("AFTER_PROCESS".equals(phase)) {
            handleAfterProcess(context, event);
        }
    }

    private void handleBeforeProcess(BusinessContext context, BusinessEvent event) {
        System.out.println("Tenant1 Event Listener: Pre-processing for order");
        
        // Traditional order-specific logic
        Object orderId = context.getData().get("orderId");
        if (orderId != null) {
            System.out.println("Tenant1: Pre-processing order " + orderId);
        }
    }

    private void handleAfterProcess(BusinessContext context, BusinessEvent event) {
        System.out.println("Tenant1 Event Listener: Post-processing for order");
        
        // Update inventory simulation
        updateInventory(context);
        
        // Custom notification logic
        handleCustomNotification(context, event);
    }

    private void updateInventory(BusinessContext context) {
        Object orderId = context.getData().get("orderId");
        System.out.println("Tenant1: Updating inventory for order " + orderId);
    }

    private void handleCustomNotification(BusinessContext context, BusinessEvent event) {
        System.out.println("Tenant1 Event Listener: Custom notification logic");
        
        // Simulate SMS-only notification preference
        Object userId = context.getData().get("userId");
        if (userId != null && shouldUseSmsOnly(context)) {
            event.skipDefaultAction();
            System.out.println("Default email notification skipped for tenant1 - using SMS only");
        }
    }

    private boolean shouldUseSmsOnly(BusinessContext context) {
        // Simulate SMS preference based on business context
        String notificationPreference = context.getAttribute("notification.preference");
        return "SMS_ONLY".equals(notificationPreference) || 
               context.getData().get("phone") != null;
    }
}