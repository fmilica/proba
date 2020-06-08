package mrs.isa.team12.clinical.center.dto;

import java.sql.Date;
import java.util.List;

public class AppointmentReqDto {
	private Long reqId;
	private Long ordId;
	private Date date;
	private Integer time;
	private List<Long> doctors;
	
	public AppointmentReqDto() {}

	public AppointmentReqDto(Long reqId, Long ordId, Date date, Integer time) {
		super();
		this.reqId = reqId;
		this.ordId = ordId;
		this.date = date;
		this.time = time;
	}
	
	public AppointmentReqDto(Long reqId, Long ordId, Date date, Integer time, List<Long> doctors) {
		super();
		this.reqId = reqId;
		this.ordId = ordId;
		this.date = date;
		this.time = time;
		this.doctors = doctors;
	}

	public Long getReqId() {
		return reqId;
	}

	public void setReqId(Long reqId) {
		this.reqId = reqId;
	}

	public Long getOrdId() {
		return ordId;
	}

	public void setOrdId(Long ordId) {
		this.ordId = ordId;
	}

	@Override
	public String toString() {
		return "AppointmentReqDto [reqId=" + reqId + ", ordId=" + ordId + ", date=" + date + ", time=" + time + "]";
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public List<Long> getDoctors() {
		return doctors;
	}

	public void setDoctors(List<Long> doctors) {
		this.doctors = doctors;
	}
}
