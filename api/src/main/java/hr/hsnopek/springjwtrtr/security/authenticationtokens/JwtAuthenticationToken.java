package hr.hsnopek.springjwtrtr.security.authenticationtokens;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import hr.hsnopek.springjwtrtr.security.providers.JwtAuthenticationProvider;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8441647194432178255L;
	
	private final Object principal;
	private final String token;

	/**
	 * Use this constructor to return fully authenticated token in {@link JwtAuthenticationProvider}.
	 * 
	 * @param principal UserPrincipal object created from JWT
	 * @param authorities Authories parsed from JWT
	 */
    public JwtAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities){
        super(authorities);
		this.principal = principal;
		this.token = null;
    }

	/**
	 * This constructor can be safely used by any code that wishes to create a
	 * <code>JwtAuthenticationToken</code>, as the {@link #isAuthenticated()}
	 * will return <code>false</code>.
	 * @param token JWT Bearer token
	 * 
	 */
    public JwtAuthenticationToken(String token){
    	super(null);
		this.principal = null;
        this.token = token;
        super.setDetails(null);
		setAuthenticated(false);
    }

    @Override
    public Object getPrincipal() {
		return this.token == null ? this.principal : token;
    }

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return super.getAuthorities();
	}

	@Override
	public String getName() {
		return super.getName();
	}

	@Override
	public boolean isAuthenticated() {
		return super.isAuthenticated();
	}

	@Override
	public Object getCredentials() {
		return null;
	}
	@Override
	public Object getDetails() {
		return super.getDetails();
	}
}
