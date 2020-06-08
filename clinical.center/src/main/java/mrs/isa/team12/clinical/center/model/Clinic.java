package mrs.isa.team12.clinical.center.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "clinic")
@Where(clause="is_active=true")
public class Clinic {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Version
	private Long version;
	
	@Column(name="is_active")
	private Boolean active;
	
	@Column(name="name", unique=true, nullable=false)
	private String name;
	
	@Column(name="address", unique=false, nullable=false)
	private String address;
	
	@Column(name="city", unique=false, nullable=false)
	private String city;
	
	@Column(name="country", unique=false, nullable=false)
	private String country;
	
	@Column(name="description", unique=false, nullable=true)
	private String description;
	
	@Column(name="rating", unique=false, nullable=true)
	private Double rating;

	@ManyToOne
	@JoinColumn(name = "clinical_center_id", referencedColumnName = "id", nullable = false)
	private ClinicalCentre clinicalCentre;
	
	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "report_id")
	@JsonBackReference("clinic-report")
	private Report report;
	
	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "diagnose_perscription_id")
	// NEMA U KONTRA SMERU
	private DiagnosePerscription diagnosePerscription;
	
	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "clinic")
	private Set<Doctor> doctors;
	
	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "clinic")
	private Set<Nurse> nurses;
	
	@OneToMany(cascade = {ALL}, fetch = LAZY)
	// NEMA VEZU U KONTRA SMERU
	private Set<Patient> patients;
	
	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "clinic")
	private Set<AppointmentType> appointmentTypes;
	
	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "clinic")
	private Set<Appointment> appointments;
	
	@OneToMany(cascade={ALL}, fetch=LAZY, mappedBy="clinic")
	private Set<ClinicAdmin> admins;
	
	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "clinic")
	private Set<Ordination> ordinations;
	
	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "clinic")
	private Set<AppointmentRequest> appointmentRequests;
	
	@OneToMany(cascade = {ALL}, fetch = LAZY)
	// NEMA VEZU OD STRANE LEAVE REQUSEST KA OVAMO
	private Set<LeaveRequest> leaveRequests;
	
	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "clinic")
	private Set<Rating> ratings;
	
	public void add(ClinicAdmin clinicAdmin) {
		/*if (clinicAdmin.getClinic() != null) {
			clinicAdmin.getClinic().getAdmins().remove(clinicAdmin);
		}*/
		clinicAdmin.setClinic(this);
		this.getAdmins().add(clinicAdmin);
	}

	public Clinic() {}
	

	public Clinic(String name) {
		super();
		this.name = name;
		this.active = true;
	}

	public Clinic(Long id, String name, String address, String city, String country, String description, Double rating,
			ClinicalCentre clinicalCentre, Report report,
			DiagnosePerscription diagnosePerscription, Set<Doctor> doctors, Set<Nurse> nurses, Set<Patient> patients,
			Set<AppointmentType> appointmentTypes, Set<Appointment> appointments, Set<ClinicAdmin> admins, 
			Set<Ordination> ordinations, Set<AppointmentRequest> appointmentRequests, 
			Set<LeaveRequest> leaveRequests, Set<Rating> ratings) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.city = city;
		this.country = country;
		this.description = description;
		this.rating = rating;
		this.clinicalCentre = clinicalCentre;
		this.report = report;
		this.diagnosePerscription = diagnosePerscription;
		this.doctors = doctors;
		this.nurses = nurses;
		this.patients = patients;
		this.appointmentTypes = appointmentTypes;
		this.appointments = appointments;
		this.admins = admins;
		this.ordinations = ordinations;
		this.appointmentRequests = appointmentRequests;
		this.leaveRequests = leaveRequests;
		this.ratings = ratings;
		this.active = true;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	public ClinicalCentre getClinicalCentre() {
		return clinicalCentre;
	}
	public void setClinicalCentre(ClinicalCentre clinicalCentre) {
		this.clinicalCentre = clinicalCentre;
	}
	public Report getReport() {
		return report;
	}
	public void setReport(Report report) {
		this.report = report;
	}
	public DiagnosePerscription getDiagnosePerscription() {
		return diagnosePerscription;
	}
	public void setDiagnosePerscription(DiagnosePerscription diagnosePerscription) {
		this.diagnosePerscription = diagnosePerscription;
	}
	public Set<Doctor> getDoctors() {
		return doctors;
	}
	public void setDoctors(Set<Doctor> doctors) {
		this.doctors = doctors;
	}
	public Set<Nurse> getNurses() {
		return nurses;
	}
	public void setNurses(Set<Nurse> nurses) {
		this.nurses = nurses;
	}
	public Set<Patient> getPatients() {
		return patients;
	}
	public void setPatients(Set<Patient> patients) {
		this.patients = patients;
	}
	public Set<AppointmentType> getAppointmentTypes() {
		return appointmentTypes;
	}
	public void setAppointmentTypes(Set<AppointmentType> appointmentTypes) {
		this.appointmentTypes = appointmentTypes;
	}
	public Set<Appointment> getAppointments() {
		return appointments;
	}
	public void setAppointments(Set<Appointment> appointments) {
		this.appointments = appointments;
	}
	public Set<ClinicAdmin> getAdmins() {
		return admins;
	}
	public void setAdmins(Set<ClinicAdmin> admins) {
		this.admins = admins;
	}
	public Set<Ordination> getOrdinations() {
		return ordinations;
	}
	public void setOrdinations(Set<Ordination> ordinations) {
		this.ordinations = ordinations;
	}
	public Set<AppointmentRequest> getAppointmentRequests() {
		return appointmentRequests;
	}
	public void setAppointmentRequests(Set<AppointmentRequest> appointmentRequests) {
		this.appointmentRequests = appointmentRequests;
	}
	public Set<LeaveRequest> getLeaveRequests() {
		return leaveRequests;
	}
	public void setLeaveRequests(Set<LeaveRequest> leaveRequests) {
		this.leaveRequests = leaveRequests;
	}
	public Set<Rating> getRatings() {
		return ratings;
	}
	public void setRatings(Set<Rating> ratings) {
		this.ratings = ratings;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}

	public void addAppType(AppointmentType appType) {
		if(!this.appointmentTypes.contains(appType)) {
			this.appointmentTypes.add(appType);
		}
		if(appType.getClinic() != this) {
			appType.setClinic(this);
		}
	}
	public Set<ClinicAdmin> getClinicAdmins(){
		return this.admins;
	}
	
	public void addAppointmentRequest(AppointmentRequest ar) {
		ar.setClinic(this);
		this.appointmentRequests.add(ar);
	}
	
	public void addAppointment(Appointment a) {
		a.setClinic(this);
		this.appointments.add(a);
	}
	
	public AppointmentType getOneAppointmentType(String name) {
		for (AppointmentType ap : appointmentTypes) {
			if(ap.getName().equals(name)) {
				return ap;
			}
		}
		return null;
	}

	public List<Appointment> getAvailableAppointments() {
		List<Appointment> available = new ArrayList<Appointment>();
		for(Appointment a: appointments) {
			if (a.getPatient() == null) {
				available.add(a);
			}
		}
		return available;
	}
	
	@Override
	public String toString() {
		return "Clinic [id=" + id + ", name=" + name + ", address=" + address + ", city=" + city + "]";
	}
	
}
