package mrs.isa.team12.clinical.center.dto;

import mrs.isa.team12.clinical.center.model.Doctor;
import mrs.isa.team12.clinical.center.model.enums.Specialization;

public class DoctorDto {
	
	private Long id;
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
	private Specialization specialization;
	private Integer startWork;
	private Integer endWork;
	private Double rating;
	
	public DoctorDto(Doctor doctor) {
		this.id = doctor.getId();
		this.email = doctor.getEmail();
		this.name = doctor.getName();
		this.surname = doctor.getSurname();
		this.gender = doctor.getGender();
		this.dateOfBirth = doctor.getDateOfBirth();
		this.address = doctor.getAddress();
		this.city = doctor.getCity();
		this.country = doctor.getCountry();
		this.phoneNumber = doctor.getPhoneNumber();
		this.securityNumber = doctor.getSecurityNumber();
		this.specialization = doctor.getSpecialization();
		this.startWork = doctor.getStartWork();
		this.endWork = doctor.getEndWork();
		this.rating = doctor.getRating();
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
	public Specialization getSpecialization() {
		return specialization;
	}
	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Integer getStartWork() {
		return startWork;
	}
	public void setStartWork(Integer startWork) {
		this.startWork = startWork;
	}
	public Integer getEndWork() {
		return endWork;
	}
	public void setEndWork(Integer endWork) {
		this.endWork = endWork;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}
}
