package com.company.tenant1.processor;

import com.company.core.model.BusinessContext;
import com.company.core.model.Order;
import com.company.core.processor.UniversalProcessor;
// NOTE: Spring imports commented out for standalone compilation
// import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
// import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Tenant1 Custom Processor - Inheritance Extension Pattern
 * NOTE: Spring annotations commented out for standalone compilation
 */
// @Component
// @ConditionalOnProperty(name = "tenant.id", havingValue = "tenant1")
public class CustomOrderProcessor extends UniversalProcessor {

    private static final BigDecimal AUDIT_THRESHOLD = new BigDecimal("10000");

    @Override
    protected void beforeProcess(BusinessContext context) {
        System.out.println("TENANT1: Custom processing for " + context.getBusinessType());
        
        String scenario = context.getScenario();
        switch (scenario) {
            case "order":
                enhancedOrderProcessing(context);
                break;
            case "medical":
                enterpriseMedicalProcessing(context);
                break;
            case "finance":
                enterpriseFinanceProcessing(context);
                break;
            case "procurement":
                enterpriseProcurementProcessing(context);
                break;
            case "hr":
                enterpriseHRProcessing(context);
                break;
            default:
                defaultEnterpriseProcessing(context);
                break;
        }
    }

    @Override
    protected void afterProcess(BusinessContext context) {
        System.out.println("TENANT1: Enterprise audit and notification");
        performEnterpriseAudit(context);
        sendEnterpriseNotification(context);
    }

    private void enhancedOrderProcessing(BusinessContext context) {
        System.out.println("TENANT1: Enhanced order processing with approval workflow");
        Object amountObj = context.getData().get("amount");
        if (amountObj instanceof Number) {
            double amount = ((Number) amountObj).doubleValue();
            if (amount > AUDIT_THRESHOLD.doubleValue()) {
                context.setAttribute("requires.audit", "true");
                System.out.println("TENANT1: High-value order requires approval: " + amount);
            }
        }
        context.setAttribute("inventory.strategy", "ENTERPRISE");
    }

    private void enterpriseMedicalProcessing(BusinessContext context) {
        System.out.println("TENANT1: Multi-level medical certification");
        context.setAttribute("medical.compliance", "ENTERPRISE_LEVEL");
        context.setAttribute("approval.required", "MULTI_LEVEL");
    }

    private void enterpriseFinanceProcessing(BusinessContext context) {
        System.out.println("TENANT1: Multi-level finance approval");
        Object amountObj = context.getData().get("amount");
        if (amountObj instanceof Number) {
            double amount = ((Number) amountObj).doubleValue();
            if (amount > 50000) {
                context.setAttribute("finance.approval.level", "CFO");
            } else if (amount > 10000) {
                context.setAttribute("finance.approval.level", "MANAGER");
            }
        }
    }

    private void enterpriseProcurementProcessing(BusinessContext context) {
        System.out.println("TENANT1: Vendor qualification and procurement approval");
        context.setAttribute("vendor.verification", "REQUIRED");
    }

    private void enterpriseHRProcessing(BusinessContext context) {
        System.out.println("TENANT1: Multi-level HR approval with background check");
        String position = (String) context.getData().get("position");
        if ("MANAGER".equals(position) || "SENIOR".equals(position)) {
            context.setAttribute("background.check", "ENHANCED");
        }
    }

    private void defaultEnterpriseProcessing(BusinessContext context) {
        System.out.println("TENANT1: Standard enterprise processing");
        context.setAttribute("enterprise.standard", "APPLIED");
    }

    private void performEnterpriseAudit(BusinessContext context) {
        System.out.println("TENANT1: Enterprise audit trail recording");
        context.setAttribute("audit.timestamp", String.valueOf(System.currentTimeMillis()));
    }

    private void sendEnterpriseNotification(BusinessContext context) {
        System.out.println("TENANT1: Multi-channel enterprise notification");
        context.setAttribute("notification.channels", "EMAIL,SMS,PUSH");
    }

    // Traditional Order processing compatibility
    @Override
    public void beforeSave(Order order) {
        if (order.getAmount() != null && order.getAmount().compareTo(AUDIT_THRESHOLD) > 0) {
            order.setStatus("PENDING_APPROVAL");
            System.out.println("TENANT1: High-value traditional order requires approval");
        }
    }

    @Override
    public void beforeNotify(Order order) {
        System.out.println("TENANT1: SMS notification for traditional order");
    }
}