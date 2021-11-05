package hr.hsnopek.springjwtrtr.security.authenticationtokens;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class LdapAuthenticationToken extends AbstractAuthenticationToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6787037805594490886L;
	
	private final Object principal;
	private Object credentials;
	
	/**
	 * This constructor can be safely used by any code that wishes to create a
	 * <code>LdapAuthenticationToken</code>, as the {@link #isAuthenticated()}
	 * will return <code>false</code>.
	 *
	 */
	public LdapAuthenticationToken(Object principal, Object credentials) {
		super(null);
		this.principal = principal;
		this.credentials = credentials;
	}

	@Override
	public Object getCredentials() {
		return this.credentials;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		super.setAuthenticated(isAuthenticated);
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		this.credentials = null;
	}

}
