package mrs.isa.team12.clinical.center.dto;

import mrs.isa.team12.clinical.center.model.RegisteredUser;

public class PatientDto {
	private Long id;
	private String email;
	private String password;
	private String name;
	private String surname;
	private String gender;
	private String dateOfBirth;
	private String address;
	private String city;
	private String country;
	private String phoneNumber;
	private String securityNumber;
	
	public PatientDto() {}
	
	public PatientDto(Long id, String email, String password, String name, String surname, String gender,
			String dateOfBirth, String address, String city, String country, String phoneNumber,
			String securityNumber) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.city = city;
		this.country = country;
		this.phoneNumber = phoneNumber;
		this.securityNumber = securityNumber;
	}
	
	public PatientDto(RegisteredUser p) {
		this.id = p.getId();
		this.email = p.getEmail();
		this.password = p.getPassword();
		this.name = p.getName();
		this.surname = p.getSurname();
		this.gender = p.getGender();
		this.dateOfBirth = p.getDateOfBirth();
		this.address = p.getAddress();
		this.city = p.getCity();
		this.country = p.getCountry();
		this.phoneNumber = p.getPhoneNumber();
		this.securityNumber = p.getSecurityNumber();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
