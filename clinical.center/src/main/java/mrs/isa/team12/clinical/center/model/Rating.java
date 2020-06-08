package mrs.isa.team12.clinical.center.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Where;

@Entity
@Table(name = "rating")
@Where(clause="is_active=true")
public class Rating {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Version
	private Long version;
	
	@Column(name="is_active")
	private Boolean active;
	
	@Column(name = "rated", unique = false, nullable = false )
	private int rated;
	
	@ManyToOne
	@JoinColumn(name="doctor_id", referencedColumnName = "id", nullable = true)
	private Doctor doctor;
	
	@ManyToOne
	@JoinColumn(name="clinic_id", referencedColumnName = "id", nullable = true)
	private Clinic clinic;
	
	@ManyToOne
	@JoinColumn(name="patient_id", referencedColumnName = "id", nullable = true)
	private Patient patient;
	
	@ManyToMany(cascade = {ALL}, fetch = LAZY)
	@JoinColumn(name = "report_id", referencedColumnName = "id", nullable = true)
	private Set<Report> reports;
	
	public Rating() {}

	public Rating(Long id, int rated, Doctor doctor, Clinic clinic, Patient patient, Set<Report> reports) {
		super();
		this.id = id;
		this.rated = rated;
		this.doctor = doctor;
		this.clinic = clinic;
		this.patient = patient;
		this.reports = reports;
		this.active = true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getRated() {
		return rated;
	}

	public void setRated(int rated) {
		this.rated = rated;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public Clinic getClinic() {
		return clinic;
	}

	public void setClinic(Clinic clinic) {
		this.clinic = clinic;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Set<Report> getReports() {
		return reports;
	}

	public void setReports(Set<Report> reports) {
		this.reports = reports;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	
}
