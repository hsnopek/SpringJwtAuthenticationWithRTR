package hr.hsnopek.springjwtrtr.domain.feature.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.hsnopek.springjwtrtr.domain.feature.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
	User findByVerificationCode(String verificationCode);

}
