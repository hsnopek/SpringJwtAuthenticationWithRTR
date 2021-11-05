package hr.hsnopek.springjwtrtr.general.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class ErrorResponse {

	private String uri;
	private String errorMessage;
	private int errorCode;
	private LocalDateTime timestamp;
	
	public ErrorResponse(String uri, String errorMessage, int errorCode, LocalDateTime timestamp) {
		super();
		this.uri = uri;
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
		this.timestamp = timestamp;
	}
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	

	
	
	
}
