package mrs.isa.team12.clinical.center.dto;

import java.util.List;

import mrs.isa.team12.clinical.center.model.Ordination;
import mrs.isa.team12.clinical.center.model.enums.OrdinationType;

public class OrdinationFreeTimesDto {

	private Long id;
	private String name;
	private Integer ordinationNumber;
	private OrdinationType type;
	private List<Integer> availableTimes;
	
	public OrdinationFreeTimesDto(Ordination o, List<Integer> availableTimes) {
		this.id = o.getId();
		this.name = o.getName();
		this.ordinationNumber = o.getOrdinationNumber();
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
	public OrdinationType getType() {
		return type;
	}
	public void setType(OrdinationType type) {
		this.type = type;
	}
	public List<Integer> getAvailableTimes() {
		return availableTimes;
	}
	public void setAvailableTimes(List<Integer> availableTimes) {
		this.availableTimes = availableTimes;
	}
}
