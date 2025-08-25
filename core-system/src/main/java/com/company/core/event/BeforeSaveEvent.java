package com.company.core.event;

import com.company.core.model.Order;
import org.springframework.context.ApplicationEvent;

/**
 * 订单处理事件基类 - 简化事件机制设计
 * 核心设计：统一事件接口，避免重复代码
 */
public abstract class OrderEvent extends ApplicationEvent {
    private final Order order;
    private final String phase;
    private boolean skipDefaultAction = false;

    protected OrderEvent(Object source, Order order, String phase) {
        super(source);
        this.order = order;
        this.phase = phase;
    }

    public Order getOrder() { return order; }
    public String getPhase() { return phase; }
    
    public void skipDefaultAction() { this.skipDefaultAction = true; }
    public boolean isSkipDefaultAction() { return skipDefaultAction; }
}

/**
 * 保存前事件
 */
class BeforeSaveEvent extends OrderEvent {
    public BeforeSaveEvent(Object source, Order order) {
        super(source, order, "BEFORE_SAVE");
    }
}