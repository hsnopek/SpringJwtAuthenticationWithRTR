
package hr.hsnopek.springjwtrtr.domain.feature.user.entity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import hr.hsnopek.springjwtrtr.domain.feature.role.entity.Role;
import hr.hsnopek.springjwtrtr.domain.feature.userdevice.entity.UserDevice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

@Entity(name = "user_")
@Table(name = "user_")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "active", nullable = false)
    private Boolean active;
    
    @Column(name = "secret", nullable = true)
    private String secret;
    
    @Column(name = "verification_code", nullable = true)
    private String verificationCode;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name = "USER_ROLE", 
    	joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "user_id")}, 
    	inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "role_id")})
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<UserDevice> userDevices = new ArrayList<>();
    
    public User() {}
    
    public User(User user) {
    	this.id = user.getId();
    	this.email = user.getEmail();
    	this.username = user.getUsername();
    	this.password = user.getPassword();
    	this.firstName = user.getFirstName();
    	this.lastName = user.getLastName();
    	this.active = user.getActive();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    
    @JsonIgnore
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @JsonIgnore
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public List<UserDevice> getUserDevices() {
		return userDevices;
	}

	public void setUserDevices(List<UserDevice> userDevices) {
		this.userDevices = userDevices;
	}
	
	@JsonIgnore
	public boolean totpEnabled() {
		return !StringUtils.isBlank(secret);
	}
}
