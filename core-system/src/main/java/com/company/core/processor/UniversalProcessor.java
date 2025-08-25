package com.company.core.processor;

import com.company.core.event.BusinessEvent;
import com.company.core.model.BusinessContext;
import com.company.core.model.Order;
// NOTE: Spring imports removed for standalone compilation
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
// import org.springframework.context.ApplicationEventPublisher;
// import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Universal Business Processor - supports multiple business scenarios
 * NOTE: Spring annotations commented out for standalone compilation
 */
// @Component
// @ConditionalOnMissingBean(OrderProcessor.class)
public class UniversalProcessor implements OrderProcessor {

    // @Autowired
    // private ApplicationEventPublisher eventPublisher;

    /**
     * Process business context - main new capability
     */
    public ProcessResult processBusiness(BusinessContext context) {
        try {
            System.out.printf("PROCESSING: %s%n", context);
            
            // Validation
            if (context.getScenario() == null || context.getBusinessType() == null) {
                return new ProcessResult(false, "Invalid business context", null);
            }

            // Extension point execution - BEFORE
            beforeProcess(context);
            publishBusinessEvent("BEFORE_PROCESS", context);

            // Core business logic
            processCoreLogic(context);

            // Extension point execution - AFTER
            afterProcess(context);
            publishBusinessEvent("AFTER_PROCESS", context);

            context.setStatus("PROCESSED");
            
            return new ProcessResult(true, "Processing completed successfully", context.getData());

        } catch (Exception e) {
            System.err.println("Processing failed: " + e.getMessage());
            return new ProcessResult(false, "Processing failed: " + e.getMessage(), null);
        }
    }


    /**
     * Hook methods - can be overridden by subclasses
     */
    protected void beforeProcess(BusinessContext context) {
        System.out.println("HOOK: beforeProcess");
    }

    protected void afterProcess(BusinessContext context) {
        System.out.println("HOOK: afterProcess");
    }

    // Core business logic dispatch
    private void processCoreLogic(BusinessContext context) {
        if (!isValidContext(context)) {
            return;
        }
        
        switch (context.getScenario()) {
            case "order":
                processOrderBusiness(context);
                break;
            case "medical":
                processMedicalBusiness(context);
                break;
            default:
                processDefaultBusiness(context);
                break;
        }
    }

    private void processOrderBusiness(BusinessContext context) {
        System.out.println("CORE: Order business processing");
        context.setAttribute("processed.by", "order.handler");
    }

    private void processMedicalBusiness(BusinessContext context) {
        System.out.println("CORE: Medical business processing");
        context.setAttribute("processed.by", "medical.handler");
    }

    private void processDefaultBusiness(BusinessContext context) {
        System.out.println("CORE: Default business processing");
        context.setAttribute("processed.by", "default.handler");
    }

    // Utility methods
    private boolean isValidContext(BusinessContext context) {
        return context.getScenario() != null && context.getBusinessType() != null;
    }

    private void publishBusinessEvent(String phase, BusinessContext context) {
        BusinessEvent event = new BusinessEvent(phase, context);
        // NOTE: Event publishing simulated for standalone compilation
        System.out.println("EVENT: " + phase + " for " + context.getScenario());
        // eventPublisher.publishEvent(event);
    }

    // Traditional Order processing compatibility
    @Override
    public Order processOrder(Order order) {
        System.out.println("LEGACY: Traditional order processing");
        return order;
    }

    /**
     * Simple processing result class
     */
    public static class ProcessResult {
        private boolean success;
        private String message;
        private Map<String, Object> data;

        public ProcessResult(boolean success, String message, Map<String, Object> data) {
            this.success = success;
            this.message = message;
            this.data = data != null ? data : new HashMap<>();
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Map<String, Object> getData() { return data; }
    }
}