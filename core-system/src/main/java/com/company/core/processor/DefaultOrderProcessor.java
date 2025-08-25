package com.company.core.processor;

import com.company.core.event.AfterSaveEvent;
import com.company.core.event.BeforeNotifyEvent;
import com.company.core.event.BeforeSaveEvent;
import com.company.core.model.Order;
import com.company.core.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 默认订单处理器 - 体现组件化核心设计模式
 * 
 * 设计要点：
 * 1. 模板方法模式：定义处理流程，开放扩展钩子
 * 2. 事件机制：关键节点发布事件，支持松耦合扩展  
 * 3. 条件执行：事件监听器可跳过默认行为
 */
@Component
public class DefaultOrderProcessor implements OrderProcessor {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private NotificationService notificationService;

    @Override
    public void process(Order order) {
        // 1. 核心业务流程
        validate(order);
        calculatePrice(order);
        
        // 2. 保存阶段：钩子方法 + 事件机制双重扩展
        BeforeSaveEvent beforeSaveEvent = publishEvent(new BeforeSaveEvent(this, order));
        beforeSave(order);  // 扩展点1：继承覆盖
        
        if (!beforeSaveEvent.isSkipDefaultAction()) {
            save(order);
            publishEvent(new AfterSaveEvent(this, order));
        }
        
        // 3. 通知阶段：同样的扩展模式
        BeforeNotifyEvent beforeNotifyEvent = publishEvent(new BeforeNotifyEvent(this, order));
        beforeNotify(order);  // 扩展点2：继承覆盖
        
        if (!beforeNotifyEvent.isSkipDefaultAction()) {
            notifyUser(order);
        }
    }

    // 核心业务方法
    protected void validate(Order order) {
        if (order.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Order amount must be positive");
        }
    }

    protected void calculatePrice(Order order) {
        // 价格计算逻辑
    }

    protected void save(Order order) {
        order.setStatus("SAVED");
    }

    protected void notifyUser(Order order) {
        if (order.getUser() != null && order.getUser().getEmail() != null) {
            notificationService.sendEmail(order.getUser().getEmail(), 
                "Your order " + order.getId() + " has been created");
        }
    }

    // 扩展钩子方法 - 子类可覆盖实现定制逻辑
    protected void beforeSave(Order order) { }
    protected void beforeNotify(Order order) { }
    
    // 工具方法：简化事件发布
    private <T> T publishEvent(T event) {
        eventPublisher.publishEvent(event);
        return event;
    }
}