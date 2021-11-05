package hr.hsnopek.springjwtrtr.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class CustomAuthenticationException extends AuthenticationException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3263863697612028629L;

	public CustomAuthenticationException(String msg) {
		super(msg);
	}

	public CustomAuthenticationException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}