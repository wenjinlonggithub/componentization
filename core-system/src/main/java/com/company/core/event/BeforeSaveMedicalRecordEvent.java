package com.company.core.event;

import com.company.core.model.MedicalRecord;
import org.springframework.context.ApplicationEvent;

public class BeforeSaveMedicalRecordEvent extends ApplicationEvent {
    private final MedicalRecord medicalRecord;
    private boolean skipDefaultAction = false;

    public BeforeSaveMedicalRecordEvent(Object source, MedicalRecord medicalRecord) {
        super(source);
        this.medicalRecord = medicalRecord;
    }

    public MedicalRecord getMedicalRecord() { return medicalRecord; }

    public void skipDefaultAction() { this.skipDefaultAction = true; }
    public boolean isSkipDefaultAction() { return skipDefaultAction; }
}