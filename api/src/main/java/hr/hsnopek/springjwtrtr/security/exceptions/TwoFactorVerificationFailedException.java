package hr.hsnopek.springjwtrtr.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class TwoFactorVerificationFailedException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1466234260461469085L;

	public TwoFactorVerificationFailedException(String msg) {
		super(msg);
	}

	public TwoFactorVerificationFailedException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}