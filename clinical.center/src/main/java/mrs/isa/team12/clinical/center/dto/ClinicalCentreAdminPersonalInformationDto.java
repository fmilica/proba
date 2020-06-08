package mrs.isa.team12.clinical.center.dto;

import mrs.isa.team12.clinical.center.model.ClinicalCentreAdmin;

public class ClinicalCentreAdminPersonalInformationDto {
	
	private Long id;
	private String name;
	private String surname;
	private String email;
	private String gender;
	private String dateOfBirth;
	private String phoneNumber;
	private String securityNumber;
	private String address;
	private String city;
	private String country;
	
	public ClinicalCentreAdminPersonalInformationDto() {}
	
	public ClinicalCentreAdminPersonalInformationDto(ClinicalCentreAdmin clinicalCentreAdmin) {
		this.name = clinicalCentreAdmin.getName();
		this.surname = clinicalCentreAdmin.getSurname();
		this.email = clinicalCentreAdmin.getEmail();
		this.gender = clinicalCentreAdmin.getGender();
		this.dateOfBirth = clinicalCentreAdmin.getDateOfBirth();
		this.phoneNumber = clinicalCentreAdmin.getPhoneNumber();
		this.securityNumber = clinicalCentreAdmin.getSecurityNumber();
		this.address = clinicalCentreAdmin.getAddress();
		this.city = clinicalCentreAdmin.getCity();
		this.country = clinicalCentreAdmin.getCountry();
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
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
}
