package com.company.core.processor;

import com.company.core.event.AfterSaveEvent;
import com.company.core.event.BeforeNotifyEvent;
import com.company.core.event.BeforeSaveEvent;
import com.company.core.model.Order;
import com.company.core.model.User;
import com.company.core.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DefaultOrderProcessor implements OrderProcessor {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private NotificationService notificationService;

    @Override
    public void process(Order order) {
        validate(order);
        calculatePrice(order);
        
        BeforeSaveEvent beforeSaveEvent = new BeforeSaveEvent(this, order);
        eventPublisher.publishEvent(beforeSaveEvent);
        beforeSave(order);
        
        if (!beforeSaveEvent.isSkipDefaultAction()) {
            save(order);
            eventPublisher.publishEvent(new AfterSaveEvent(this, order));
        }
        
        BeforeNotifyEvent beforeNotifyEvent = new BeforeNotifyEvent(this, order);
        eventPublisher.publishEvent(beforeNotifyEvent);
        beforeNotify(order);
        
        if (!beforeNotifyEvent.isSkipDefaultAction()) {
            notifyUser(order);
        }
    }

    protected void validate(Order order) {
        if (order.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Order amount must be positive");
        }
        System.out.println("Order validated: " + order.getId());
    }

    protected void calculatePrice(Order order) {
        System.out.println("Price calculated for order: " + order.getId());
    }

    protected void beforeSave(Order order) {
    }

    protected void save(Order order) {
        order.setStatus("SAVED");
        System.out.println("Order saved: " + order.getId());
    }

    protected void beforeNotify(Order order) {
    }

    protected void notifyUser(Order order) {
        if (order.getUser() != null && order.getUser().getEmail() != null) {
            notificationService.sendEmail(order.getUser().getEmail(), 
                "Your order " + order.getId() + " has been created");
        }
    }
}