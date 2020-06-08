package mrs.isa.team12.clinical.center.dto;

import java.sql.Date;

public class LeaveDto {
	private Date startDate;
	private Date endDate;
	private String type;
	
	public LeaveDto() {}
	
	public LeaveDto(Date start, Date end, String type) {
		this.startDate = start;
		this.endDate = end;
		this.type = type;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
