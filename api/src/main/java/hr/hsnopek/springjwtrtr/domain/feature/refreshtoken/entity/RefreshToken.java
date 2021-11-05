package hr.hsnopek.springjwtrtr.domain.feature.refreshtoken.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.NaturalId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import hr.hsnopek.springjwtrtr.domain.feature.userdevice.entity.UserDevice;

@Entity(name = "refresh_token")
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @Column(name = "refresh_token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "refresh_token", nullable = false, unique = true)
    @NaturalId(mutable = true)
    private String refreshToken;

    @OneToOne
    @JoinColumn(name = "user_device_id", unique = true)
    @JsonBackReference
    private UserDevice userDevice;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;
    
    @Column(name = "revoked", nullable = true)
    private Boolean revoked;
    
    @Column(name = "logged_in", nullable = true)
    private Boolean loggedIn;
    
    @Column(name = "replaced_by", nullable = true)
    private Long replacedBy;
    
    @Column(name = "jti", nullable = false)
    private String jti;


    public RefreshToken() {
    }

    public RefreshToken(Long id, String token, UserDevice userDevice, Instant expiryDate) {
        this.id = id;
        this.refreshToken = token;
        this.userDevice = userDevice;
        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public UserDevice getUserDevice() {
        return userDevice;
    }

    public void setUserDevice(UserDevice userDevice) {
        this.userDevice = userDevice;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

	public Boolean getRevoked() {
		return revoked;
	}

	public void setRevoked(Boolean revoked) {
		this.revoked = revoked;
	}
	
	public Boolean getLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(Boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean isExpired() {
		return Instant.now().isAfter(expiryDate);
	}

    public boolean isActive() {
    	return !revoked && !isExpired();
    }

    @JsonIgnore
	public Long getReplacedBy() {
		return replacedBy;
	}
	public void setReplacedBy(Long replacedBy) {
		this.replacedBy = replacedBy;
	}

	public String getJti() {
		return jti;
	}

	public void setJti(String jti) {
		this.jti = jti;
	}
    
    
}
