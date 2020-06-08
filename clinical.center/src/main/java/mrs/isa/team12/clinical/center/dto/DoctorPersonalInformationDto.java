package mrs.isa.team12.clinical.center.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mrs.isa.team12.clinical.center.model.AppointmentType;
import mrs.isa.team12.clinical.center.model.Doctor;
import mrs.isa.team12.clinical.center.model.enums.Specialization;

public class DoctorPersonalInformationDto {
	
	private Long id;
	private String name;
	private String surname;
	private String email;
	private String gender;
	private String dateOfBirth;
	private String phoneNumber;
	private String securityNumber;
	private Integer startWork;
	private Integer endWork;
	private String address;
	private String city;
	private String country;
	private Specialization specialization;
	private List<String> appTypes;
	private List<String> allAppTypes;
	
	public DoctorPersonalInformationDto() {}
	
	public DoctorPersonalInformationDto(Doctor doctor) {
		this.name = doctor.getName();
		this.surname = doctor.getSurname();
		this.email = doctor.getEmail();
		this.gender = doctor.getGender();
		this.dateOfBirth = doctor.getDateOfBirth();
		this.startWork = doctor.getStartWork();
		this.endWork = doctor.getEndWork();
		this.phoneNumber = doctor.getPhoneNumber();
		this.securityNumber = doctor.getSecurityNumber();
		this.specialization = doctor.getSpecialization();
		this.address = doctor.getAddress();
		this.city = doctor.getCity();
		this.country = doctor.getCountry();
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
	public Specialization getSpecialization() {
		return specialization;
	}
	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
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
	public List<String> getAppTypes() {
		return appTypes;
	}
	public void setAppTypes(List<String> appTypes) {
		this.appTypes = appTypes;
	}
	public void setAppTypesSet(Set<AppointmentType> appTypes) {
		this.appTypes = new ArrayList<String>();
		for(AppointmentType at : appTypes) {
			this.appTypes.add(at.getName());
		}
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<String> getAllAppTypes() {
		return allAppTypes;
	}
	public void setAllAppTypesSet(Set<AppointmentType> allAppTypes) {
		this.allAppTypes = new ArrayList<String>();
		for(AppointmentType at : allAppTypes) {
			this.allAppTypes.add(at.getName());
		}
	}
}
