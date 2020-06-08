package mrs.isa.team12.clinical.center.dto;

import java.util.HashSet;
import java.util.Set;

import mrs.isa.team12.clinical.center.model.Appointment;
import mrs.isa.team12.clinical.center.model.MedicalReport;
import mrs.isa.team12.clinical.center.model.Prescription;

public class MedicalReportVerifyDto {
	private Long id;
	private String patient;
	private String doctor;
	private String diagnose;
	private Set<String> prescriptions;
	
	public MedicalReportVerifyDto() {}

	public MedicalReportVerifyDto(Long id, String patient, String doctor, String diagnose, Set<String> prescriptions) {
		super();
		this.id = id;
		this.patient = patient;
		this.doctor = doctor;
		this.diagnose = diagnose;
		this.prescriptions = prescriptions;
	}
	
	public MedicalReportVerifyDto(MedicalReport mr) {
		this.id = mr.getId();
		Appointment a = mr.getAppointment();
		this.patient = a.getPatient().getName() + " " + a.getPatient().getSurname();
		this.doctor = a.getDoctor().getName() + " " + a.getDoctor().getSurname();
		this.diagnose = mr.getDiagnosis().getName();
		Set<String> prescription = new HashSet<String>();
		for (Prescription p : mr.getPrescriptions()) {
			prescription.add(p.getMedicine());
		}
		this.prescriptions = prescription;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPatient() {
		return patient;
	}

	public void setPatient(String patient) {
		this.patient = patient;
	}

	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public String getDiagnose() {
		return diagnose;
	}

	public void setDiagnose(String diagnose) {
		this.diagnose = diagnose;
	}

	public Set<String> getPrescriptions() {
		return prescriptions;
	}

	public void setPrescriptions(Set<String> prescriptions) {
		this.prescriptions = prescriptions;
	}
}
