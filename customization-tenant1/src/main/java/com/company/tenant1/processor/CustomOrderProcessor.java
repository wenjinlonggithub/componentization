package com.company.tenant1.processor;

import com.company.core.model.BusinessContext;
import com.company.core.model.Order;
import com.company.core.processor.UniversalProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 租户1定制处理器 - 继承扩展模式示例
 * 
 * 设计展示：
 * 1. 继承UniversalProcessor，覆盖钩子方法
 * 2. 支持传统Order处理 + 新的BusinessContext处理
 * 3. 展示深度定制场景的实现方式
 * 
 * 适用场景：企业级租户的复杂业务定制
 */
@Component
public class CustomOrderProcessor extends UniversalProcessor {

    private static final BigDecimal AUDIT_THRESHOLD = new BigDecimal("10000");

    @Override
    protected void beforeProcess(BusinessContext context) {
        System.out.printf("  🔗 [租户1-继承扩展] 企业级定制处理: %s%n", context.getBusinessType());
        
        // 根据业务场景执行不同的定制逻辑
        String scenario = context.getScenario();
        switch (scenario) {
            case "order" -> enhancedOrderProcessing(context);
            case "medical" -> enterpriseMedicalProcessing(context);
            case "finance" -> enterpriseFinanceProcessing(context);
            case "procurement" -> enterpriseProcurementProcessing(context);
            case "hr" -> enterpriseHRProcessing(context);
            default -> defaultEnterpriseProcessing(context);
        }
    }

    @Override
    protected void afterProcess(BusinessContext context) {
        System.out.printf("  🔗 [租户1-继承扩展] 企业级后置处理%n");
        
        // 企业级审计和通知
        performEnterpriseAudit(context);
        sendEnterpriseNotification(context);
    }

    // ========== 租户1专属业务逻辑 ==========

    private void enhancedOrderProcessing(BusinessContext context) {
        System.out.printf("    📦 [企业订单] 高级验证 + 风险控制%n");
        
        // 检查订单金额
        Object amountObj = context.getData().get("amount");
        if (amountObj instanceof BigDecimal) {
            BigDecimal amount = (BigDecimal) amountObj;
            if (amount.compareTo(AUDIT_THRESHOLD) > 0) {
                context.setAttribute("requires.audit", "true");
                context.setAttribute("audit.level", "HIGH");
                System.out.printf("      💰 高额订单检测: ¥%s 需要审批%n", amount);
            }
        }
        
        // 企业级库存控制
        context.setAttribute("inventory.strategy", "ENTERPRISE");
        System.out.printf("      📊 企业库存策略已启用%n");
    }

    private void enterpriseMedicalProcessing(BusinessContext context) {
        System.out.printf("    🏥 [企业医疗] 多级认证 + 合规检查%n");
        
        context.setAttribute("medical.compliance", "ENTERPRISE_LEVEL");
        context.setAttribute("approval.required", "MULTI_LEVEL");
        
        System.out.printf("      👨‍⚕️ 医师资质：主治医师 → 科室主任 → 医务科%n");
        System.out.printf("      📋 合规检查：企业级医疗法规验证%n");
    }

    private void performEnterpriseAudit(BusinessContext context) {
        System.out.printf("    📋 [企业审计] 完整操作轨迹记录%n");
        context.setAttribute("audit.timestamp", String.valueOf(System.currentTimeMillis()));
        context.setAttribute("audit.operator", context.getOperatorId());
        context.setAttribute("audit.level", "ENTERPRISE");
    }

    private void enterpriseFinanceProcessing(BusinessContext context) {
        System.out.printf("    💰 [企业财务] 多级财务审批 + 合规检查%n");
        
        Object amountObj = context.getData().get("amount");
        if (amountObj instanceof Number) {
            double amount = ((Number) amountObj).doubleValue();
            if (amount > 50000) {
                context.setAttribute("finance.approval.level", "CFO");
                System.out.printf("      📊 大额支出 ¥%.2f 需要CFO审批%n", amount);
            } else if (amount > 10000) {
                context.setAttribute("finance.approval.level", "MANAGER");
                System.out.printf("      📊 中额支出 ¥%.2f 需要部门经理审批%n", amount);
            }
        }
        
        context.setAttribute("finance.compliance", "ENTERPRISE_LEVEL");
        System.out.printf("      📋 财务合规：企业级财务制度验证%n");
    }

    private void enterpriseProcurementProcessing(BusinessContext context) {
        System.out.printf("    🏭 [企业采购] 供应商资质审核 + 多级采购审批%n");
        
        String vendorId = (String) context.getData().get("vendorId");
        if (vendorId != null) {
            context.setAttribute("vendor.verification", "REQUIRED");
            context.setAttribute("vendor.level", "CERTIFIED_ENTERPRISE");
            System.out.printf("      🏢 供应商 %s 企业资质验证中...%n", vendorId);
        }
        
        Object costObj = context.getData().get("totalCost");
        if (costObj instanceof Number) {
            double cost = ((Number) costObj).doubleValue();
            if (cost > 100000) {
                context.setAttribute("procurement.approval", "BOARD_LEVEL");
                System.out.printf("      💼 大额采购 ¥%.2f 需要董事会审批%n", cost);
            }
        }
    }

    private void enterpriseHRProcessing(BusinessContext context) {
        System.out.printf("    👥 [企业HR] 多级人事审批 + 背景调查%n");
        
        String position = (String) context.getData().get("position");
        if ("MANAGER".equals(position) || "SENIOR".equals(position)) {
            context.setAttribute("background.check", "ENHANCED");
            context.setAttribute("approval.level", "VP_HR");
            System.out.printf("      🔍 高级职位 %s 需要增强背景调查%n", position);
        }
        
        context.setAttribute("onboard.process", "ENTERPRISE_STANDARD");
        System.out.printf("      📋 入职流程：企业标准化入职程序%n");
    }

    private void defaultEnterpriseProcessing(BusinessContext context) {
        System.out.printf("    🏢 [企业通用] 标准企业级处理流程%n");
        context.setAttribute("enterprise.standard", "APPLIED");
        context.setAttribute("compliance.level", "ENTERPRISE");
    }

    private void sendEnterpriseNotification(BusinessContext context) {
        System.out.printf("    📬 [企业通知] 多渠道 + 实时推送%n");
        System.out.printf("      → 邮件通知: 业务管理员%n");
        System.out.printf("      → 短信通知: 相关负责人%n");
        System.out.printf("      → 系统推送: 企业仪表板%n");
        
        context.setAttribute("notification.channels", "EMAIL,SMS,PUSH");
        context.setAttribute("notification.level", "ENTERPRISE");
    }

    // ========== 保持原有Order处理兼容性 ==========
    
    @Override
    protected void beforeSave(Order order) {
        if (order.getAmount() != null && order.getAmount().compareTo(AUDIT_THRESHOLD) > 0) {
            order.setStatus("PENDING_APPROVAL");
            System.out.printf("    💰 [传统订单] 高额订单 ¥%s 需要审批%n", order.getAmount());
        }
    }

    @Override
    protected void beforeNotify(Order order) {
        System.out.printf("    📱 [传统订单] 租户1 SMS通知发送%n");
        if (order.getUser() != null && order.getUser().getPhone() != null) {
            // SMS通知逻辑
        }
    }
}