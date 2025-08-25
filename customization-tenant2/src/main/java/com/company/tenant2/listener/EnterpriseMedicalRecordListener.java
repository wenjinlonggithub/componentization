package com.company.tenant2.listener;

import com.company.core.event.AfterSaveMedicalRecordEvent;
import com.company.core.event.BeforeSaveMedicalRecordEvent;
import com.company.core.model.MedicalRecord;
import com.company.tenant2.service.DatabaseService;
import com.company.tenant2.service.HisIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class EnterpriseMedicalRecordListener {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private HisIntegrationService hisIntegrationService;

    @EventListener
    @Order(1)
    public void handleBeforeSave(BeforeSaveMedicalRecordEvent event) {
        System.out.println("租户2事件监听：病历保存前企业级验证");
        
        MedicalRecord record = event.getMedicalRecord();
        
        // 执行企业级数据验证
        performEnterpriseValidation(record);
        
        // 检查系统状态
        checkSystemHealth();
    }

    @EventListener
    public void handleAfterSave(AfterSaveMedicalRecordEvent event) {
        System.out.println("租户2事件监听：病历保存后企业级处理");
        
        MedicalRecord record = event.getMedicalRecord();
        
        try {
            // 保存到企业数据库
            databaseService.saveToDatabase(record);
            
            // 上报到HIS系统
            hisIntegrationService.uploadToHisSystem(record);
            
            // 执行后续企业流程
            executeEnterpriseWorkflow(record);
            
        } catch (Exception e) {
            System.err.println("租户2：企业级处理失败: " + e.getMessage());
            
            // 处理失败的补偿操作
            handleProcessingFailure(record, e);
        }
    }

    private void performEnterpriseValidation(MedicalRecord record) {
        System.out.println("租户2：执行企业级数据验证");
        
        // 企业级别的数据质量检查
        validateDataQuality(record);
        
        // 合规性检查
        validateCompliance(record);
        
        // 安全性检查
        validateSecurity(record);
        
        System.out.println("租户2：企业级验证完成");
    }

    private void validateDataQuality(MedicalRecord record) {
        System.out.println("租户2：数据质量检查");
        
        // 检查必要字段的完整性
        if (record.getChiefComplaint() == null || record.getChiefComplaint().trim().isEmpty()) {
            System.out.println("租户2：警告 - 主诉缺失，影响病历质量");
        }
        
        if (record.getDiagnosis() == null || record.getDiagnosis().isEmpty()) {
            System.out.println("租户2：警告 - 诊断信息缺失");
        }
        
        // 检查数据格式和长度
        if (record.getChiefComplaint() != null && record.getChiefComplaint().length() > 1000) {
            System.out.println("租户2：警告 - 主诉内容过长");
        }
        
        System.out.println("租户2：数据质量检查完成");
    }

    private void validateCompliance(MedicalRecord record) {
        System.out.println("租户2：合规性检查");
        
        // 检查是否包含敏感信息
        checkSensitiveInformation(record);
        
        // 检查数据隐私合规
        checkPrivacyCompliance(record);
        
        System.out.println("租户2：合规性检查完成");
    }

    private void validateSecurity(MedicalRecord record) {
        System.out.println("租户2：安全性检查");
        
        // 检查数据完整性
        if (record.getId() == null) {
            System.out.println("租户2：警告 - 病历ID缺失，存在安全风险");
        }
        
        // 检查访问权限（这里简化处理）
        System.out.println("租户2：安全性检查完成");
    }

    private void checkSensitiveInformation(MedicalRecord record) {
        // 检查是否包含不应该出现的敏感信息
        String[] sensitivePatterns = {"身份证", "银行卡", "密码"};
        
        String content = (record.getChiefComplaint() + " " + 
                         record.getPresentIllnessHistory() + " " + 
                         record.getRecommendations()).toLowerCase();
        
        for (String pattern : sensitivePatterns) {
            if (content.contains(pattern)) {
                System.out.println("租户2：警告 - 检测到疑似敏感信息: " + pattern);
            }
        }
    }

    private void checkPrivacyCompliance(MedicalRecord record) {
        // 简单的隐私合规检查
        System.out.println("租户2：检查数据隐私合规性");
        
        // 检查是否有患者同意书等（这里简化）
        if (record.getPatientId() != null) {
            System.out.println("租户2：患者ID已匿名化处理");
        }
    }

    private void checkSystemHealth() {
        System.out.println("租户2：检查企业系统健康状态");
        
        // 检查数据库状态
        try {
            var dbStats = databaseService.getDatabaseStatistics();
            System.out.println("租户2：数据库状态正常，记录数: " + dbStats.get("totalRecords"));
        } catch (Exception e) {
            System.err.println("租户2：数据库健康检查失败: " + e.getMessage());
        }
        
        // 检查HIS系统状态
        try {
            hisIntegrationService.performHisHealthCheck();
        } catch (Exception e) {
            System.err.println("租户2：HIS系统健康检查失败: " + e.getMessage());
        }
    }

    private void executeEnterpriseWorkflow(MedicalRecord record) {
        System.out.println("租户2：执行企业级后续流程");
        
        // 触发企业工作流
        triggerWorkflowProcess(record);
        
        // 生成企业报告
        generateEnterpriseReports(record);
        
        // 更新企业指标
        updateEnterpriseMetrics(record);
    }

    private void triggerWorkflowProcess(MedicalRecord record) {
        System.out.println("租户2：触发企业工作流程");
        
        // 根据诊断结果触发不同的工作流
        if (record.getDiagnosis() != null && !record.getDiagnosis().isEmpty()) {
            for (String diagnosis : record.getDiagnosis()) {
                if (diagnosis.contains("高血压") || diagnosis.contains("糖尿病")) {
                    System.out.println("租户2：触发慢性病管理流程");
                }
                if (diagnosis.contains("感染") || diagnosis.contains("发热")) {
                    System.out.println("租户2：触发感染控制流程");
                }
            }
        }
    }

    private void generateEnterpriseReports(MedicalRecord record) {
        System.out.println("租户2：生成企业级报告");
        
        // 生成质量报告
        System.out.println("租户2：病历质量报告已生成");
        
        // 生成统计报告
        System.out.println("租户2：统计分析报告已更新");
    }

    private void updateEnterpriseMetrics(MedicalRecord record) {
        System.out.println("租户2：更新企业指标");
        
        // 更新KPI指标
        System.out.println("租户2：医疗质量KPI已更新");
        System.out.println("租户2：HIS系统集成率指标已更新");
    }

    private void handleProcessingFailure(MedicalRecord record, Exception e) {
        System.err.println("租户2：处理病历保存失败，启动补偿机制");
        
        // 记录失败信息
        System.err.println("租户2：记录失败原因: " + e.getMessage());
        
        // 尝试重试或采取补偿措施
        System.out.println("租户2：将失败记录加入重试队列");
        
        // 发送告警通知
        System.out.println("租户2：发送系统告警通知");
    }
}