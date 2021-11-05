package hr.hsnopek.springjwtrtr.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class IpAddressNotValidException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3009849214581467565L;

	public IpAddressNotValidException(String msg) {
		super(msg);
	}

	public IpAddressNotValidException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}