package mrs.isa.team12.clinical.center.dto;

import mrs.isa.team12.clinical.center.model.AppointmentType;

public class AppointmentTypeDto {
	
	private Long id;
	private String name;
	private Double price;
	private Integer duration;
	
	public AppointmentTypeDto() {}

	public AppointmentTypeDto(AppointmentType appType) {
		this.id = appType.getId();
		this.name = appType.getName();
		this.price = appType.getPrice();
		this.duration = appType.getDuration();
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
}
