package com.company.core.processor;

import com.company.core.event.AfterSaveMedicalRecordEvent;
import com.company.core.event.BeforeSaveMedicalRecordEvent;
import com.company.core.model.MedicalRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class DefaultMedicalRecordProcessor implements MedicalRecordProcessor {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    // 简单的内存存储，实际项目中会使用数据库
    private final Map<String, MedicalRecord> recordStorage = new HashMap<>();

    @Override
    public void saveMedicalRecord(MedicalRecord medicalRecord) {
        System.out.println("开始保存病历：患者ID=" + medicalRecord.getPatientId());
        
        // 验证病历数据
        validateMedicalRecord(medicalRecord);
        
        // 设置ID和时间戳
        if (medicalRecord.getId() == null) {
            medicalRecord.setId(UUID.randomUUID().toString());
        }
        medicalRecord.setUpdatedAt(LocalDateTime.now());
        
        // 发布保存前事件
        BeforeSaveMedicalRecordEvent beforeSaveEvent = new BeforeSaveMedicalRecordEvent(this, medicalRecord);
        eventPublisher.publishEvent(beforeSaveEvent);
        
        // 调用钩子方法
        beforeSave(medicalRecord);
        
        // 条件执行保存
        if (!beforeSaveEvent.isSkipDefaultAction()) {
            performSave(medicalRecord);
            
            // 发布保存后事件
            eventPublisher.publishEvent(new AfterSaveMedicalRecordEvent(this, medicalRecord));
        }
        
        System.out.println("病历保存完成：ID=" + medicalRecord.getId());
    }

    @Override
    public MedicalRecord getMedicalRecord(String recordId) {
        return recordStorage.get(recordId);
    }

    @Override
    public void updateMedicalRecord(MedicalRecord medicalRecord) {
        if (medicalRecord.getId() == null || !recordStorage.containsKey(medicalRecord.getId())) {
            throw new IllegalArgumentException("病历记录不存在");
        }
        saveMedicalRecord(medicalRecord);
    }

    protected void validateMedicalRecord(MedicalRecord record) {
        if (record.getPatientId() == null || record.getPatientId().isEmpty()) {
            throw new IllegalArgumentException("患者ID不能为空");
        }
        if (record.getDoctorId() == null || record.getDoctorId().isEmpty()) {
            throw new IllegalArgumentException("医生ID不能为空");
        }
        System.out.println("病历数据验证通过");
    }

    protected void beforeSave(MedicalRecord medicalRecord) {
        // 默认空实现，供子类覆盖
    }

    protected void performSave(MedicalRecord medicalRecord) {
        recordStorage.put(medicalRecord.getId(), medicalRecord);
        medicalRecord.setStatus("COMPLETED");
        System.out.println("病历已保存到默认存储：" + medicalRecord.getId());
    }
}