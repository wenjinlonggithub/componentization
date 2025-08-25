package com.company.tenant1.listener;

import com.company.core.event.AfterSaveEvent;
import com.company.core.event.BeforeNotifyEvent;
import com.company.core.event.BeforeSaveEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    @EventListener
    public void handleBeforeSave(BeforeSaveEvent event) {
        System.out.println("Tenant1 Event Listener: Pre-save processing for order " + 
                         event.getOrder().getId());
    }

    @EventListener
    public void handleAfterSave(AfterSaveEvent event) {
        System.out.println("Tenant1 Event Listener: Post-save processing for order " + 
                         event.getOrder().getId());
        updateInventory(event.getOrder());
    }

    @EventListener
    public void handleBeforeNotify(BeforeNotifyEvent event) {
        System.out.println("Tenant1 Event Listener: Custom notification logic for order " + 
                         event.getOrder().getId());
        if (shouldUseSmsOnly(event.getOrder())) {
            event.skipDefaultAction();
            System.out.println("Default email notification skipped for tenant1");
        }
    }

    private void updateInventory(com.company.core.model.Order order) {
        System.out.println("Tenant1: Updating inventory for order " + order.getId());
    }

    private boolean shouldUseSmsOnly(com.company.core.model.Order order) {
        return order.getUser() != null && order.getUser().getPhone() != null;
    }
}