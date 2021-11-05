package hr.hsnopek.springjwtrtr.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AccessTokenInvalidException extends AuthenticationException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 398388182994891637L;

	public AccessTokenInvalidException(String msg) {
		super(msg);
	}

	public AccessTokenInvalidException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}