package com.company.tenant2.service;

import com.company.core.model.MedicalRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class HisIntegrationService {
    
    // 模拟HIS系统响应
    private final Map<String, Object> hisSystemStatus = new HashMap<>();
    private final List<String> uploadHistory = new ArrayList<>();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public HisIntegrationService() {
        // 初始化HIS系统状态
        hisSystemStatus.put("status", "ONLINE");
        hisSystemStatus.put("lastConnected", LocalDateTime.now().format(FORMATTER));
        hisSystemStatus.put("version", "HIS-v2.3.1");
    }
    
    public void uploadToHisSystem(MedicalRecord record) {
        System.out.println("租户2：开始上报病历到HIS系统");
        
        try {
            // 检查HIS系统连接状态
            validateHisConnection();
            
            // 转换病历数据为HIS格式
            Map<String, Object> hisData = convertToHisFormat(record);
            
            // 模拟上传到HIS系统
            String uploadId = performHisUpload(hisData);
            
            // 记录上传历史
            recordUploadHistory(record.getId(), uploadId);
            
            System.out.println("租户2：病历已成功上报到HIS系统，上传ID: " + uploadId);
            
        } catch (Exception e) {
            System.err.println("租户2：HIS系统上报失败: " + e.getMessage());
            throw new RuntimeException("HIS系统上报失败", e);
        }
    }
    
    private void validateHisConnection() {
        System.out.println("租户2：验证HIS系统连接");
        
        // 模拟HIS系统连接检查
        if (!"ONLINE".equals(hisSystemStatus.get("status"))) {
            throw new RuntimeException("HIS系统不可用");
        }
        
        // 模拟网络延迟检查
        simulateNetworkLatency();
        
        System.out.println("租户2：HIS系统连接正常");
    }
    
    private Map<String, Object> convertToHisFormat(MedicalRecord record) {
        System.out.println("租户2：转换病历数据为HIS标准格式");
        
        Map<String, Object> hisData = new HashMap<>();
        
        // HIS系统标准字段映射
        hisData.put("his_record_id", record.getId());
        hisData.put("his_patient_id", record.getPatientId());
        hisData.put("his_doctor_id", record.getDoctorId());
        hisData.put("his_chief_complaint", record.getChiefComplaint());
        hisData.put("his_present_illness", record.getPresentIllnessHistory());
        hisData.put("his_past_medical_history", record.getPastMedicalHistory());
        hisData.put("his_allergy_history", record.getAllergyHistory());
        hisData.put("his_diagnosis", record.getDiagnosis());
        hisData.put("his_recommendations", record.getRecommendations());
        hisData.put("his_other_notes", record.getOtherNotes());
        hisData.put("his_created_time", record.getCreatedAt() != null ? 
            record.getCreatedAt().format(FORMATTER) : "");
        hisData.put("his_status", record.getStatus());
        
        // HIS系统特有字段
        hisData.put("his_upload_time", LocalDateTime.now().format(FORMATTER));
        hisData.put("his_source_system", "EMR-TENANT2");
        hisData.put("his_data_version", "1.0");
        
        System.out.println("租户2：HIS数据格式转换完成");
        return hisData;
    }
    
    private String performHisUpload(Map<String, Object> hisData) {
        System.out.println("租户2：执行HIS系统数据上传");
        
        // 模拟HIS系统上传过程
        String uploadId = "HIS-" + UUID.randomUUID().toString().substring(0, 8);
        
        // 模拟数据验证
        validateHisData(hisData);
        
        // 模拟网络传输
        simulateDataTransmission(hisData);
        
        // 模拟HIS系统处理
        simulateHisProcessing(uploadId);
        
        return uploadId;
    }
    
    private void validateHisData(Map<String, Object> hisData) {
        System.out.println("租户2：HIS数据格式验证");
        
        // 验证必填字段
        String[] requiredFields = {"his_record_id", "his_patient_id", "his_doctor_id"};
        for (String field : requiredFields) {
            if (hisData.get(field) == null) {
                throw new RuntimeException("HIS必填字段缺失: " + field);
            }
        }
        
        System.out.println("租户2：HIS数据验证通过");
    }
    
    private void simulateDataTransmission(Map<String, Object> hisData) {
        System.out.println("租户2：模拟数据传输到HIS系统...");
        
        try {
            // 模拟网络传输时间
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        int dataSize = hisData.toString().length();
        System.out.println("租户2：数据传输完成，传输大小: " + dataSize + " bytes");
    }
    
    private void simulateHisProcessing(String uploadId) {
        System.out.println("租户2：HIS系统处理数据中...");
        
        try {
            // 模拟HIS系统处理时间
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("租户2：HIS系统处理完成，返回上传ID: " + uploadId);
    }
    
    private void simulateNetworkLatency() {
        try {
            // 模拟网络延迟
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void recordUploadHistory(String recordId, String uploadId) {
        String historyEntry = String.format("Record: %s -> HIS Upload: %s at %s", 
            recordId, uploadId, LocalDateTime.now().format(FORMATTER));
        uploadHistory.add(historyEntry);
        
        // 保留最近100条记录
        if (uploadHistory.size() > 100) {
            uploadHistory.remove(0);
        }
    }
    
    public Map<String, Object> getHisSystemStatus() {
        Map<String, Object> status = new HashMap<>(hisSystemStatus);
        status.put("uploadHistory", uploadHistory.size());
        status.put("lastChecked", LocalDateTime.now().format(FORMATTER));
        return status;
    }
    
    public List<String> getUploadHistory() {
        return new ArrayList<>(uploadHistory);
    }
    
    public void performHisHealthCheck() {
        System.out.println("租户2：执行HIS系统健康检查");
        
        try {
            validateHisConnection();
            
            // 更新系统状态
            hisSystemStatus.put("lastHealthCheck", LocalDateTime.now().format(FORMATTER));
            hisSystemStatus.put("healthStatus", "HEALTHY");
            
            System.out.println("租户2：HIS系统健康检查通过");
            
        } catch (Exception e) {
            hisSystemStatus.put("healthStatus", "UNHEALTHY");
            hisSystemStatus.put("lastError", e.getMessage());
            
            System.err.println("租户2：HIS系统健康检查失败: " + e.getMessage());
        }
    }
}