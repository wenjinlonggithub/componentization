package com.company.core.event;

import com.company.core.model.Order;
import org.springframework.context.ApplicationEvent;

public class AfterSaveEvent extends ApplicationEvent {
    private final Order order;

    public AfterSaveEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }

    public Order getOrder() { return order; }
}