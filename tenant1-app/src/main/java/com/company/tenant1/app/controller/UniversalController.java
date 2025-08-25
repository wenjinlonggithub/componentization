package com.company.tenant1.app.controller;

import com.company.core.model.BusinessContext;
import com.company.core.processor.UniversalProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Tenant1 Universal Controller - Enterprise Edition
 */
@RestController
@RequestMapping("/api/business")
public class UniversalController {

    @Autowired
    private UniversalProcessor universalProcessor;

    @PostMapping("/process")
    public UniversalProcessor.ProcessResult processBusiness(@RequestBody Map<String, Object> request) {
        BusinessContext context = buildBusinessContext(request);
        
        System.out.println("TENANT1-APP: Processing " + context);
        
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

    @PostMapping("/finance")
    public UniversalProcessor.ProcessResult processFinance(@RequestBody Map<String, Object> financeData) {
        return processBusinessScenario("finance", "EXPENSE_AUDIT", financeData);
    }

    @PostMapping("/procurement")
    public UniversalProcessor.ProcessResult processProcurement(@RequestBody Map<String, Object> procurementData) {
        return processBusinessScenario("procurement", "VENDOR_APPROVAL", procurementData);
    }

    @PostMapping("/hr")
    public UniversalProcessor.ProcessResult processHR(@RequestBody Map<String, Object> hrData) {
        return processBusinessScenario("hr", "EMPLOYEE_ONBOARD", hrData);
    }

    @GetMapping("/info")
    public Map<String, Object> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("tenant", "tenant1");
        info.put("name", "Tenant1 Enterprise Edition");
        info.put("type", "ENTERPRISE");
        info.put("extensionMode", "Inheritance Extension Pattern");
        
        Map<String, String> scenarios = new HashMap<>();
        scenarios.put("order", "Enterprise order processing with approval workflow");
        scenarios.put("medical", "Multi-level medical certification");
        scenarios.put("finance", "Enterprise finance approval and compliance");
        scenarios.put("procurement", "Vendor qualification and procurement approval");
        scenarios.put("hr", "Multi-level HR approval with background check");
        info.put("supportedScenarios", scenarios);
        
        return info;
    }

    private UniversalProcessor.ProcessResult processBusinessScenario(String scenario, String businessType, Map<String, Object> data) {
        Map<String, Object> request = new HashMap<>();
        request.put("scenario", scenario);
        request.put("businessType", businessType);
        request.put("tenantId", "tenant1");
        request.put("operatorId", data.getOrDefault("operatorId", "user001"));
        request.put("data", data);
        
        return processBusiness(request);
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
        String prefix;
        String scenarioStr = scenario != null ? scenario.toLowerCase() : "default";
        switch (scenarioStr) {
            case "order":
                prefix = "T1-ORD";
                break;
            case "medical":
                prefix = "T1-MED";
                break;
            case "finance":
                prefix = "T1-FIN";
                break;
            case "procurement":
                prefix = "T1-PRO";
                break;
            case "hr":
                prefix = "T1-HRM";
                break;
            default:
                prefix = "T1-BIZ";
                break;
        }
        
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}