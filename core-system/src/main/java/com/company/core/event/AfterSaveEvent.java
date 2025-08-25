package com.company.core.event;

import com.company.core.model.Order;

/**
 * 保存后事件
 */
public class AfterSaveEvent extends OrderEvent {
    public AfterSaveEvent(Object source, Order order) {
        super(source, order, "AFTER_SAVE");
    }
}