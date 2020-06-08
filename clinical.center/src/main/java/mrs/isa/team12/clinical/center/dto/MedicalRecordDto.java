package mrs.isa.team12.clinical.center.dto;

import java.util.HashSet;
import java.util.Set;

import mrs.isa.team12.clinical.center.model.Appointment;
import mrs.isa.team12.clinical.center.model.MedicalRecords;

public class MedicalRecordDto {
	private Long id;
	private Integer height;
	private Integer weight;
	private String bloodPressure;
	private String bloodType;
	private String allergies;
	
	private Set<MedicalReportDto> medicalReports;
	
	public MedicalRecordDto() {}
	
	public MedicalRecordDto(MedicalRecords medicalRecords) {
		super();
		this.id = medicalRecords.getId();
		this.height = medicalRecords.getHeight();
		this.weight = medicalRecords.getWeight();
		this.bloodPressure = medicalRecords.getBloodPressure();
		this.bloodType = medicalRecords.getBloodType();
		this.allergies = medicalRecords.getAllergies();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public String getBloodPressure() {
		return bloodPressure;
	}
	public void setBloodPressure(String bloodPressure) {
		this.bloodPressure = bloodPressure;
	}
	public String getBloodType() {
		return bloodType;
	}
	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}
	public String getAllergies() {
		return allergies;
	}
	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}

	public Set<MedicalReportDto> getMedicalReports() {
		return medicalReports;
	}

	public void setMedicalReports(Set<Appointment> appointments) {
		if (appointments != null) {
			for(Appointment a : appointments) {
				if (a.getFinished()) {
					MedicalReportDto medicalReport = new MedicalReportDto(a.getMedicalReport());
					medicalReport.setPrescriptionMedicines(a.getMedicalReport().getPrescriptions());
					if (this.medicalReports == null) {
						this.medicalReports = new HashSet<MedicalReportDto>();
					}
					this.medicalReports.add(medicalReport);
				}
			}
		}
	}

	@Override
	public String toString() {
		return "MedicalRecordDto [height=" + height + ", weight=" + weight + ", bloodPressure=" + bloodPressure
				+ ", bloodType=" + bloodType + ", allergies=" + allergies + ", medicalReports=" + medicalReports + "]";
	}
}
