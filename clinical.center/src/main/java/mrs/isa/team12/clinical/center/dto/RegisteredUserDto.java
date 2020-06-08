package mrs.isa.team12.clinical.center.dto;

import mrs.isa.team12.clinical.center.model.RegisteredUser;

public class RegisteredUserDto {

	private String name;
	private String surname;
	private Boolean logged;
	
	public RegisteredUserDto(RegisteredUser user) {
		this.name = user.getName();
		this.surname = user.getSurname();
	}
	
	public RegisteredUserDto(RegisteredUser user, Boolean logged) {
		this.name = user.getName();
		this.surname = user.getSurname();
		this.logged = logged;
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

	public Boolean getLogged() {
		return logged;
	}

	public void setLogged(Boolean logged) {
		this.logged = logged;
	}
	
}
