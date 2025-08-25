package com.company.core.event;

import com.company.core.model.MedicalRecord;
import org.springframework.context.ApplicationEvent;

public class AfterSaveMedicalRecordEvent extends ApplicationEvent {
    private final MedicalRecord medicalRecord;

    public AfterSaveMedicalRecordEvent(Object source, MedicalRecord medicalRecord) {
        super(source);
        this.medicalRecord = medicalRecord;
    }

    public MedicalRecord getMedicalRecord() { return medicalRecord; }
}