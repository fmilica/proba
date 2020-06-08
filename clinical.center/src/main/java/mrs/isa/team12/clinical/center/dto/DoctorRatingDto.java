package mrs.isa.team12.clinical.center.dto;

import mrs.isa.team12.clinical.center.model.Doctor;

public class DoctorRatingDto {
	
	private String fullName;
	private Double rating;
	
	public DoctorRatingDto() {}
	
	public DoctorRatingDto(Doctor doctor) {
		this.fullName = doctor.getName() + doctor.getSurname();
		this.rating = doctor.getRating();
	}
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
}
