package hr.hsnopek.springjwtrtr.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class RefreshTokenInvalidException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6472178485161523397L;

	public RefreshTokenInvalidException(String msg) {
		super(msg);
	}

	public RefreshTokenInvalidException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}