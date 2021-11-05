package hr.hsnopek.springjwtrtr.security.model;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import hr.hsnopek.springjwtrtr.domain.feature.user.entity.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPrincipal implements UserDetails {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -446670097917896040L;
	
	private Collection<? extends GrantedAuthority> authorities;
	private String username;
	private String password;
	private boolean active;
	
	private User user;
	
	public UserPrincipal(String username, Collection<? extends GrantedAuthority> authorities) {
		this.username = username;
        this.authorities = authorities;
        this.password = null;
        this.active = true;
	}
	
	public UserPrincipal(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.active = user.getActive();
        this.authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());
        
        this.user = user;
	}
	
    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
	}

	@Override
	@JsonIgnore
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.active;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.active;
	}

	@JsonIgnore
	public User getUser() {
		return user;
	}
	
}
