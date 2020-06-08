package mrs.isa.team12.clinical.center.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "nurse")
public class Nurse extends MedicalPersonnel{
	
	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "nurse")
	private Set<MedicalReport> medicalReports;

	public Nurse() {}

	public Nurse(String email, String password, String name, String suername, String gender,
			String dateOfBirth, String address, String city, String country, 
			String phoneNumber, String securityNumber, Set<Leave> leaveList, Set<Patient> patients, Clinic clinic, Boolean logged) {
		super(email, password, name, suername, gender, dateOfBirth, address, city, country, phoneNumber, securityNumber, leaveList,
				patients, clinic, logged);
	}

	public Set<MedicalReport> getMedicalReports() {
		return medicalReports;
	}

	public void setMedicalReports(Set<MedicalReport> medicalReports) {
		this.medicalReports = medicalReports;
	}
}
