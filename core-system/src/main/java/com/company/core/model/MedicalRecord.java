package com.company.core.model;

import java.time.LocalDateTime;
import java.util.List;

public class MedicalRecord {
    private String id;
    private String patientId;
    private String doctorId;
    private String chiefComplaint;           // 主诉
    private String presentIllnessHistory;    // 现病史
    private String pastMedicalHistory;       // 既往病史
    private List<String> allergyHistory;     // 过敏史
    private List<String> diagnosis;          // 诊断
    private String recommendations;          // 建议
    private String otherNotes;              // 其他
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;                   // 状态：DRAFT, COMPLETED, SUBMITTED

    public MedicalRecord() {}

    public MedicalRecord(String patientId, String doctorId) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.createdAt = LocalDateTime.now();
        this.status = "DRAFT";
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getChiefComplaint() { return chiefComplaint; }
    public void setChiefComplaint(String chiefComplaint) { this.chiefComplaint = chiefComplaint; }

    public String getPresentIllnessHistory() { return presentIllnessHistory; }
    public void setPresentIllnessHistory(String presentIllnessHistory) { this.presentIllnessHistory = presentIllnessHistory; }

    public String getPastMedicalHistory() { return pastMedicalHistory; }
    public void setPastMedicalHistory(String pastMedicalHistory) { this.pastMedicalHistory = pastMedicalHistory; }

    public List<String> getAllergyHistory() { return allergyHistory; }
    public void setAllergyHistory(List<String> allergyHistory) { this.allergyHistory = allergyHistory; }

    public List<String> getDiagnosis() { return diagnosis; }
    public void setDiagnosis(List<String> diagnosis) { this.diagnosis = diagnosis; }

    public String getRecommendations() { return recommendations; }
    public void setRecommendations(String recommendations) { this.recommendations = recommendations; }

    public String getOtherNotes() { return otherNotes; }
    public void setOtherNotes(String otherNotes) { this.otherNotes = otherNotes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}