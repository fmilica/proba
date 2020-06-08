package mrs.isa.team12.clinical.center.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mrs.isa.team12.clinical.center.model.MedicalReport;
import mrs.isa.team12.clinical.center.model.Prescription;

public class MedicalReportDto {
	
	private Long id;
	private Long appId;
	private String description;
	private String diagnosisName;
	private List<Long> prescriptionIds;
	private Set<String> prescriptionMedicines;
	
	public MedicalReportDto() {}
	
	public MedicalReportDto(MedicalReport medicalReport) {
		this.id = medicalReport.getId();
		this.description = medicalReport.getDescription();
		this.diagnosisName = medicalReport.getDiagnosis().getName();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDiagnosisName() {
		return diagnosisName;
	}
	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}
	public List<Long> getPrescriptionIds() {
		return prescriptionIds;
	}
	public void setPrescriptioinIds(List<Long> prescriptionIds) {
		this.prescriptionIds = prescriptionIds;
	}
	public Set<String> getPrescriptionMedicines() {
		return prescriptionMedicines;
	}
	public void setPrescriptionMedicines(Set<Prescription> prescriptions) {
		for(Prescription p : prescriptions) {
			if (this.prescriptionMedicines == null) {
				this.prescriptionMedicines = new HashSet<String>();
			}
			this.prescriptionMedicines.add(p.getMedicine());
		}
	}
	
	@Override
	public String toString() {
		return "MedicalReportDto [description=" + description + ", diagnosisName=" + diagnosisName
				+ ", prescriptionMedicines=" + prescriptionMedicines + "]";
	}
}
