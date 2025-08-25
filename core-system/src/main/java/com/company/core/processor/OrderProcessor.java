package com.company.core.processor;

import com.company.core.model.Order;

public interface OrderProcessor {
    void process(Order order);
}