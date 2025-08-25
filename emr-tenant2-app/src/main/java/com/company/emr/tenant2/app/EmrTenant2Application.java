package com.company.emr.tenant2.app;

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
@ComponentScan(basePackages = {"com.company.core", "com.company.tenant2"})
public class EmrTenant2Application implements CommandLineRunner {

    @Autowired
    private MedicalRecordProcessor medicalRecordProcessor;

    public static void main(String[] args) {
        SpringApplication.run(EmrTenant2Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n=== EMR租户2应用演示（企业版：数据库+HIS系统） ===");
        
        // 创建示例患者
        Patient patient1 = new Patient("P003", "王五", "男", LocalDate.of(1985, 3, 10));
        patient1.setPhone("13800138003");
        patient1.setIdCard("110101198503101234");
        
        Patient patient2 = new Patient("P004", "赵六", "女", LocalDate.of(1990, 12, 5));
        patient2.setPhone("13800138004");
        patient2.setIdCard("110101199012051234");

        System.out.println("\n--- 处理企业级病历（数据库+HIS上报） ---");
        MedicalRecord record1 = createEnterpriseRecord(patient1, "DR003");
        medicalRecordProcessor.saveMedicalRecord(record1);

        System.out.println("\n--- 处理复杂企业病历（完整工作流） ---");
        MedicalRecord record2 = createComplexEnterpriseRecord(patient2, "DR004");
        medicalRecordProcessor.saveMedicalRecord(record2);

        System.out.println("\n=== EMR租户2演示完成 ===\n");
    }

    private MedicalRecord createEnterpriseRecord(Patient patient, String doctorId) {
        MedicalRecord record = new MedicalRecord(patient.getId(), doctorId);
        
        record.setChiefComplaint("发热2天，咳嗽咳痰");
        record.setPresentIllnessHistory("患者2天前开始出现发热，体温最高39.2℃，伴有咳嗽，咳白色粘痰，量中等。无胸闷气促，无腹痛腹泻。");
        record.setPastMedicalHistory("既往身体健康，无慢性疾病史，无手术史。疫苗接种史完整。");
        record.setAllergyHistory(Arrays.asList("无明确药物过敏史"));
        record.setDiagnosis(Arrays.asList("上呼吸道感染", "病毒性感染可能"));
        record.setRecommendations("建议多饮水，充分休息，可适当服用退热药物。密切观察体温变化，如持续高热超过3天请复诊。");
        record.setOtherNotes("患者为医院员工，建议做好个人防护，避免交叉感染。");
        
        return record;
    }

    private MedicalRecord createComplexEnterpriseRecord(Patient patient, String doctorId) {
        MedicalRecord record = new MedicalRecord(patient.getId(), doctorId);
        
        record.setChiefComplaint("糖尿病复查，血糖控制不佳");
        record.setPresentIllnessHistory("患者糖尿病史3年，近1月血糖控制不佳，空腹血糖波动在8-12mmol/L，餐后2小时血糖12-16mmol/L。伴有多尿、乏力症状。");
        record.setPastMedicalHistory("2型糖尿病3年，曾规律服用二甲双胍。母亲有糖尿病史。无其他重要疾病史。");
        record.setAllergyHistory(Arrays.asList("磺胺类药物过敏"));
        record.setDiagnosis(Arrays.asList("2型糖尿病", "糖尿病血糖控制不佳", "糖尿病慢性并发症待排"));
        record.setRecommendations("调整降糖方案，加用胰岛素治疗。建议完善糖化血红蛋白、肾功能、眼底检查等。加强饮食控制和运动锻炼。定期监测血糖。");
        record.setOtherNotes("患者依从性一般，需要加强健康教育。已安排糖尿病专科护士进行个体化指导。家属已告知病情及注意事项。");
        
        return record;
    }
}