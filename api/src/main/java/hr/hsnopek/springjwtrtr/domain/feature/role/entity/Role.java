package hr.hsnopek.springjwtrtr.domain.feature.role.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hr.hsnopek.springjwtrtr.domain.feature.role.converters.RoleNameConverter;
import hr.hsnopek.springjwtrtr.domain.feature.role.enumeration.RoleNameEnum;
import hr.hsnopek.springjwtrtr.domain.feature.user.entity.User;

@Entity(name = "role")
@Table(name = "role")
public class Role {
	
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name")
    @Convert(converter = RoleNameConverter.class)
    @NaturalId
    private RoleNameEnum roleName;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<User> userList = new HashSet<>();

    public Role() {

    }
    
    public Role(RoleNameEnum roleName) {
    	this.roleName = roleName;
    }
    
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleNameEnum getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleNameEnum roleName) {
        this.roleName = roleName;
    }

    public Set<User> getUserList() {
        return userList;
    }

    public void setUserList(Set<User> userList) {
        this.userList = userList;
    }

}
