package com.company.tenant1.listener;

import com.company.core.event.AfterSaveMedicalRecordEvent;
import com.company.core.event.BeforeSaveMedicalRecordEvent;
import com.company.tenant1.service.DatasetStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MedicalRecordEventListener {

    @Autowired
    private DatasetStorageService datasetStorageService;

    @EventListener
    public void handleBeforeSave(BeforeSaveMedicalRecordEvent event) {
        System.out.println("租户1事件监听：病历保存前处理 - " + event.getMedicalRecord().getId());
        
        // 检查数据集存储空间
        checkDatasetStorage();
    }

    @EventListener
    public void handleAfterSave(AfterSaveMedicalRecordEvent event) {
        System.out.println("租户1事件监听：病历保存后处理 - " + event.getMedicalRecord().getId());
        
        // 生成数据集报告
        datasetStorageService.generateDatasetReport();
        
        // 执行数据集相关的后续处理
        processDatasetAnalytics(event.getMedicalRecord());
    }

    private void checkDatasetStorage() {
        // 检查数据集存储空间和状态
        System.out.println("租户1：检查数据集存储状态");
        
        // 这里可以添加磁盘空间检查、数据集大小限制等逻辑
        java.io.File datasetFile = new java.io.File("medical_records_dataset.csv");
        if (datasetFile.exists()) {
            long fileSizeKB = datasetFile.length() / 1024;
            System.out.println("租户1：当前数据集文件大小: " + fileSizeKB + "KB");
            
            if (fileSizeKB > 10240) { // 10MB
                System.out.println("租户1：警告 - 数据集文件较大，建议进行数据归档");
            }
        }
    }

    private void processDatasetAnalytics(com.company.core.model.MedicalRecord record) {
        System.out.println("租户1：执行数据集分析处理");
        
        // 这里可以触发数据分析任务
        if ("SAVED_TO_DATASET".equals(record.getStatus())) {
            System.out.println("租户1：新的病历记录已加入数据集，触发增量分析");
            
            // 示例：简单的统计分析
            if (record.getDiagnosis() != null && !record.getDiagnosis().isEmpty()) {
                System.out.println("租户1：诊断数据可用于疾病谱分析");
            }
            
            if (record.getAllergyHistory() != null && !record.getAllergyHistory().isEmpty()) {
                System.out.println("租户1：过敏史数据可用于过敏原分析");
            }
        }
    }
}