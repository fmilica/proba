package mrs.isa.team12.clinical.center.dto;

import java.util.List;

public class DoctorsOrdinationsFreeTimesDto {

	private List<DoctorFreeTimesDto> availableDoctors;
	private List<OrdinationFreeTimesDto> availableOrdinations;
	
	public DoctorsOrdinationsFreeTimesDto(List<DoctorFreeTimesDto> availableDoctors,
			List<OrdinationFreeTimesDto> availableOrdinations) {
		this.availableDoctors = availableDoctors;
		this.availableOrdinations = availableOrdinations;
	}
	
	public List<DoctorFreeTimesDto> getAvailableDoctors() {
		return availableDoctors;
	}
	public void setAvailableDoctors(List<DoctorFreeTimesDto> availableDoctors) {
		this.availableDoctors = availableDoctors;
	}
	public List<OrdinationFreeTimesDto> getAvailableOrdinations() {
		return availableOrdinations;
	}
	public void setAvailableOrdinations(List<OrdinationFreeTimesDto> availableOrdinations) {
		this.availableOrdinations = availableOrdinations;
	}
}
