package hr.hsnopek.springjwtrtr.general.exceptions;

public class UserRestrictedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7845579254138862024L;

	public UserRestrictedException(String errorMessage) {
		super(errorMessage);
	}
}
