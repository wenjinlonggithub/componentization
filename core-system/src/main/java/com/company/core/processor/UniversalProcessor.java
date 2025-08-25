package com.company.core.processor;

import com.company.core.event.BusinessEvent;
import com.company.core.model.BusinessContext;
import com.company.core.model.Order;
import com.company.core.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 通用业务处理器 - 支持订单、医疗等多种业务场景
 * 
 * 核心设计：
 * 1. 保持原有OrderProcessor的简洁性
 * 2. 增加BusinessContext支持多业务场景
 * 3. 统一的扩展机制（钩子方法 + 事件驱动）
 */
@Component
public class UniversalProcessor implements OrderProcessor {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private NotificationService notificationService;

    @Override
    public void process(Order order) {
        // 兼容原有Order处理逻辑
        BusinessContext context = createContextFromOrder(order);
        processBusinessContext(context);
        updateOrderFromContext(order, context);
    }

    /**
     * 新增：通用业务上下文处理
     */
    public ProcessResult processBusiness(BusinessContext context) {
        try {
            System.out.printf("🚀 [通用处理器] 开始处理 %s 业务%n", context);
            
            // 业务验证
            if (!validateBusiness(context)) {
                return ProcessResult.failure("业务验证失败");
            }

            // 扩展点执行
            executeExtensions(context);

            // 核心业务逻辑
            processCoreLogic(context);

            context.setStatus("COMPLETED");
            System.out.printf("✅ [通用处理器] %s 处理完成%n", context.getBusinessId());
            
            return ProcessResult.success("业务处理成功", context);

        } catch (Exception e) {
            System.err.printf("❌ [通用处理器] 处理异常: %s%n", e.getMessage());
            return ProcessResult.failure("系统异常：" + e.getMessage());
        }
    }

    // ========== 扩展机制 ==========
    
    private void executeExtensions(BusinessContext context) {
        // 钩子方法扩展
        beforeProcess(context);
        
        // 事件驱动扩展
        BusinessEvent beforeEvent = publishEvent(new BusinessEvent(this, context, "BEFORE_PROCESS"));
        
        // 后置处理
        if (!beforeEvent.isSkipDefaultAction()) {
            afterProcess(context);
        }
        
        publishEvent(new BusinessEvent(this, context, "AFTER_PROCESS"));
    }

    /**
     * 钩子方法 - 子类可覆盖
     */
    protected void beforeProcess(BusinessContext context) {
        System.out.printf("  🔗 [钩子扩展] beforeProcess: %s%n", context.getBusinessType());
    }

    protected void afterProcess(BusinessContext context) {
        System.out.printf("  🔗 [钩子扩展] afterProcess: %s%n", context.getBusinessType());
    }

    // ========== 业务逻辑 ==========
    
    private boolean validateBusiness(BusinessContext context) {
        if (context.getScenario() == null || context.getBusinessType() == null) {
            return false;
        }
        System.out.printf("  ✓ 业务验证通过: %s.%s%n", context.getScenario(), context.getBusinessType());
        return true;
    }

    private void processCoreLogic(BusinessContext context) {
        String scenario = context.getScenario();
        System.out.printf("  🔄 执行核心逻辑: %s-%s%n", scenario, context.getBusinessType());
        
        if ("order".equals(scenario)) {
            processOrderLogic(context);
        } else if ("medical".equals(scenario)) {
            processMedicalLogic(context);
        } else {
            processGenericLogic(context);
        }
    }

    private void processOrderLogic(BusinessContext context) {
        System.out.printf("    📦 订单业务处理%n");
        context.setAttribute("processed.by", "ORDER_LOGIC");
    }

    private void processMedicalLogic(BusinessContext context) {
        System.out.printf("    🏥 医疗业务处理%n");
        context.setAttribute("processed.by", "MEDICAL_LOGIC");
    }

    private void processGenericLogic(BusinessContext context) {
        System.out.printf("    ⚙️ 通用业务处理%n");
        context.setAttribute("processed.by", "GENERIC_LOGIC");
    }

    // ========== 原有Order兼容方法 ==========
    
    private BusinessContext createContextFromOrder(Order order) {
        BusinessContext context = new BusinessContext("order", "ORDER_PROCESS");
        context.setBusinessId(order.getId());
        context.setTenantId("default"); // 可以从order中获取
        context.putData("order", order);
        return context;
    }

    private void processBusinessContext(BusinessContext context) {
        processBusiness(context);
    }

    private void updateOrderFromContext(Order order, BusinessContext context) {
        // 将context的处理结果更新回order
        order.setStatus(context.getStatus());
    }

    // ========== 工具方法 ==========
    
    private <T> T publishEvent(T event) {
        System.out.printf("  📡 [事件发布] %s%n", event.getClass().getSimpleName());
        eventPublisher.publishEvent(event);
        return event;
    }

    /**
     * 简化的处理结果类
     */
    public static class ProcessResult {
        private final boolean success;
        private final String message;
        private final Object data;

        private ProcessResult(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public static ProcessResult success(String message, Object data) {
            return new ProcessResult(true, message, data);
        }

        public static ProcessResult failure(String message) {
            return new ProcessResult(false, message, null);
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getData() { return data; }
    }
}