package hr.hsnopek.springjwtrtr.domain.feature.user.mapper;

import java.util.UUID;

import org.jboss.aerogear.security.otp.api.Base32;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import hr.hsnopek.springjwtrtr.domain.feature.user.dto.RegisterUserRequest;
import hr.hsnopek.springjwtrtr.domain.feature.user.entity.User;
import hr.hsnopek.springjwtrtr.general.util.ApplicationProperties;

@Component
public class RegisterUserDTOMapper {
	
	@Autowired
	ModelMapper modelMapper;
	
	public User map(RegisterUserRequest registerUserDTO) {
		User user = modelMapper.map(registerUserDTO, User.class);
		
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

		if(Boolean.TRUE.equals(registerUserDTO.getTotp())) {
			// If 2-factor-authentication is active, user becomes active as soon as he
			// confirms email and verifies secret
			user.setActive(false);
			user.setSecret(Base32.random());
		} else {
			user.setActive(true);
			user.setSecret(null);
		}
		
		if(Boolean.TRUE.equals(ApplicationProperties.EMAIL_VERIFICATION_ENABLED)){
			user.setVerificationCode(UUID.randomUUID().toString());
			user.setActive(false);
		}
		
		if(Boolean.TRUE.equals(registerUserDTO.isLdapUser())) {
			user.setActive(true);
		}
		
		return user;
	}
}
