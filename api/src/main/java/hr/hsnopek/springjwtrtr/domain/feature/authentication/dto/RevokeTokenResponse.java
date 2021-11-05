package hr.hsnopek.springjwtrtr.domain.feature.authentication.dto;

public class RevokeTokenResponse {

	private String token;
	private boolean revoked;
	
	public RevokeTokenResponse(String token, boolean revoked) {
		super();
		this.token = token;
		this.revoked = revoked;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public boolean isRevoked() {
		return revoked;
	}
	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}

	
	
}
