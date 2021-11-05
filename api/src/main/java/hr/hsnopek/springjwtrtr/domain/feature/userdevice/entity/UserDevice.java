package hr.hsnopek.springjwtrtr.domain.feature.userdevice.entity;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import hr.hsnopek.springjwtrtr.domain.feature.refreshtoken.entity.RefreshToken;
import hr.hsnopek.springjwtrtr.domain.feature.user.entity.User;

@Entity(name = "user_device")
@Table(name = "user_device")
public class UserDevice {

    @Id
    @Column(name = "user_device_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JsonBackReference
    private User user;

    @Column(name = "DEVICE_ID", nullable = false)
    private String deviceId;

    @OneToMany(mappedBy = "userDevice", cascade = CascadeType.ALL)
    @Where(clause = "revoked = false")
    @JsonManagedReference
    private List<RefreshToken> refreshTokens;
    
    public UserDevice() {
    }

    public UserDevice(Long id, User user, String deviceId, List<RefreshToken> refreshTokens) {
        this.id = id;
        this.user = user;
        this.deviceId = deviceId;
        this.refreshTokens = refreshTokens;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<RefreshToken> getRefreshTokens() {
        return refreshTokens;
    }

    public void setRefreshTokens(List<RefreshToken> refreshToken) {
        this.refreshTokens = refreshToken;
    }

}
