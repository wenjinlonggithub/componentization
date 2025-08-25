package com.company.tenant2.listener;

import com.company.core.event.BusinessEvent;
import com.company.core.model.BusinessContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Tenant2 Event Listener - Event-Driven Extension Pattern
 */
@Component
@ConditionalOnProperty(name = "tenant.id", havingValue = "tenant2")
public class AdvancedOrderEventListener {

    private static final BigDecimal VALIDATION_THRESHOLD = new BigDecimal("5000");

    @EventListener
    @Order(1)
    public void handleBusinessEvent(BusinessEvent event) {
        if (!event.isForPhase("BEFORE_PROCESS") && !event.isForPhase("AFTER_PROCESS")) {
            return;
        }

        BusinessContext context = event.getContext();
        String phase = event.getPhase();
        
        System.out.println("TENANT2: Event handling " + phase + " for " + context.getScenario());

        if ("BEFORE_PROCESS".equals(phase)) {
            handleBeforeProcess(context, event);
        } else if ("AFTER_PROCESS".equals(phase)) {
            handleAfterProcess(context, event);
        }
    }

    private void handleBeforeProcess(BusinessContext context, BusinessEvent event) {
        String scenario = context.getScenario();
        
        switch (scenario) {
            case "order":
                handleOrderValidation(context, event);
                break;
            case "medical":
                handleMedicalValidation(context, event);
                break;
            case "analytics":
                handleAnalyticsValidation(context, event);
                break;
            case "warehouse":
                handleWarehouseValidation(context, event);
                break;
            case "customer-service":
                handleCustomerServiceValidation(context, event);
                break;
            case "marketing":
                handleMarketingValidation(context, event);
                break;
            case "quality":
                handleQualityValidation(context, event);
                break;
            default:
                handleDefaultValidation(context, event);
                break;
        }
    }

    private void handleOrderValidation(BusinessContext context, BusinessEvent event) {
        System.out.println("TENANT2: Intelligent fraud detection and fast track validation");
        
        Object amountObj = context.getData().get("amount");
        if (amountObj instanceof Number) {
            BigDecimal amount = new BigDecimal(amountObj.toString());
            if (amount.compareTo(VALIDATION_THRESHOLD) > 0) {
                System.out.println("TENANT2: Fraud detection triggered for amount: " + amount);
                context.setAttribute("fraud.check", "ADVANCED");
            }
        }

        Object itemCount = context.getData().get("itemCount");
        if (itemCount != null && (Integer) itemCount <= 3) {
            System.out.println("TENANT2: Fast track enabled for simple order");
            context.setAttribute("fast.track", "enabled");
        }
    }

    private void handleMedicalValidation(BusinessContext context, BusinessEvent event) {
        System.out.println("TENANT2: AI diagnosis assistance");
        context.setAttribute("ai.diagnosis.support", "ENABLED");
        context.setAttribute("drug.interaction.check", "ADVANCED");
    }

    private void handleAnalyticsValidation(BusinessContext context, BusinessEvent event) {
        System.out.println("TENANT2: Data quality check and ML model validation");
        context.setAttribute("data.quality.check", "ENABLED");
        context.setAttribute("ml.model.validation", "ADVANCED");
    }

    private void handleWarehouseValidation(BusinessContext context, BusinessEvent event) {
        System.out.println("TENANT2: Inventory optimization and supply chain analysis");
        context.setAttribute("inventory.optimization", "ENABLED");
        context.setAttribute("supply.chain.analysis", "REAL_TIME");
    }

    private void handleCustomerServiceValidation(BusinessContext context, BusinessEvent event) {
        System.out.println("TENANT2: Sentiment analysis and intelligent routing");
        context.setAttribute("sentiment.analysis", "ENABLED");
        context.setAttribute("intelligent.routing", "AI_POWERED");
    }

    private void handleMarketingValidation(BusinessContext context, BusinessEvent event) {
        System.out.println("TENANT2: User profiling and recommendation algorithm");
        context.setAttribute("user.profiling", "ADVANCED");
        context.setAttribute("recommendation.algorithm", "ML_POWERED");
    }

    private void handleQualityValidation(BusinessContext context, BusinessEvent event) {
        System.out.println("TENANT2: Computer vision and automated inspection");
        context.setAttribute("computer.vision", "ENABLED");
        context.setAttribute("automated.inspection", "AI_POWERED");
    }

    private void handleDefaultValidation(BusinessContext context, BusinessEvent event) {
        System.out.println("TENANT2: Standard intelligent processing");
        context.setAttribute("intelligent.processing", "STANDARD");
    }

    private void handleAfterProcess(BusinessContext context, BusinessEvent event) {
        System.out.println("TENANT2: Value-added services processing");
        provideValueAddedServices(context);
        integrateWithExternalSystems(context);
        generateBusinessInsights(context);
    }

    private void provideValueAddedServices(BusinessContext context) {
        String scenario = context.getScenario();
        System.out.println("TENANT2: Value-added services for " + scenario);
        
        switch (scenario) {
            case "order":
                System.out.println("TENANT2: Smart recommendation engine");
                context.setAttribute("recommendation.engine", "ENABLED");
                break;
            case "medical":
                System.out.println("TENANT2: Health trend analysis");
                context.setAttribute("health.analytics", "ENABLED");
                break;
            case "analytics":
                System.out.println("TENANT2: Advanced data mining");
                context.setAttribute("advanced.analytics", "ENABLED");
                break;
            case "warehouse":
                System.out.println("TENANT2: Smart scheduling system");
                context.setAttribute("smart.scheduling", "ENABLED");
                break;
            case "customer-service":
                System.out.println("TENANT2: Satisfaction prediction");
                context.setAttribute("satisfaction.prediction", "ENABLED");
                break;
            case "marketing":
                System.out.println("TENANT2: Real-time personalization");
                context.setAttribute("real.time.personalization", "ENABLED");
                break;
            case "quality":
                System.out.println("TENANT2: Predictive maintenance");
                context.setAttribute("predictive.maintenance", "ENABLED");
                break;
            default:
                System.out.println("TENANT2: Standard value-added services");
                context.setAttribute("standard.value.added", "ENABLED");
                break;
        }
    }

    private void integrateWithExternalSystems(BusinessContext context) {
        System.out.println("TENANT2: External integration with ERP, CRM, and data warehouse");
        context.setAttribute("external.integration", "COMPLETED");
    }

    private void generateBusinessInsights(BusinessContext context) {
        System.out.println("TENANT2: Real-time reporting and trend prediction");
        context.setAttribute("business.insights", "GENERATED");
    }
}