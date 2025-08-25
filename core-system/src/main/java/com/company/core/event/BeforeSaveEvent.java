package com.company.core.event;

import com.company.core.model.Order;
import org.springframework.context.ApplicationEvent;

public class BeforeSaveEvent extends ApplicationEvent {
    private final Order order;
    private boolean skipDefaultAction = false;

    public BeforeSaveEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }

    public Order getOrder() { return order; }

    public void skipDefaultAction() { this.skipDefaultAction = true; }
    public boolean isSkipDefaultAction() { return skipDefaultAction; }
}