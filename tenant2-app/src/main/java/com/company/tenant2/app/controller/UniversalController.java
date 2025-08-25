package com.company.tenant2.app.controller;

import com.company.core.model.BusinessContext;
import com.company.core.processor.UniversalProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Tenant2 Universal Controller - Professional Edition
 */
@RestController
@RequestMapping("/api/business")
public class UniversalController {

    @Autowired
    private UniversalProcessor universalProcessor;

    @PostMapping("/process")
    public UniversalProcessor.ProcessResult processBusiness(@RequestBody Map<String, Object> request) {
        BusinessContext context = buildBusinessContext(request);
        
        System.out.println("TENANT2-APP: Processing " + context);
        
        return universalProcessor.processBusiness(context);
    }

    @PostMapping("/order")
    public UniversalProcessor.ProcessResult processOrder(@RequestBody Map<String, Object> orderData) {
        return processBusinessScenario("order", "ORDER_PROCESS", orderData);
    }

    @PostMapping("/medical")
    public UniversalProcessor.ProcessResult processMedical(@RequestBody Map<String, Object> medicalData) {
        return processBusinessScenario("medical", "PRESCRIPTION", medicalData);
    }

    @PostMapping("/analytics")
    public UniversalProcessor.ProcessResult processAnalytics(@RequestBody Map<String, Object> analyticsData) {
        return processBusinessScenario("analytics", "INTELLIGENT_ANALYSIS", analyticsData);
    }

    @PostMapping("/warehouse")
    public UniversalProcessor.ProcessResult processWarehouse(@RequestBody Map<String, Object> warehouseData) {
        return processBusinessScenario("warehouse", "INVENTORY_OPTIMIZE", warehouseData);
    }

    @PostMapping("/customer-service")
    public UniversalProcessor.ProcessResult processCustomerService(@RequestBody Map<String, Object> serviceData) {
        return processBusinessScenario("customer-service", "AI_SUPPORT", serviceData);
    }

    @PostMapping("/marketing")
    public UniversalProcessor.ProcessResult processMarketing(@RequestBody Map<String, Object> marketingData) {
        return processBusinessScenario("marketing", "PRECISION_CAMPAIGN", marketingData);
    }

    @PostMapping("/quality")
    public UniversalProcessor.ProcessResult processQuality(@RequestBody Map<String, Object> qualityData) {
        return processBusinessScenario("quality", "INTELLIGENT_QC", qualityData);
    }

    @GetMapping("/info")
    public Map<String, Object> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("tenant", "tenant2");
        info.put("name", "Tenant2 Professional Edition");
        info.put("type", "PROFESSIONAL");
        info.put("extensionMode", "Event-Driven Extension Pattern");
        
        Map<String, String> scenarios = new HashMap<>();
        scenarios.put("order", "Intelligent order processing with fraud detection");
        scenarios.put("medical", "AI-powered medical diagnosis assistance");
        scenarios.put("analytics", "Intelligent data analysis and ML validation");
        scenarios.put("warehouse", "Smart inventory optimization");
        scenarios.put("customer-service", "AI intelligent customer service");
        scenarios.put("marketing", "Precision recommendation engine");
        scenarios.put("quality", "Intelligent quality control system");
        info.put("supportedScenarios", scenarios);
        
        Map<String, String> features = new HashMap<>();
        features.put("intelligentValidation", "Smart validation");
        features.put("fraudDetection", "Fraud detection");
        features.put("valueAddedServices", "Value-added services");
        features.put("businessInsights", "Business insights");
        features.put("fastTrackProcessing", "Fast track processing");
        info.put("professionalFeatures", features);

        return info;
    }

    private UniversalProcessor.ProcessResult processBusinessScenario(String scenario, String businessType, Map<String, Object> data) {
        Map<String, Object> request = new HashMap<>();
        request.put("scenario", scenario);
        request.put("businessType", businessType);
        request.put("tenantId", "tenant2");
        request.put("operatorId", data.getOrDefault("operatorId", "user002"));
        request.put("data", data);
        
        return processBusiness(request);
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
        String prefix;
        String scenarioStr = scenario != null ? scenario.toLowerCase() : "default";
        switch (scenarioStr) {
            case "order":
                prefix = "T2-ORD";
                break;
            case "medical":
                prefix = "T2-MED";
                break;
            case "analytics":
                prefix = "T2-ANA";
                break;
            case "warehouse":
                prefix = "T2-WHS";
                break;
            case "customer-service":
                prefix = "T2-CS";
                break;
            case "marketing":
                prefix = "T2-MKT";
                break;
            case "quality":
                prefix = "T2-QC";
                break;
            default:
                prefix = "T2-BIZ";
                break;
        }
        
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}