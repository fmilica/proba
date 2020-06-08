package mrs.isa.team12.clinical.center.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public abstract class MedicalPersonnel extends RegisteredUser {
	
	@OneToMany(cascade = {ALL}, fetch = LAZY)
	private Set<Leave> leaveList;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "medical_personnel_patient", 
				joinColumns = @JoinColumn(name = "medical_personnel_id"),
				inverseJoinColumns = @JoinColumn(name = "patient_id"))
	private Set<Patient> patients;
	
	@ManyToOne
	@JoinColumn(name = "clinic_id", referencedColumnName = "id", nullable = true)
	private Clinic clinic;
	
	public MedicalPersonnel() {}

	public MedicalPersonnel(String email, String password, String name, String suername, String gender,
			String dateOfBirth, String address, String city, String country, 
			String phoneNumber, String securityNumber, Set<Leave> leaveList, Set<Patient> patients, Clinic clinic, Boolean logged) {
		super(email, password, name, suername, gender, dateOfBirth, address, city, country, phoneNumber, securityNumber, logged);
		this.leaveList = leaveList;
		this.patients = patients;
		this.clinic = clinic;
	}

	public Set<Leave> getLeaveList() {
		return leaveList;
	}


	public void setLeaveList(Set<Leave> leaveList) {
		this.leaveList = leaveList;
	}


	public Set<Patient> getPatients() {
		return patients;
	}


	public void setPatients(Set<Patient> patients) {
		this.patients = patients;
	}

	public Clinic getClinic() {
		return clinic;
	}

	public void setClinic(Clinic clinic) {
		this.clinic = clinic;
	}
}
