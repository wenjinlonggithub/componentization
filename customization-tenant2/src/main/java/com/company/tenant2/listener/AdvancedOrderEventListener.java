package com.company.tenant2.listener;

import com.company.core.event.AfterSaveEvent;
import com.company.core.event.BeforeNotifyEvent;
import com.company.core.event.BeforeSaveEvent;
import com.company.core.model.Order;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AdvancedOrderEventListener {

    @EventListener
    @Order(1)
    public void handleBeforeSave(BeforeSaveEvent event) {
        Order order = event.getOrder();
        if (requiresComplexValidation(order)) {
            performComplexValidation(order);
        }
    }

    @EventListener
    public void handleAfterSave(AfterSaveEvent event) {
        Order order = event.getOrder();
        integrateWithExternalSystem(order);
        generateAdvancedReports(order);
    }

    @EventListener
    public void handleBeforeNotify(BeforeNotifyEvent event) {
        Order order = event.getOrder();
        if (shouldUseAdvancedNotification(order)) {
            sendAdvancedNotification(order);
            event.skipDefaultAction();
        }
    }

    private boolean requiresComplexValidation(Order order) {
        return order.getAmount().compareTo(new BigDecimal("5000")) > 0;
    }

    private void performComplexValidation(Order order) {
        System.out.println("Tenant2: Performing complex validation for order " + order.getId());
        System.out.println("Tenant2: Checking fraud detection systems...");
        System.out.println("Tenant2: Validating customer credit score...");
    }

    private void integrateWithExternalSystem(Order order) {
        System.out.println("Tenant2: Integrating order " + order.getId() + " with ERP system");
        System.out.println("Tenant2: Syncing with warehouse management system");
    }

    private void generateAdvancedReports(Order order) {
        System.out.println("Tenant2: Generating advanced analytics report for order " + order.getId());
    }

    private boolean shouldUseAdvancedNotification(Order order) {
        return true;
    }

    private void sendAdvancedNotification(Order order) {
        System.out.println("Tenant2: Sending multi-channel notification for order " + order.getId());
        System.out.println("Tenant2: - Email notification sent");
        System.out.println("Tenant2: - SMS notification sent");
        System.out.println("Tenant2: - Push notification sent");
        System.out.println("Tenant2: - Dashboard alert created");
    }
}