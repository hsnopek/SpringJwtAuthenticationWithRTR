package hr.hsnopek.springjwtrtr.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AccountInactiveException extends AuthenticationException {


	/**
	 * 
	 */
	private static final long serialVersionUID = -9172657272766557986L;

	public AccountInactiveException(String msg) {
		super(msg);
	}

	public AccountInactiveException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}