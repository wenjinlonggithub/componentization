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
        
        switch (scenario) {
            case "order" -> handleOrderValidation(context, event);
            case "medical" -> handleMedicalValidation(context, event);
            case "analytics" -> handleAnalyticsValidation(context, event);
            case "warehouse" -> handleWarehouseValidation(context, event);
            case "customer-service" -> handleCustomerServiceValidation(context, event);
            case "marketing" -> handleMarketingValidation(context, event);
            case "quality" -> handleQualityValidation(context, event);
            default -> handleDefaultValidation(context, event);
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

    // 新增业务场景的验证处理
    private void handleAnalyticsValidation(BusinessContext context, BusinessEvent event) {
        System.out.printf("    📊 [智能分析] 数据质量检查 + 模型验证%n");
        context.setAttribute("data.quality.check", "ENABLED");
        context.setAttribute("ml.model.validation", "ADVANCED");
        System.out.printf("      🎆 数据质量分数: 95%%+%n");
    }

    private void handleWarehouseValidation(BusinessContext context, BusinessEvent event) {
        System.out.printf("    🏢 [智能仓储] 库存优化 + 供应链分析%n");
        context.setAttribute("inventory.optimization", "ENABLED");
        context.setAttribute("supply.chain.analysis", "REAL_TIME");
        System.out.printf("      📦 智能库存调度已启用%n");
    }

    private void handleCustomerServiceValidation(BusinessContext context, BusinessEvent event) {
        System.out.printf("    🤖 [AI客服] 情感分析 + 智能路由%n");
        context.setAttribute("sentiment.analysis", "ENABLED");
        context.setAttribute("intelligent.routing", "AI_POWERED");
        System.out.printf("      💬 客户情感分析: 实时监测%n");
    }

    private void handleMarketingValidation(BusinessContext context, BusinessEvent event) {
        System.out.printf("    🎯 [精准营销] 用户画像 + 推荐算法%n");
        context.setAttribute("user.profiling", "ADVANCED");
        context.setAttribute("recommendation.algorithm", "ML_POWERED");
        System.out.printf("      🔍 用户精准匹配: 98%%+ 准确率%n");
    }

    private void handleQualityValidation(BusinessContext context, BusinessEvent event) {
        System.out.printf("    🔍 [智能质检] 机器视觉 + 自动化检测%n");
        context.setAttribute("computer.vision", "ENABLED");
        context.setAttribute("automated.inspection", "AI_POWERED");
        System.out.printf("      🔭 缺陷检测精度: 99.5%%+%n");
    }

    private void handleDefaultValidation(BusinessContext context, BusinessEvent event) {
        System.out.printf("    🎆 [智能通用] 标准智能化流程%n");
        context.setAttribute("intelligent.processing", "STANDARD");
    }

    private void provideValueAddedServices(BusinessContext context) {
        String scenario = context.getScenario();
        System.out.printf("      🎁 增值服务: %s专属功能%n", scenario);
        
        switch (scenario) {
            case "order" -> {
                System.out.printf("        → 智能推荐引擎%n");
                System.out.printf("        → 库存优化建议%n");
                context.setAttribute("recommendation.engine", "ENABLED");
            }
            case "medical" -> {
                System.out.printf("        → 健康趋势分析%n");
                System.out.printf("        → 用药依从性跟踪%n");
                context.setAttribute("health.analytics", "ENABLED");
            }
            case "analytics" -> {
                System.out.printf("        → 高级数据挖掘%n");
                System.out.printf("        → 预测模型优化%n");
                context.setAttribute("advanced.analytics", "ENABLED");
            }
            case "warehouse" -> {
                System.out.printf("        → 智能调度系统%n");
                System.out.printf("        → 供应链可视化%n");
                context.setAttribute("smart.scheduling", "ENABLED");
            }
            case "customer-service" -> {
                System.out.printf("        → 用户满意度预测%n");
                System.out.printf("        → 智能知识库%n");
                context.setAttribute("satisfaction.prediction", "ENABLED");
            }
            case "marketing" -> {
                System.out.printf("        → 实时个性化%n");
                System.out.printf("        → 多渠道协同%n");
                context.setAttribute("real.time.personalization", "ENABLED");
            }
            case "quality" -> {
                System.out.printf("        → 质量趋势分析%n");
                System.out.printf("        → 预防性维护%n");
                context.setAttribute("predictive.maintenance", "ENABLED");
            }
            default -> {
                System.out.printf("        → 标准增值服务%n");
                context.setAttribute("standard.value.added", "ENABLED");
            }
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