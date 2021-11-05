package hr.hsnopek.springjwtrtr.domain.feature.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class RegisterUserRequest {

	private String username;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private Boolean totp;
	
	private Boolean ldapUser;
	
	public RegisterUserRequest() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean getTotp() {
		return totp;
	}

	public void setTotp(Boolean totp) {
		this.totp = totp;
	}

	public Boolean isLdapUser() {
		return ldapUser;
	}

	public void setLdapUser(Boolean ldapUser) {
		this.ldapUser = ldapUser;
	}
	
	
}
