package hr.hsnopek.springjwtrtr.domain.feature.user.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.persistence.NoResultException;

import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.hsnopek.springjwtrtr.domain.base.BaseService;
import hr.hsnopek.springjwtrtr.domain.feature.authentication.exceptions.UserAlreadyExistsException;
import hr.hsnopek.springjwtrtr.domain.feature.authentication.exceptions.WeakPasswordException;
import hr.hsnopek.springjwtrtr.domain.feature.role.entity.Role;
import hr.hsnopek.springjwtrtr.domain.feature.role.enumeration.RoleNameEnum;
import hr.hsnopek.springjwtrtr.domain.feature.role.repository.RoleRepository;
import hr.hsnopek.springjwtrtr.domain.feature.user.dto.RegisterUserRequest;
import hr.hsnopek.springjwtrtr.domain.feature.user.dto.UserDTO;
import hr.hsnopek.springjwtrtr.domain.feature.user.entity.User;
import hr.hsnopek.springjwtrtr.domain.feature.user.mapper.RegisterUserDTOMapper;
import hr.hsnopek.springjwtrtr.domain.feature.user.mapper.UserDTOMapper;
import hr.hsnopek.springjwtrtr.domain.feature.user.repository.UserRepository;
import hr.hsnopek.springjwtrtr.general.exceptions.UserRestrictedException;
import hr.hsnopek.springjwtrtr.general.localization.Message;
import hr.hsnopek.springjwtrtr.general.service.EmailService;
import hr.hsnopek.springjwtrtr.general.service.Translator;

@Service
public class UserService extends BaseService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	private UserDTOMapper userDTOMapper;
	@Autowired
	private RegisterUserDTOMapper registerUserDTOMapper;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordValidator passwordValidator;
	@Autowired
	private EmailService eMailService;

	@Transactional
	public UserDTO registerUser(RegisterUserRequest request)
			throws UserAlreadyExistsException, WeakPasswordException, UnsupportedEncodingException, MessagingException {
		
		Optional<User> optionalUser = findByUsernameOrEmail(request.getUsername(), request.getEmail());
		UserDTO newUserDTO = null;
		
		if(optionalUser.isPresent()) {
			throw new UserAlreadyExistsException("User already exists!");
		} else {
			
			if(isLdapUser(request)) {
				checkPasswordStrength(request);
			}
			User user = registerUserDTOMapper.map(request);
			User savedUser = userRepository.save(user);
			mapDefaultRoleToUser(savedUser);
			newUserDTO = userDTOMapper.map(savedUser);
			
			eMailService.sendVerificationEmail(newUserDTO, "zsnopek@gmail.com", "SpringJWTAuthenticationWithRTR");

		}

		return newUserDTO;
	}

	private Optional<User> findByUsernameOrEmail(String username, String email) {
		return userRepository.findByUsernameOrEmail(username, email);
	}

	private boolean isLdapUser(RegisterUserRequest registerUserRequest) {
		return Boolean.FALSE.equals(registerUserRequest.isLdapUser());
	}

	public Optional<User> findByEmail(String email) {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		return optionalUser;
	}

	private void checkPasswordStrength(RegisterUserRequest registerUserDTO) {
		RuleResult result = this.passwordValidator.validate(new PasswordData(registerUserDTO.getPassword()));
		if (!result.isValid()) {
		  throw new WeakPasswordException("Password too weak!");
		}
	}
	
	public List<User> findAll(){
		return userRepository.findAll();
	}
	
	public User findById(Long id, String username) {
		Optional<User> optionalUser = userRepository.findById(id);
		
		if(optionalUser.isPresent() && !optionalUser.get().getUsername().equals(username))
			throw new UserRestrictedException(Translator.toLocale(Message.ERROR_FIND_USER_NOT_ALLOWED));
		
		return optionalUser.orElseThrow(() -> new NoResultException(
				Translator.toLocale(Message.ERROR_USER_DOESNT_EXIST, new Object[] { id })));
	}
	
	public User findByUsername(String username) {
		return userRepository.findByUsername(username).orElse(null);
	}

	private void mapDefaultRoleToUser(User newUser) {
		List<Role> roles = new ArrayList<>();
		Role userRole = roleRepository.findAll()
				.stream()
				.filter( role -> role.getRoleName().equals(RoleNameEnum.USER))
				.findFirst()
				.orElse(null);
		
		roles.add(userRole);
		newUser.setRoles(new HashSet<Role>(roles));
	}

	@Transactional
	public boolean verifyUser(String verificationCode) {
		
		User user = userRepository.findByVerificationCode(verificationCode);

		if (user == null || user.getActive()) {
			throw new NoResultException(Translator.toLocale(Message.ERROR_EMAIL_VERIFICATION));
		} else {
			user.setVerificationCode(null);
			user.setActive(true);
			userRepository.save(user);
			return true;
		}
	}
}
