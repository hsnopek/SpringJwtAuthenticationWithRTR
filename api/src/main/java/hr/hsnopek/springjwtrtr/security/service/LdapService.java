package hr.hsnopek.springjwtrtr.security.service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import hr.hsnopek.springjwtrtr.domain.feature.authentication.dto.AuthenticateUserRequest;
import hr.hsnopek.springjwtrtr.domain.feature.authentication.dto.AuthenticateUserResponse;
import hr.hsnopek.springjwtrtr.domain.feature.authentication.exceptions.UserAlreadyExistsException;
import hr.hsnopek.springjwtrtr.domain.feature.authentication.exceptions.WeakPasswordException;
import hr.hsnopek.springjwtrtr.domain.feature.authentication.service.AuthenticationService;
import hr.hsnopek.springjwtrtr.domain.feature.user.dto.RegisterUserRequest;
import hr.hsnopek.springjwtrtr.domain.feature.user.dto.UserDTO;
import hr.hsnopek.springjwtrtr.domain.feature.user.entity.User;
import hr.hsnopek.springjwtrtr.domain.feature.user.service.UserService;
import hr.hsnopek.springjwtrtr.security.authenticationtokens.LdapAuthenticationToken;
import hr.hsnopek.springjwtrtr.security.model.LdapUser;
import hr.hsnopek.springjwtrtr.security.providers.LdapAuthenticationProvider;

@Service
public class LdapService {

	@Autowired
	UserService userService;
	@Autowired
	AuthenticationService authenticationService;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	LdapAuthenticationProvider ldapAuthenticationProvider;

	@Transactional
	public AuthenticateUserResponse authenticateLdapUser(AuthenticateUserRequest authenticateUserDTO) throws UserAlreadyExistsException, WeakPasswordException,
			UnsupportedEncodingException, MessagingException, JsonProcessingException {

		try {
			Authentication authenticationToken = ldapAuthenticationProvider
					.authenticate(new LdapAuthenticationToken(authenticateUserDTO.getUsername(), authenticateUserDTO.getPassword()));

			if (authenticationToken.isAuthenticated()) {
				LdapUser ldapUser = (LdapUser) authenticationToken.getPrincipal();
				Optional<User> user = userService.findByEmail(ldapUser.getEmail());
				if (!user.isPresent()) {
					registerNewUser(ldapUser);
				}
				return authenticationService.authenticate(authenticateUserDTO);
			}

		} catch (AuthenticationException e) {
			throw e;
		}

		return new AuthenticateUserResponse(null, null);
	}

	private UserDTO registerNewUser(LdapUser ldapUser) throws UnsupportedEncodingException, MessagingException {
		
		RegisterUserRequest registerUserRequest = new RegisterUserRequest();
		registerUserRequest.setUsername(ldapUser.getUsername());
		registerUserRequest.setEmail(ldapUser.getEmail());
		registerUserRequest.setFirstName(ldapUser.getFirstName());
		registerUserRequest.setLastName(ldapUser.getLastName());
		registerUserRequest.setPassword(ldapUser.getPassword());
		registerUserRequest.setTotp(false);
		registerUserRequest.setLdapUser(true);
		
		return userService.registerUser(registerUserRequest);
	}
}
