package com.company.emr.tenant1.app;

import com.company.core.model.MedicalRecord;
import com.company.core.model.Patient;
import com.company.core.processor.MedicalRecordProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = {"com.company.core", "com.company.tenant1"})
public class EmrTenant1Application implements CommandLineRunner {

    @Autowired
    private MedicalRecordProcessor medicalRecordProcessor;

    public static void main(String[] args) {
        SpringApplication.run(EmrTenant1Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n=== EMR租户1应用演示（数据集存储模式） ===");
        
        // 创建示例患者
        Patient patient1 = new Patient("P001", "张三", "男", LocalDate.of(1980, 5, 15));
        patient1.setPhone("13800138001");
        patient1.setIdCard("110101198005151234");
        
        Patient patient2 = new Patient("P002", "李四", "女", LocalDate.of(1975, 8, 20));
        patient2.setPhone("13800138002");
        patient2.setIdCard("110101197508201234");

        System.out.println("\n--- 处理常规病历（数据集存储） ---");
        MedicalRecord record1 = createSampleRecord(patient1, "DR001");
        medicalRecordProcessor.saveMedicalRecord(record1);

        System.out.println("\n--- 处理复杂病历（数据集存储） ---");
        MedicalRecord record2 = createComplexRecord(patient2, "DR002");
        medicalRecordProcessor.saveMedicalRecord(record2);

        System.out.println("\n=== EMR租户1演示完成 ===\n");
    }

    private MedicalRecord createSampleRecord(Patient patient, String doctorId) {
        MedicalRecord record = new MedicalRecord(patient.getId(), doctorId);
        
        record.setChiefComplaint("头痛3天，伴有恶心");
        record.setPresentIllnessHistory("患者3天前开始出现头痛，呈持续性胀痛，位于双侧太阳穴，伴有恶心，无呕吐，无发热。");
        record.setPastMedicalHistory("既往身体健康，无高血压、糖尿病等慢性疾病史。");
        record.setAllergyHistory(Arrays.asList("青霉素过敏"));
        record.setDiagnosis(Arrays.asList("偏头痛", "可能的血管神经性头痛"));
        record.setRecommendations("建议充分休息，避免熬夜和情绪紧张，可适当服用止痛药。如症状持续或加重，请及时复诊。");
        record.setOtherNotes("患者工作压力较大，建议调整工作节奏。");
        
        return record;
    }

    private MedicalRecord createComplexRecord(Patient patient, String doctorId) {
        MedicalRecord record = new MedicalRecord(patient.getId(), doctorId);
        
        record.setChiefComplaint("胸痛1周，活动后加重");
        record.setPresentIllnessHistory("患者1周前开始出现胸痛，位于胸骨后，呈压榨性疼痛，活动后明显加重，休息后可缓解。伴有轻微气促，无放射痛。");
        record.setPastMedicalHistory("既往有高血压病史5年，平素血压控制尚可。父亲有冠心病史。");
        record.setAllergyHistory(Arrays.asList("对阿司匹林过敏", "海鲜过敏"));
        record.setDiagnosis(Arrays.asList("疑似冠心病", "心绞痛待排", "高血压病2级"));
        record.setRecommendations("建议完善心电图、心脏彩超、冠脉CTA等检查。暂时给予硝酸甘油含服，避免剧烈运动。严密观察病情变化，如胸痛加重立即就医。");
        record.setOtherNotes("患者年龄较大，有家族史，需要密切关注心血管风险。已告知患者及家属注意事项。");
        
        return record;
    }
}