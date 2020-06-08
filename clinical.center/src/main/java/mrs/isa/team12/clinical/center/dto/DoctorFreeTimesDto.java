package mrs.isa.team12.clinical.center.dto;

import java.util.List;

import mrs.isa.team12.clinical.center.model.Doctor;

public class DoctorFreeTimesDto {

	private Long id;
	private String name;
	private String surname;
	private Double rating;
	private Integer duration;
	private Double price;
	private List<Integer> availableTimes;
	
	public DoctorFreeTimesDto() {}
	
	public DoctorFreeTimesDto(Doctor doctor, List<Integer> availableTimes) {
		this.id = doctor.getId();
		this.name = doctor.getName();
		this.surname = doctor.getSurname();
		this.rating = doctor.getRating();
		this.availableTimes = availableTimes;
	}
	
	public DoctorFreeTimesDto(Doctor doctor, Integer duration, Double price, List<Integer> availableTimes) {
		this.id = doctor.getId();
		this.name = doctor.getName();
		this.surname = doctor.getSurname();
		this.rating = doctor.getRating();
		this.duration = duration;
		this.price = price;
		this.availableTimes = availableTimes;
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
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public List<Integer> getAvailableTimes() {
		return availableTimes;
	}
	public void setAvailableTimes(List<Integer> availableTimes) {
		this.availableTimes = availableTimes;
	}
}
