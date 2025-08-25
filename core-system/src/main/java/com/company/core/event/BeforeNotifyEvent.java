package com.company.core.event;

import com.company.core.model.Order;

/**
 * 通知前事件
 */
public class BeforeNotifyEvent extends OrderEvent {
    public BeforeNotifyEvent(Object source, Order order) {
        super(source, order, "BEFORE_NOTIFY");
    }
}