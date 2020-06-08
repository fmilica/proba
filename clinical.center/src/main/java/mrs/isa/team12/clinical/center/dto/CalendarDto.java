package mrs.isa.team12.clinical.center.dto;

import java.util.List;

public class CalendarDto {
	private List<LeaveDto> leaves;
	private List<DoctorsAppointmentDto> appointments;
	private String user;
	
	public CalendarDto() {}
	
	public CalendarDto(List<LeaveDto> leaves, List<DoctorsAppointmentDto> appointments, String user) {
		this.leaves = leaves;
		this.appointments= appointments;
		this.user = user;
	}

	public List<LeaveDto> getLeaves() {
		return leaves;
	}

	public void setLeaves(List<LeaveDto> leaves) {
		this.leaves = leaves;
	}

	public List<DoctorsAppointmentDto> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<DoctorsAppointmentDto> appointments) {
		this.appointments = appointments;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
