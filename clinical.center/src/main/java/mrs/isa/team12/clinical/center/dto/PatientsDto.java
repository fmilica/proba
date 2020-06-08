package mrs.isa.team12.clinical.center.dto;

import mrs.isa.team12.clinical.center.model.Patient;

public class PatientsDto{
	
	private String name;
	private String surname;
	private String securityNumber;
	
	public PatientsDto(Patient patient) {
		this.name = patient.getName();
		this.surname = patient.getSurname();
		this.securityNumber = patient.getSecurityNumber();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getSecurityNumber() {
		return securityNumber;
	}
	public void setSecurityNumber(String securityNumber) {
		this.securityNumber = securityNumber;
	}

	@Override
	public String toString() {
		return "ViewPatientsDto [name=" + name + ", surname=" + surname + ", securityNumber=" + securityNumber + "]";
	}
	
	
}
