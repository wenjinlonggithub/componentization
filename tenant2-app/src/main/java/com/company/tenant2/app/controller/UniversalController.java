package com.company.tenant2.app.controller;

import com.company.core.model.BusinessContext;
import com.company.core.processor.UniversalProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 租户2通用业务控制器 - 支持多业务场景
 * 
 * 展示特性：
 * 1. 事件驱动扩展模式的实际效果
 * 2. 松耦合的功能扩展
 * 3. 专业版智能增值服务
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
        System.out.printf("🎯 [租户2-专业版] 开始处理 %s 业务%n", context);
        System.out.println("=".repeat(80));
        
        UniversalProcessor.ProcessResult result = universalProcessor.processBusiness(context);
        
        System.out.println("=".repeat(80));
        System.out.printf("🏁 [租户2-专业版] 处理完成: %s%n", result.isSuccess() ? "成功" : "失败");
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
        request.put("tenantId", "tenant2");
        request.put("operatorId", orderData.getOrDefault("operatorId", "user002"));
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
        request.put("tenantId", "tenant2");
        request.put("operatorId", medicalData.getOrDefault("doctorId", "doctor002"));
        request.put("data", medicalData);
        
        return processBusiness(request);
    }

    /**
     * 智能分析场景（租户2专属）
     */
    @PostMapping("/analytics")
    public UniversalProcessor.ProcessResult processAnalytics(@RequestBody Map<String, Object> analyticsData) {
        Map<String, Object> request = new HashMap<>();
        request.put("scenario", "analytics");
        request.put("businessType", "INTELLIGENT_ANALYSIS");
        request.put("tenantId", "tenant2");
        request.put("operatorId", analyticsData.getOrDefault("analystId", "analyst002"));
        request.put("data", analyticsData);
        
        return processBusiness(request);
    }

    /**
     * 仓储物流场景 - 智能库存优化
     */
    @PostMapping("/warehouse")
    public UniversalProcessor.ProcessResult processWarehouse(@RequestBody Map<String, Object> warehouseData) {
        Map<String, Object> request = new HashMap<>();
        request.put("scenario", "warehouse");
        request.put("businessType", "INVENTORY_OPTIMIZE");
        request.put("tenantId", "tenant2");
        request.put("operatorId", warehouseData.getOrDefault("warehouseManagerId", "warehouse002"));
        request.put("data", warehouseData);
        
        return processBusiness(request);
    }

    /**
     * 客户服务场景 - AI智能客服
     */
    @PostMapping("/customer-service")
    public UniversalProcessor.ProcessResult processCustomerService(@RequestBody Map<String, Object> serviceData) {
        Map<String, Object> request = new HashMap<>();
        request.put("scenario", "customer-service");
        request.put("businessType", "AI_SUPPORT");
        request.put("tenantId", "tenant2");
        request.put("operatorId", serviceData.getOrDefault("agentId", "agent002"));
        request.put("data", serviceData);
        
        return processBusiness(request);
    }

    /**
     * 营销活动场景 - 精准营销推荐
     */
    @PostMapping("/marketing")
    public UniversalProcessor.ProcessResult processMarketing(@RequestBody Map<String, Object> marketingData) {
        Map<String, Object> request = new HashMap<>();
        request.put("scenario", "marketing");
        request.put("businessType", "PRECISION_CAMPAIGN");
        request.put("tenantId", "tenant2");
        request.put("operatorId", marketingData.getOrDefault("marketerId", "marketer002"));
        request.put("data", marketingData);
        
        return processBusiness(request);
    }

    /**
     * 质量管理场景 - 智能质检
     */
    @PostMapping("/quality")
    public UniversalProcessor.ProcessResult processQuality(@RequestBody Map<String, Object> qualityData) {
        Map<String, Object> request = new HashMap<>();
        request.put("scenario", "quality");
        request.put("businessType", "INTELLIGENT_QC");
        request.put("tenantId", "tenant2");
        request.put("operatorId", qualityData.getOrDefault("qcManagerId", "qc002"));
        request.put("data", qualityData);
        
        return processBusiness(request);
    }

    /**
     * 系统信息
     */
    @GetMapping("/info")
    public Map<String, Object> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("tenant", "tenant2");
        info.put("name", "租户2专业版");
        info.put("type", "PROFESSIONAL");
        info.put("extensionMode", "事件驱动扩展模式");
        info.put("description", "展示灵活的智能增值服务");
        
        Map<String, String> scenarios = new HashMap<>();
        scenarios.put("order", "智能订单处理 - 风控检测+快速通道");
        scenarios.put("medical", "智能医疗管理 - AI诊断辅助");
        scenarios.put("analytics", "智能数据分析 - 业务洞察+趋势预测");
        scenarios.put("warehouse", "仓储物流 - 智能库存优化");
        scenarios.put("customer-service", "客户服务 - AI智能客服");
        scenarios.put("marketing", "营销活动 - 精准推荐引擎");
        scenarios.put("quality", "质量管理 - 智能质检系统");
        info.put("supportedScenarios", scenarios);
        
        info.put("professionalFeatures", Map.of(
            "intelligentValidation", "智能验证",
            "fraudDetection", "风控检测",
            "valueAddedServices", "增值服务",
            "businessInsights", "业务洞察",
            "fastTrackProcessing", "快速通道"
        ));

        return info;
    }

    private BusinessContext buildBusinessContext(Map<String, Object> request) {
        String scenario = (String) request.get("scenario");
        String businessType = (String) request.get("businessType");
        
        BusinessContext context = new BusinessContext(scenario, businessType);
        context.setBusinessId(generateBusinessId(scenario));
        context.setTenantId((String) request.getOrDefault("tenantId", "tenant2"));
        context.setOperatorId((String) request.getOrDefault("operatorId", "system"));
        
        Map<String, Object> data = (Map<String, Object>) request.get("data");
        if (data != null) {
            context.setData(data);
        }
        
        return context;
    }

    private String generateBusinessId(String scenario) {
        String prefix = switch (scenario.toLowerCase()) {
            case "order" -> "T2-ORD";
            case "medical" -> "T2-MED";
            case "analytics" -> "T2-ANA";
            case "warehouse" -> "T2-WHS";
            case "customer-service" -> "T2-CS";
            case "marketing" -> "T2-MKT";
            case "quality" -> "T2-QC";
            default -> "T2-BIZ";
        };
        
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}