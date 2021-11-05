package hr.hsnopek.springjwtrtr.domain.feature.authentication.dto;

public class AuthenticateUserRequest {

	private String username;
	private String password;
	private String userDeviceId;
	
	public AuthenticateUserRequest() {
	}
	
	public AuthenticateUserRequest(String username, String password) {
		this.username = username;
		this.password = password;
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

	public String getUserDeviceId() {
		return userDeviceId;
	}

	public void setUserDeviceId(String userDeviceId) {
		this.userDeviceId = userDeviceId;
	}
	
	
}
