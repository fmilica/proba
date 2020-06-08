package mrs.isa.team12.clinical.center.model;

import static javax.persistence.FetchType.LAZY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Where;

@Entity
@Table(name = "medical_records")
@Where(clause="is_active=true")
public class MedicalRecords {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Version
	private Long version;
	
	@Column(name="is_active")
	private Boolean active;
	
	@Column(name = "height", unique = false, nullable = false)
	private Integer height;
	
	@Column(name = "weight", unique = false, nullable = false)
	private Integer weight;
	
	@Column(name = "blood_pressure", unique = false, nullable = false)
	private String bloodPressure;
	
	@Column(name = "blood_type", unique = false, nullable = false)
	private String bloodType;
	
	@Column(name = "allergies", unique = false, nullable = false)
	private String allergies;
	
	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "patient_id")
	private Patient patient;
/*	
	@OneToMany(cascade = {ALL}, fetch = LAZY, orphanRemoval = true)
	@JoinColumn(name = "medical_record_id")
	private Set<Appointment> appointments;
*/	
	public MedicalRecords() {}

	public MedicalRecords(Integer height, Integer weight, String bloodPressure, String bloodType, String allergies, Patient patient/*, Set<Appointment> appointments*/) {
		super();
		this.height = height;
		this.weight = weight;
		this.bloodPressure = bloodPressure;
		this.bloodType = bloodType;
		this.allergies = allergies;
		this.patient = patient;
		this.active = true;
		//this.appointments = appointments;
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
	
	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
/*
	public Set<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(Set<Appointment> appointments) {
		this.appointments = appointments;
	}
*/
}
