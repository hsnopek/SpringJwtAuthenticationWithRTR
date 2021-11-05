package hr.hsnopek.springjwtrtr.domain.feature.authentication.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7944762466407173660L;

	public UserAlreadyExistsException(String errorMessage) {
		super(errorMessage);
	}
}
