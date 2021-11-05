package hr.hsnopek.springjwtrtr.web.rest;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.Principal;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import hr.hsnopek.springjwtrtr.domain.base.BaseController;
import hr.hsnopek.springjwtrtr.domain.feature.user.dto.RegisterUserRequest;
import hr.hsnopek.springjwtrtr.domain.feature.user.dto.RegisterUserResponse;
import hr.hsnopek.springjwtrtr.domain.feature.user.dto.UserDTO;
import hr.hsnopek.springjwtrtr.domain.feature.user.entity.User;
import hr.hsnopek.springjwtrtr.domain.feature.user.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
	
	@Autowired
	UserService userService;
	
    /**
     *  Registers new user. User must provide username, password, email.
     *  User can also enable 2FA i totp parameter is set to true.
     *  
     *  @param {@link RegisterUserRequest} registerUserDTO
     *  @param {@link HttpServletResponse} httpServletResposne
     *  
     *  @return {@link RegisterUserResponse}
     */
	@PostMapping("/register")
	public ResponseEntity<RegisterUserResponse> registerUser(@RequestBody RegisterUserRequest registerUserDTO,
			HttpServletResponse httpServletResponse) throws UnsupportedEncodingException, MessagingException {

		UserDTO userDTO = userService.registerUser(registerUserDTO);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.path("/user/{id}")
				.buildAndExpand(userDTO.getId())
				.toUri();

		httpServletResponse.setHeader("Location", location.toString());

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(new RegisterUserResponse(RegisterUserResponse.Status.OK, userDTO));
	}
	
    /**
     *  Activates user if verification code recieved in email is correct
     *  
     *  @param verificationCode verification code sent to user's email when account was created
     *  
     *  @return {@link Boolean}
     */
	@GetMapping("/email-verify")
	public ResponseEntity<?> verifyUser(@RequestParam("verificationCode") String verificationCode) {
		userService.verifyUser(verificationCode);
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}
	
	//TODO: Forgoten mail, reset
	
	
    /**
     *  Finds user by userId
     *  
     *  @param id userId
     *  @param principal currently logged-in user via JWT
     *  
     *  @return {@link User}

     */
	@GetMapping("/{id}")
	public ResponseEntity<?> findUserById(@PathVariable(value = "id") Long id, Principal userPrincipal){
		return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id, userPrincipal.getName()));
	}
	
    /**
     *  Finds user by userId
     *  
     *  @param id userId
     *  @param principal currently logged-in user via JWT
     *  
     *  @return {@link User}

     */
	@GetMapping("/principal")
	public ResponseEntity<?> findUser(Principal userPrincipal){
		return ResponseEntity.status(HttpStatus.OK).body(userService.findByUsername(userPrincipal.getName()));
	}
}
