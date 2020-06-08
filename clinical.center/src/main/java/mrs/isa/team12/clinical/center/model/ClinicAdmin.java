package mrs.isa.team12.clinical.center.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "clinic_admin")
public class ClinicAdmin extends RegisteredUser {
	
	/*Treba da bude false*/
	@ManyToOne
	@JoinColumn(name="clinic_id", referencedColumnName="id", nullable=false)
	private Clinic clinic;

	public ClinicAdmin() {}

	public ClinicAdmin(String email, String password, String name, String surname, String gender, 
			String dateOfBirth, String address, String city, String country, 
			String phoneNumber, String securityNumber, Boolean logged) {
		super(email, password, name, surname, gender, dateOfBirth, address, city, country, phoneNumber, securityNumber, logged);
	}
	
	public ClinicAdmin(String email, String password, String name, String surname, String gender,
			String dateOfBirth, String address, String city, String country, 
			String phoneNumber, String securityNumber, Clinic clinic, Boolean logged) {
		super(email, password, name, surname, gender, dateOfBirth, address, city, country, phoneNumber, securityNumber, logged);
		this.clinic = clinic;
	}
	
	public Clinic getClinic() {
		return clinic;
	}
	public void setClinic(Clinic clinic) {
		this.clinic = clinic;
	}

	@Override
	public String toString() {
		return "ClinicAdmin [clinic=" + clinic + "]";
	}

}
