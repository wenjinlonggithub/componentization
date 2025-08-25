package com.company.tenant1.processor;

import com.company.core.model.Order;
import com.company.core.processor.DefaultOrderProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CustomOrderProcessor extends DefaultOrderProcessor {

    @Override
    protected void beforeSave(Order order) {
        if (order.getAmount().compareTo(new BigDecimal("10000")) > 0) {
            audit(order);
        }
        System.out.println("Tenant1 custom validation completed for order: " + order.getId());
    }

    @Override
    protected void beforeNotify(Order order) {
        if (order.getUser() != null && order.getUser().getPhone() != null) {
            System.out.println("Tenant1 SMS notification: Order " + order.getId() + 
                             " created for " + order.getUser().getPhone());
        }
    }

    private void audit(Order order) {
        System.out.println("AUDIT REQUIRED: High-value order " + order.getId() + 
                         " with amount " + order.getAmount() + " needs manager approval");
        order.setStatus("PENDING_APPROVAL");
    }
}