package mrs.isa.team12.clinical.center.dto;

import java.util.List;

import mrs.isa.team12.clinical.center.model.Ordination;

public class ExamRoomDto {

	private Long id;
	private String name;
	private Integer ordinationNumber;
	private String date;
	private List<Integer> availableTimes;
	
	public ExamRoomDto(Ordination ordination, String date, List<Integer> availableTimes) {
		this.id = ordination.getId();
		this.name = ordination.getName();
		this.ordinationNumber = ordination.getOrdinationNumber();
		this.date = date;
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
	public Integer getOrdinationNumber() {
		return ordinationNumber;
	}
	public void setOrdinationNumber(Integer ordinationNumber) {
		this.ordinationNumber = ordinationNumber;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<Integer> getAvailableTimes() {
		return availableTimes;
	}
	public void setAvailableTimes(List<Integer> availableTimes) {
		this.availableTimes = availableTimes;
	}
}
