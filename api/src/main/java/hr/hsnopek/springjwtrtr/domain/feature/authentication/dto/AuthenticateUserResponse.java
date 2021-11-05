package hr.hsnopek.springjwtrtr.domain.feature.authentication.dto;

public class AuthenticateUserResponse {

	private String refreshToken;
	private String accessToken;
	
	public AuthenticateUserResponse() {
	}
	
	public AuthenticateUserResponse(String refreshToken, String accessToken) {
		this.refreshToken = refreshToken;
		this.accessToken = accessToken;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	
}
