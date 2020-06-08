package mrs.isa.team12.clinical.center.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Where;

@Entity
@Table(name = "clinical_centre")
@Where(clause="is_active=true")
public class ClinicalCentre {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Version
	private Long version;
	
	@Column(name="is_active")
	private Boolean active;
	
	@Column(name = "name", unique = true, nullable = false)
	private String name;
	
	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "clinicalCentre")
	private Set<Clinic> clinics;
	
	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "clinicalCentre")
	private Set<ClinicalCentreAdmin> admins;
	
	@OneToMany(cascade = {ALL}, fetch = LAZY)
	private Set<RegistrationRequest> registrationRequests;
	
	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "clinicalCentre")
	private Set<Patient> patients;
	
	public ClinicalCentre() {}
	
	public ClinicalCentre(String name) {
		super();
		this.name = name;
		this.active = true;
	}
	
	public ClinicalCentre(Long id, String name, Set<Clinic> clinics,
			Set<ClinicalCentreAdmin> admins, Set<RegistrationRequest> registrationRequests, Set<Patient> patients) {
		super();
		this.id = id;
		this.name = name;
		this.clinics = clinics;
		this.admins = admins;
		this.registrationRequests = registrationRequests;
		this.patients = patients;
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
	
	public Set<Clinic> getClinics() {
		return clinics;
	}
	
	public void setClinics(Set<Clinic> clinics) {
		this.clinics = clinics;
	}
	
	public Set<ClinicalCentreAdmin> getAdmins() {
		return admins;
	}
	
	public void setAdmins(Set<ClinicalCentreAdmin> admins) {
		this.admins = admins;
	}
	
	public Set<RegistrationRequest> getRegistrationRequests() {
		return registrationRequests;
	}
	
	public void setRegistrationRequests(Set<RegistrationRequest> registrationRequests) {
		this.registrationRequests = registrationRequests;
	}
	
	public Set<Patient> getPatients() {
		return patients;
	}
	
	public void setPatients(Set<Patient> patients) {
		this.patients = patients;
	}
	
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void add(ClinicalCentreAdmin clinicalCentreAdmin) {
		if(!this.admins.contains(clinicalCentreAdmin)) {
			this.admins.add(clinicalCentreAdmin);
		}
	}
	
	public void addClinic(Clinic clinic) {
		if(!this.clinics.contains(clinic)) {
			this.clinics.add(clinic);
		}
	}
	
	public Clinic getOneClinic(Long id) {
		for (Clinic clinic : clinics) {
			if(clinic.getId() == id) {
				return clinic;
			}
		}
		return null;
	}
	
	public Doctor getDoctor(Long id) {
		for (Clinic clinic : clinics) {
			for (Doctor doctor : clinic.getDoctors()) {
				if(doctor.getId() == id) {
					return doctor;
				}
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "ClinicalCentre [id = "+ id + ", name=" + name + "]";
	}
}
