package com.company.tenant1.app.controller;

import com.company.core.model.BusinessContext;
import com.company.core.processor.UniversalProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 租户1通用业务控制器 - 支持多业务场景
 * 
 * 展示特性：
 * 1. 继承扩展模式的实际效果
 * 2. 支持订单、医疗等多种业务场景
 * 3. 一个应用展示企业级定制功能
 */
@RestController
@RequestMapping("/api/business")
public class UniversalController {

    @Autowired
    private UniversalProcessor universalProcessor;

    /**
     * 通用业务处理接口
     */
    @PostMapping("/process")
    public UniversalProcessor.ProcessResult processBusiness(@RequestBody Map<String, Object> request) {
        BusinessContext context = buildBusinessContext(request);
        
        System.out.println("\n" + "=".repeat(80));
        System.out.printf("🎯 [租户1-企业版] 开始处理 %s 业务%n", context);
        System.out.println("=".repeat(80));
        
        UniversalProcessor.ProcessResult result = universalProcessor.processBusiness(context);
        
        System.out.println("=".repeat(80));
        System.out.printf("🏁 [租户1-企业版] 处理完成: %s%n", result.isSuccess() ? "成功" : "失败");
        System.out.println("=".repeat(80) + "\n");
        
        return result;
    }

    /**
     * 订单业务场景
     */
    @PostMapping("/order")
    public UniversalProcessor.ProcessResult processOrder(@RequestBody Map<String, Object> orderData) {
        Map<String, Object> request = new HashMap<>();
        request.put("scenario", "order");
        request.put("businessType", "ORDER_PROCESS");
        request.put("tenantId", "tenant1");
        request.put("operatorId", orderData.getOrDefault("operatorId", "user001"));
        request.put("data", orderData);
        
        return processBusiness(request);
    }

    /**
     * 医疗业务场景
     */
    @PostMapping("/medical")
    public UniversalProcessor.ProcessResult processMedical(@RequestBody Map<String, Object> medicalData) {
        Map<String, Object> request = new HashMap<>();
        request.put("scenario", "medical");
        request.put("businessType", "PRESCRIPTION");
        request.put("tenantId", "tenant1");
        request.put("operatorId", medicalData.getOrDefault("doctorId", "doctor001"));
        request.put("data", medicalData);
        
        return processBusiness(request);
    }

    /**
     * 财务业务场景 - 企业级财务审计
     */
    @PostMapping("/finance")
    public UniversalProcessor.ProcessResult processFinance(@RequestBody Map<String, Object> financeData) {
        Map<String, Object> request = new HashMap<>();
        request.put("scenario", "finance");
        request.put("businessType", "EXPENSE_AUDIT");
        request.put("tenantId", "tenant1");
        request.put("operatorId", financeData.getOrDefault("auditorId", "auditor001"));
        request.put("data", financeData);
        
        return processBusiness(request);
    }

    /**
     * 采购业务场景 - 企业级供应商管理
     */
    @PostMapping("/procurement")
    public UniversalProcessor.ProcessResult processProcurement(@RequestBody Map<String, Object> procurementData) {
        Map<String, Object> request = new HashMap<>();
        request.put("scenario", "procurement");
        request.put("businessType", "VENDOR_APPROVAL");
        request.put("tenantId", "tenant1");
        request.put("operatorId", procurementData.getOrDefault("buyerId", "buyer001"));
        request.put("data", procurementData);
        
        return processBusiness(request);
    }

    /**
     * HR业务场景 - 企业级人力资源管理
     */
    @PostMapping("/hr")
    public UniversalProcessor.ProcessResult processHR(@RequestBody Map<String, Object> hrData) {
        Map<String, Object> request = new HashMap<>();
        request.put("scenario", "hr");
        request.put("businessType", "EMPLOYEE_ONBOARD");
        request.put("tenantId", "tenant1");
        request.put("operatorId", hrData.getOrDefault("hrManagerId", "hr001"));
        request.put("data", hrData);
        
        return processBusiness(request);
    }

    /**
     * 系统信息
     */
    @GetMapping("/info")
    public Map<String, Object> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("tenant", "tenant1");
        info.put("name", "租户1企业版");
        info.put("type", "ENTERPRISE");
        info.put("extensionMode", "继承扩展模式");
        info.put("description", "展示深度定制的企业级功能");
        
        Map<String, String> scenarios = new HashMap<>();
        scenarios.put("order", "企业订单处理 - 高额订单审批流程");
        scenarios.put("medical", "企业医疗管理 - 多级认证审批");
        scenarios.put("finance", "财务管理 - 企业级支出审计");
        scenarios.put("procurement", "采购管理 - 供应商资质审核");
        scenarios.put("hr", "人力资源 - 员工入职审批");
        info.put("supportedScenarios", scenarios);
        
        info.put("enterpriseFeatures", Map.of(
            "highValueAudit", "高额订单审批",
            "enterpriseValidation", "企业级验证",
            "multiChannelNotification", "多渠道通知",
            "advancedAudit", "高级审计日志"
        ));

        return info;
    }

    private BusinessContext buildBusinessContext(Map<String, Object> request) {
        String scenario = (String) request.get("scenario");
        String businessType = (String) request.get("businessType");
        
        BusinessContext context = new BusinessContext(scenario, businessType);
        context.setBusinessId(generateBusinessId(scenario));
        context.setTenantId((String) request.getOrDefault("tenantId", "tenant1"));
        context.setOperatorId((String) request.getOrDefault("operatorId", "system"));
        
        Map<String, Object> data = (Map<String, Object>) request.get("data");
        if (data != null) {
            context.setData(data);
        }
        
        return context;
    }

    private String generateBusinessId(String scenario) {
        String prefix = switch (scenario.toLowerCase()) {
            case "order" -> "T1-ORD";
            case "medical" -> "T1-MED";
            case "finance" -> "T1-FIN";
            case "procurement" -> "T1-PRO";
            case "hr" -> "T1-HRM";
            default -> "T1-BIZ";
        };
        
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}