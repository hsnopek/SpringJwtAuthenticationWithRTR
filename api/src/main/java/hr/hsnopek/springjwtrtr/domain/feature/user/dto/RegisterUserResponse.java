package hr.hsnopek.springjwtrtr.domain.feature.user.dto;

import org.apache.commons.lang3.StringUtils;

import hr.hsnopek.springjwtrtr.security.util.SecurityUtils;

public class RegisterUserResponse {

	public enum Status {
		OK, USER_EXISTS, WEAK_PASSWORD
	}

	private Status status;
	private String username;
	private String secret;
	private String qrCodeUrl;
	private String qrCode;
	
	public RegisterUserResponse(Status status, String username, String secret) {
		this.status = status;
		this.username = username;
		this.secret = secret;
	}
	
	public RegisterUserResponse(Status status, String username, String secret, String qrCodeUrl) {
		this.status = status;
		this.username = username;
		this.secret = secret;
		this.qrCodeUrl = qrCodeUrl;
	}
	
	public RegisterUserResponse(RegisterUserResponse.Status status, UserDTO userDTO) {
		this.status = status;
		this.username = userDTO.getUsername();
		this.secret = !StringUtils.isBlank(userDTO.getSecret()) ? userDTO.getSecret() : StringUtils.EMPTY;
		this.qrCodeUrl = userDTO.getSecret() != null ? SecurityUtils.generateQrUrl(userDTO.getUsername(), userDTO.getSecret()) : StringUtils.EMPTY;
	}	

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getQrCodeUrl() {
		return qrCodeUrl;
	}

	public void setQrCodeUrl(String qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
	}

}
