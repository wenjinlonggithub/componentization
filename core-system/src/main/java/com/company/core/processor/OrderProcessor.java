package com.company.core.processor;

import com.company.core.model.Order;

/**
 * Order Processor interface for compatibility
 */
public interface OrderProcessor {
    
    /**
     * Process traditional order
     */
    Order processOrder(Order order);
    
    /**
     * Hook methods for extension
     */
    default void beforeSave(Order order) {
        // Default empty implementation
    }
    
    default void beforeNotify(Order order) {
        // Default empty implementation  
    }
}