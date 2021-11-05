package hr.hsnopek.springjwtrtr.domain.feature.authentication.exceptions;

public class WeakPasswordException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2328766196472330792L;

	public WeakPasswordException(String errorMessage) {
		super(errorMessage);
	}
}
