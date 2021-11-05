package hr.hsnopek.springjwtrtr.web.rest;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;

import hr.hsnopek.springjwtrtr.domain.base.BaseController;
import hr.hsnopek.springjwtrtr.domain.feature.authentication.dto.AuthenticateUserRequest;
import hr.hsnopek.springjwtrtr.domain.feature.authentication.dto.AuthenticateUserResponse;
import hr.hsnopek.springjwtrtr.domain.feature.authentication.dto.RevokeTokenRequest;
import hr.hsnopek.springjwtrtr.domain.feature.authentication.dto.RevokeTokenResponse;
import hr.hsnopek.springjwtrtr.domain.feature.authentication.exceptions.UserAlreadyExistsException;
import hr.hsnopek.springjwtrtr.domain.feature.authentication.exceptions.WeakPasswordException;
import hr.hsnopek.springjwtrtr.domain.feature.authentication.service.AuthenticationService;
import hr.hsnopek.springjwtrtr.domain.feature.refreshtoken.service.RefreshTokenService;
import hr.hsnopek.springjwtrtr.security.service.LdapService;
import javassist.NotFoundException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController extends BaseController {

	@Autowired
    private AuthenticationService authenticationService;
	@Autowired
    private LdapService ldapService;
	@Autowired
	private RefreshTokenService refreshTokenService;

    /**
	 * Sets refreshToken in http-only cookie and returns access and refreshToken in http response.
	 *
	 * @param authenticateUserDTO username, password and deviceId
	 * @param httpServletRequest httpServletRequest
	 * @param httpServletResponse httpServletResponse
	 * 
	 * @return {@link AuthenticateUserResponse}
	 */
    @PostMapping("/login")
	public ResponseEntity<AuthenticateUserResponse> authenticateUser(
			@RequestBody AuthenticateUserRequest authenticateUserDTO, 
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws JsonProcessingException {
    	
    	AuthenticateUserResponse authenticateUserResponseDTO = authenticationService.authenticate(authenticateUserDTO);
    	setRefreshTokenInCookie(httpServletResponse, authenticateUserResponseDTO.getRefreshToken());
    	        
    	return ResponseEntity.status(HttpStatus.OK).body(authenticateUserResponseDTO);
    }
    
    /**
	 * Sets refreshToken in http-only cookie and returns access and refreshToken in http response.
	 *
	 * @param authenticateUserDTO username, password and deviceId
	 * @param httpServletRequest httpServletRequest
	 * @param httpServletResponse httpServletResponse
	 * 
	 * @return {@link AuthenticateUserResponse}
     * @throws MessagingException 
     * @throws UnsupportedEncodingException 
     * @throws WeakPasswordException 
     * @throws UserAlreadyExistsException 
	 */
    @PostMapping("/login-ldap")
	public ResponseEntity<AuthenticateUserResponse> authenticateLdapUser(
			@RequestBody AuthenticateUserRequest authenticateUserDTO, 
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws JsonProcessingException, UserAlreadyExistsException, WeakPasswordException, UnsupportedEncodingException, MessagingException {
    	
    	AuthenticateUserResponse authenticateUserResponseDTO = ldapService.authenticateLdapUser(authenticateUserDTO);
    	setRefreshTokenInCookie(httpServletResponse, authenticateUserResponseDTO.getRefreshToken());
    	        
    	return ResponseEntity.status(HttpStatus.OK).body(authenticateUserResponseDTO);
    }

    /**
	 * Gets and refreshes token from http-only cookie and return new refresh and access token in http response.
	 *
	 * @param httpServletRequest httpServletRequest
	 * @param httpServletResponse httpServletResponse
	 * 
	 * @return {@link AuthenticateUserResponse}
     */
    @PostMapping("/refresh-token")
	public ResponseEntity<AuthenticateUserResponse> refreshJwtToken(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws JsonProcessingException, NotFoundException {
    	
    	LOGGER.info("Revoking refresh token..");
    	AuthenticateUserResponse authenticateUserResponseDTO = refreshTokenService.refreshToken(httpServletRequest);
    	setRefreshTokenInCookie(httpServletResponse, authenticateUserResponseDTO.getRefreshToken());
    	
    	return ResponseEntity.status(HttpStatus.OK).body(authenticateUserResponseDTO);
    }

    /**
	 * Revokes refresh token and removes it from client cookie.
	 *
	 * @param httpServletRequest httpServletRequest
	 * @param httpServletResponse httpServletResponse
	 * 
	 * @return {@link RevokeTokenResponse}
     */
    @PostMapping("/revoke-token")
	public ResponseEntity<RevokeTokenResponse> revokeToken(
			@RequestBody RevokeTokenRequest revokeTokenRequest,
			HttpServletRequest httpServletRequest, 
			HttpServletResponse httpServletResponse) {
    	
    	refreshTokenService.revokeRefreshToken(httpServletRequest, revokeTokenRequest);
    	setRefreshTokenInCookie(httpServletResponse, null);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(new RevokeTokenResponse(revokeTokenRequest.getToken(), true));
    }


    /**
	 * Sets refresh token in http-only cookie.
     */
	private void setRefreshTokenInCookie(HttpServletResponse httpResponse, 
			String refreshToken) {
		
		if(refreshToken == null) {
			httpResponse.setHeader(
					"Set-Cookie",
					String.format("refreshToken=null;Path=/; SameSite=None; Secure=True; HttpOnly; Max-Age=0")
				);
		} else {
			httpResponse.setHeader(
					"Set-Cookie",
					String.format("refreshToken=%s;Path=/; SameSite=None; Secure=True; HttpOnly", refreshToken)
				);
		}

	}



}