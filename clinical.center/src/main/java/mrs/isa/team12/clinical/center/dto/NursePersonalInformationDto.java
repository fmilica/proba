package mrs.isa.team12.clinical.center.dto;

import mrs.isa.team12.clinical.center.model.Nurse;

public class NursePersonalInformationDto {

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
	
	public NursePersonalInformationDto() {}

	public NursePersonalInformationDto(Nurse nurse) {
		this.name = nurse.getName();
		this.surname = nurse.getSurname();
		this.email = nurse.getEmail();
		this.gender = nurse.getGender();
		this.dateOfBirth = nurse.getDateOfBirth();
		this.phoneNumber = nurse.getPhoneNumber();
		this.securityNumber = nurse.getSecurityNumber();
		this.address = nurse.getAddress();
		this.city = nurse.getCity();
		this.country = nurse.getCountry();
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
