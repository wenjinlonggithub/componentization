package com.company.core.processor;

import com.company.core.model.MedicalRecord;

public interface MedicalRecordProcessor {
    void saveMedicalRecord(MedicalRecord medicalRecord);
    MedicalRecord getMedicalRecord(String recordId);
    void updateMedicalRecord(MedicalRecord medicalRecord);
}