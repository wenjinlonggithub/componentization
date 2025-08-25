package com.company.tenant1.processor;

import com.company.core.model.MedicalRecord;
import com.company.core.processor.DefaultMedicalRecordProcessor;
import com.company.tenant1.service.DatasetStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomMedicalRecordProcessor extends DefaultMedicalRecordProcessor {

    @Autowired
    private DatasetStorageService datasetStorageService;

    @Override
    protected void beforeSave(MedicalRecord medicalRecord) {
        System.out.println("租户1：病历保存前进行数据质量检查");
        
        // 租户1特有的数据质量检查
        validateForDataset(medicalRecord);
        
        // 标准化数据格式，便于数据集分析
        standardizeForDataset(medicalRecord);
    }

    @Override
    protected void performSave(MedicalRecord medicalRecord) {
        // 不使用默认存储，直接保存到数据集
        datasetStorageService.saveToDataset(medicalRecord);
        medicalRecord.setStatus("SAVED_TO_DATASET");
        
        System.out.println("租户1：病历已保存到数据集，状态：" + medicalRecord.getStatus());
    }

    private void validateForDataset(MedicalRecord record) {
        // 数据集需要的额外验证
        if (record.getChiefComplaint() == null || record.getChiefComplaint().trim().isEmpty()) {
            System.out.println("租户1：警告 - 主诉为空，将影响数据集质量");
        }
        
        if (record.getDiagnosis() == null || record.getDiagnosis().isEmpty()) {
            System.out.println("租户1：警告 - 诊断为空，将影响数据集质量");
        }
        
        System.out.println("租户1：数据集质量检查完成");
    }

    private void standardizeForDataset(MedicalRecord record) {
        // 标准化文本格式
        if (record.getChiefComplaint() != null) {
            record.setChiefComplaint(record.getChiefComplaint().trim());
        }
        if (record.getPresentIllnessHistory() != null) {
            record.setPresentIllnessHistory(record.getPresentIllnessHistory().trim());
        }
        if (record.getRecommendations() != null) {
            record.setRecommendations(record.getRecommendations().trim());
        }
        
        System.out.println("租户1：病历数据标准化完成");
    }
}