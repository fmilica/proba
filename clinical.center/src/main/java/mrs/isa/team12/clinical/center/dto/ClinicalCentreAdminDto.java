package mrs.isa.team12.clinical.center.dto;

import mrs.isa.team12.clinical.center.model.ClinicalCentreAdmin;

public class ClinicalCentreAdminDto {

	private String email;
	private String name;
	private String surname;
	private String gender;
	private String dateOfBirth;
	private String address;
	private String city;
	private String country;
	private String phoneNumber;
	private String securityNumber;
	
	public ClinicalCentreAdminDto() {}
	
	public ClinicalCentreAdminDto(ClinicalCentreAdmin centreAdmin) {
		this.email = centreAdmin.getEmail();
		this.name = centreAdmin.getName();
		this.surname = centreAdmin.getSurname();
		this.gender = centreAdmin.getGender();
		this.dateOfBirth = centreAdmin.getDateOfBirth();
		this.address = centreAdmin.getAddress();
		this.city = centreAdmin.getCity();
		this.country = centreAdmin.getCountry();
		this.phoneNumber = centreAdmin.getPhoneNumber();
		this.securityNumber = centreAdmin.getSecurityNumber();
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getSecurityNumber() {
		return securityNumber;
	}
	public void setSecurityNumber(String securityNumber) {
		this.securityNumber = securityNumber;
	}
}
