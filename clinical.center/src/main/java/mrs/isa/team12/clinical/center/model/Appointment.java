package mrs.isa.team12.clinical.center.model;

import static javax.persistence.FetchType.LAZY;

import java.sql.Date;
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

import mrs.isa.team12.clinical.center.model.enums.OrdinationType;

@Entity
@Table(name = "appointment")
@Where(clause="is_active=true")
public class Appointment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Version
	private Long version;
	
	@Column(name="is_active")
	private Boolean active;
	
	@Column(name = "app_finished", unique = false, nullable = false)
	private Boolean finished;
	
	@Column(name = "app_date", unique = false, nullable = false )
	//@Temporal(TemporalType.DATE)
	private Date date;
	
	@Column(name = "app_start_time", unique = false, nullable = true )
	private Integer startTime;
	
	@Column(name = "app_end_time", unique = false, nullable = true)
	private Integer endTime;
	
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name= "app_type", referencedColumnName = "id", nullable = true)
	private AppointmentType appType;
	
	@Column(name = "discount", unique = false, nullable = true )
	private Double discount;
	
	@Column(name = "confirmed", unique = false, nullable = false )
	private Boolean confirmed;

	@ManyToOne
	@JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = true)
	private Patient patient;
	
	@OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "medical_report_id")
	private MedicalReport medicalReport;
/*
	@ManyToOne
	@JoinColumn(name= "medical_record_id", referencedColumnName = "id", nullable = true)
	private MedicalRecords medicalRecords;
*/
	@ManyToOne
	@JoinColumn(name="ordination_id", referencedColumnName = "id", nullable = true)
	private Ordination ordination;
	
	@ManyToOne
	@JoinColumn(name = "clinic_id", referencedColumnName = "id", nullable = false)
	private Clinic clinic;
	
	@ManyToOne
	@JoinColumn(name = "doctor_id", referencedColumnName = "id", nullable = true)
	private Doctor doctor;
	
	//dodato zbog operacija
	@ManyToMany
	@JoinTable(
	        name = "appointment_doctors", 
	        joinColumns = { @JoinColumn(name = "appointment_id") }, 
	        inverseJoinColumns = { @JoinColumn(name = "doctor_id") }
	    )
	private Set<Doctor> doctors;
	
	@Column(name = "ordination_type", unique = false, nullable = true)
	private OrdinationType type;

	//OVO SAM DODALA JA KSENIJA DA PROBAM DA OBRISEM APPOINTMENT I APPOINTMENT REQUEST
	@OneToOne(fetch = LAZY, cascade =  CascadeType.REMOVE, orphanRemoval = true)
	@JoinColumn(name = "appointment_request_id")
	private AppointmentRequest appointmentRequest;
	
	public Appointment() {}

	public Appointment(Date date, Integer startTime, AppointmentType type,
			Boolean confirmed, Boolean finished, Doctor doctor, Clinic clinic,
			Patient patient) {
		this.date = date;
		this.startTime = startTime;
		this.endTime = startTime + type.getDuration();
		this.appType = type;
		this.confirmed = confirmed;
		this.finished = finished;
		this.doctor = doctor;
		this.clinic = clinic;
		this.patient = patient;
		this.active = true;
	}

	public Appointment(Date date, Integer startTime, AppointmentType type, Double discount,
			Boolean confirmed, Boolean finished, Ordination ordination, Doctor doctor, Clinic clinic) {
		this.date = date;
		this.startTime = startTime;
		this.endTime = startTime + type.getDuration();
		this.appType = type;
		this.discount = discount;
		this.confirmed = confirmed;
		this.finished = finished;
		this.ordination = ordination;
		this.doctor = doctor;
		this.clinic = clinic;
		this.active = true;
	}
	
	public Appointment(Date date, Integer startTime, AppointmentType type, Double discount,
			Boolean confirmed, Patient patient, MedicalReport medicalReport, Ordination ordination, Doctor doctor) {
		super();
		this.date = date;
		this.startTime = startTime;
		this.endTime = startTime + type.getDuration();
		this.appType = type;
		this.discount = discount;
		this.confirmed = confirmed;
		this.patient = patient;
		this.medicalReport = medicalReport;
		this.ordination = ordination;
		this.doctor = doctor;
		this.active = true;
	}
	
	public Set<Doctor> getDoctors() {
		return doctors;
	}

	public void setDoctors(Set<Doctor> doctors) {
		this.doctors = doctors;
	}

	public OrdinationType getType() {
		return type;
	}

	public void setType(OrdinationType type) {
		this.type = type;
	}
	
	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getStartTime() {
		return startTime;
	}

	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}

	public Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}

	public AppointmentType getAppType() {
		return appType;
	}

	public void setAppType(AppointmentType appType) {
		this.appType = appType;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public MedicalReport getMedicalReport() {
		return medicalReport;
	}

	public void setMedicalReport(MedicalReport medicalReport) {
		this.medicalReport = medicalReport;
	}

	public Ordination getOrdination() {
		return ordination;
	}

	public void setOrdination(Ordination ordination) {
		this.ordination = ordination;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public AppointmentRequest getAppointmentRequest() {
		return appointmentRequest;
	}

	public void setAppointmentRequest(AppointmentRequest appointmentRequest) {
		this.appointmentRequest = appointmentRequest;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	/*
	public MedicalRecords getMedicalRecords() {
		return medicalRecords;
	}

	public void setMedicalRecords(MedicalRecords medicalRecords) {
		this.medicalRecords = medicalRecords;
	}
*/
	public void addDoctor(Doctor d) {
		this.doctors.add(d);
	}
	
	@Override
	public String toString() {
		return "Appointment [id=" + id + "]";
	}
	
}
