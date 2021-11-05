package hr.hsnopek.springjwtrtr.domain.feature.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.hsnopek.springjwtrtr.domain.feature.role.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

}
