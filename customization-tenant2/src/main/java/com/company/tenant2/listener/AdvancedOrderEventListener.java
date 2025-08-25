package com.company.tenant2.listener;

import com.company.core.event.AfterSaveEvent;
import com.company.core.event.BeforeNotifyEvent;
import com.company.core.event.BeforeSaveEvent;
import com.company.core.event.BusinessEvent;
import com.company.core.model.BusinessContext;
import com.company.core.model.Order;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 租户2事件监听器 - 事件驱动扩展模式示例
 * 
 * 设计展示：
 * 1. 同时处理传统Order事件和新的BusinessEvent
 * 2. 松耦合的功能扩展，不影响核心流程
 * 3. 展示事件驱动模式的灵活性
 * 
 * 适用场景：专业版租户的灵活业务扩展
 */
@Component
public class AdvancedOrderEventListener {

    private static final BigDecimal VALIDATION_THRESHOLD = new BigDecimal("5000");

    // ========== 新的BusinessEvent处理 ==========

    @EventListener
    @Order(1)
    public void handleBusinessEvent(BusinessEvent event) {
        if (!event.isForPhase("BEFORE_PROCESS") && !event.isForPhase("AFTER_PROCESS")) {
            return;
        }

        BusinessContext context = event.getContext();
        String phase = event.getPhase();
        
        System.out.printf("  📡 [租户2-事件扩展] %s处理: %s.%s%n", 
                        phase, context.getScenario(), context.getBusinessType());

        if ("BEFORE_PROCESS".equals(phase)) {
            handleBeforeProcess(context, event);
        } else if ("AFTER_PROCESS".equals(phase)) {
            handleAfterProcess(context, event);
        }
    }

    private void handleBeforeProcess(BusinessContext context, BusinessEvent event) {
        String scenario = context.getScenario();
        
        if ("order".equals(scenario)) {
            handleOrderValidation(context, event);
        } else if ("medical".equals(scenario)) {
            handleMedicalValidation(context, event);
        }
    }

    private void handleOrderValidation(BusinessContext context, BusinessEvent event) {
        System.out.printf("    🔍 [专业订单] 智能风控 + 信用检查%n");
        
        // 智能风控
        Object amountObj = context.getData().get("amount");
        if (amountObj instanceof Number) {
            BigDecimal amount = new BigDecimal(amountObj.toString());
            if (amount.compareTo(VALIDATION_THRESHOLD) > 0) {
                System.out.printf("      🛡️ 风控检测: ¥%s 触发高级验证%n", amount);
                context.setAttribute("fraud.check", "ADVANCED");
                context.setAttribute("credit.score.required", "true");
            }
        }

        // 快速处理判断
        Object itemCount = context.getData().get("itemCount");
        if (itemCount != null && (Integer) itemCount <= 3) {
            System.out.printf("      ⚡ 简单订单: 启用快速处理通道%n");
            context.setAttribute("fast.track", "enabled");
            // 可以选择跳过部分默认流程
        }
    }

    private void handleMedicalValidation(BusinessContext context, BusinessEvent event) {
        System.out.printf("    🩺 [专业医疗] 智能诊断辅助%n");
        
        context.setAttribute("ai.diagnosis.support", "ENABLED");
        context.setAttribute("drug.interaction.check", "ADVANCED");
        System.out.printf("      🤖 AI辅助诊断已启用%n");
        System.out.printf("      💊 高级药物相互作用检查已启用%n");
    }

    private void handleAfterProcess(BusinessContext context, BusinessEvent event) {
        String scenario = context.getScenario();
        
        System.out.printf("    🔄 [租户2后置] 增值服务处理%n");
        
        // 增值服务
        provideValueAddedServices(context);
        
        // 数据集成
        integrateWithExternalSystems(context);
        
        // 业务分析
        generateBusinessInsights(context);
    }

    private void provideValueAddedServices(BusinessContext context) {
        String scenario = context.getScenario();
        System.out.printf("      🎁 增值服务: %s专属功能%n", scenario);
        
        if ("order".equals(scenario)) {
            System.out.printf("        → 智能推荐引擎%n");
            System.out.printf("        → 库存优化建议%n");
            context.setAttribute("recommendation.engine", "ENABLED");
        } else if ("medical".equals(scenario)) {
            System.out.printf("        → 健康趋势分析%n");
            System.out.printf("        → 用药依从性跟踪%n");
            context.setAttribute("health.analytics", "ENABLED");
        }
    }

    private void integrateWithExternalSystems(BusinessContext context) {
        System.out.printf("      🔗 外部集成: ERP + CRM + 数据仓库%n");
        context.setAttribute("external.integration", "COMPLETED");
    }

    private void generateBusinessInsights(BusinessContext context) {
        System.out.printf("      📊 业务洞察: 实时报表 + 趋势预测%n");
        context.setAttribute("business.insights", "GENERATED");
    }

    // ========== 保持原有Order事件处理兼容性 ==========

    @EventListener
    @Order(1)
    public void handleBeforeSave(BeforeSaveEvent event) {
        Order order = event.getOrder();
        System.out.printf("    🔍 [传统订单] 租户2风控验证%n");
        
        if (requiresAdvancedValidation(order)) {
            performFraudDetection(order);
            validateCreditScore(order);
        }
    }

    @EventListener
    public void handleAfterSave(AfterSaveEvent event) {
        Order order = event.getOrder();
        System.out.printf("    🔄 [传统订单] 租户2后置集成%n");
        
        integrateWithERP(order);
        generateAnalyticsReport(order);
    }

    @EventListener
    public void handleBeforeNotify(BeforeNotifyEvent event) {
        System.out.printf("    📬 [传统订单] 租户2多渠道通知%n");
        
        sendMultiChannelNotification(event.getOrder());
        event.skipDefaultAction();  // 替换默认通知
    }

    // ========== 传统Order处理方法 ==========

    private boolean requiresAdvancedValidation(Order order) {
        return order.getAmount() != null && order.getAmount().compareTo(VALIDATION_THRESHOLD) > 0;
    }

    private void performFraudDetection(Order order) {
        System.out.printf("      🛡️ 欺诈检测: 订单 %s%n", order.getId());
    }

    private void validateCreditScore(Order order) {
        System.out.printf("      💳 信用评分: 用户 %s%n", order.getUserId());
    }

    private void integrateWithERP(Order order) {
        System.out.printf("      🔗 ERP集成: 订单 %s 已同步%n", order.getId());
    }

    private void generateAnalyticsReport(Order order) {
        System.out.printf("      📊 分析报告: 订单 %s 数据已更新%n", order.getId());
    }

    private void sendMultiChannelNotification(Order order) {
        System.out.printf("      📬 多渠道通知: 邮件+短信+推送 for 订单 %s%n", order.getId());
    }
}