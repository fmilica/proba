package mrs.isa.team12.clinical.center.model;

import static javax.persistence.FetchType.LAZY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Where;

@Entity
@Table(name = "medical_report")
@Where(clause="is_active=true")
public class MedicalReport {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Version
	private Long version;
	
	@Column(name="is_active")
	private Boolean active;
	
	@Column(name = "description", unique = false, nullable = false)
	private String description;
	
	@OneToOne(cascade = {CascadeType.ALL}, fetch = LAZY)
	@JoinColumn(name = "appointment_id")
	private Appointment appointment;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "medical_report_perscription", 
				joinColumns = @JoinColumn(name = "medical_report_id"),
				inverseJoinColumns = @JoinColumn(name = "prescription_id"))
	private Set<Prescription> prescriptions;
	
	// trebalo bi manyToMany, jer moze dati vise dijagnoza jednom pacijentu
	// (moze oboljevati od n stvari, ali neka, imacemo zdrave pacijente)
	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "diagnosis_id", referencedColumnName = "id", nullable = false)
	private Diagnosis diagnosis;
	
	@Column(name = "verified", unique = false, nullable = true)
	private boolean verified;
	
	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "nurse_id", referencedColumnName = "id", nullable = true)
	private Nurse nurse;
	
	public MedicalReport() {}
	
	public MedicalReport(Long id, String description, Set<Prescription> prescriptions, 
			Diagnosis diagnosis, Appointment appointment) {
		super();
		this.id = id;
		this.description = description;
		this.prescriptions = prescriptions;
		this.diagnosis = diagnosis;
		this.appointment = appointment;
		this.active = true;
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Appointment getAppointment() {
		return appointment;
	}
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
	public Set<Prescription> getPrescriptions() {
		return prescriptions;
	}
	public void setPrescriptions(Set<Prescription> prescriptions) {
		this.prescriptions = prescriptions;
	}
	public Diagnosis getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(Diagnosis diagnosis) {
		this.diagnosis = diagnosis;
	}
	
	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public void addPrescription(Prescription p) {
		if(this.prescriptions == null) {
			this.prescriptions = new HashSet<Prescription>();
		}
		if (!this.prescriptions.contains(p)) {
			this.prescriptions.add(p);
		}
	}

	public Nurse getNurse() {
		return nurse;
	}

	public void setNurse(Nurse nurse) {
		this.nurse = nurse;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
}
