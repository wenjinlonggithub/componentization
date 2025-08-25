package com.company.tenant1.service;

import com.company.core.model.MedicalRecord;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DatasetStorageService {

    private static final String DATASET_FILE_PATH = "medical_records_dataset.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void saveToDataset(MedicalRecord record) {
        try {
            System.out.println("租户1：将病历保存到数据集文件");
            
            // 检查文件是否存在，如果不存在则写入表头
            boolean fileExists = new java.io.File(DATASET_FILE_PATH).exists();
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATASET_FILE_PATH, true))) {
                // 写入CSV表头（仅在文件不存在时）
                if (!fileExists) {
                    writer.write("ID,PatientID,DoctorID,ChiefComplaint,PresentIllnessHistory,PastMedicalHistory,AllergyHistory,Diagnosis,Recommendations,OtherNotes,CreatedAt,UpdatedAt,Status");
                    writer.newLine();
                }
                
                // 写入病历数据
                writer.write(formatRecordToCsv(record));
                writer.newLine();
                writer.flush();
                
                System.out.println("租户1：病历数据已写入数据集文件 - " + DATASET_FILE_PATH);
            }
        } catch (IOException e) {
            System.err.println("租户1：写入数据集失败: " + e.getMessage());
            throw new RuntimeException("数据集存储失败", e);
        }
    }

    private String formatRecordToCsv(MedicalRecord record) {
        return String.join(",",
            escapeForCsv(record.getId()),
            escapeForCsv(record.getPatientId()),
            escapeForCsv(record.getDoctorId()),
            escapeForCsv(record.getChiefComplaint()),
            escapeForCsv(record.getPresentIllnessHistory()),
            escapeForCsv(record.getPastMedicalHistory()),
            escapeForCsv(record.getAllergyHistory() != null ? String.join(";", record.getAllergyHistory()) : ""),
            escapeForCsv(record.getDiagnosis() != null ? String.join(";", record.getDiagnosis()) : ""),
            escapeForCsv(record.getRecommendations()),
            escapeForCsv(record.getOtherNotes()),
            escapeForCsv(record.getCreatedAt() != null ? record.getCreatedAt().format(FORMATTER) : ""),
            escapeForCsv(record.getUpdatedAt() != null ? record.getUpdatedAt().format(FORMATTER) : ""),
            escapeForCsv(record.getStatus())
        );
    }

    private String escapeForCsv(String value) {
        if (value == null) return "";
        // 转义CSV中的特殊字符
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    public void generateDatasetReport() {
        System.out.println("租户1：生成数据集分析报告");
        try {
            java.io.File file = new java.io.File(DATASET_FILE_PATH);
            if (file.exists()) {
                long lines = java.nio.file.Files.lines(file.toPath()).count() - 1; // 减去表头
                System.out.println("租户1：数据集统计 - 当前包含 " + lines + " 条病历记录");
            } else {
                System.out.println("租户1：数据集文件不存在");
            }
        } catch (IOException e) {
            System.err.println("租户1：读取数据集失败: " + e.getMessage());
        }
    }
}