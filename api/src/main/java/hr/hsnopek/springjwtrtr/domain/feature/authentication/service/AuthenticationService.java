package hr.hsnopek.springjwtrtr.domain.feature.authentication.service;

import java.security.InvalidParameterException;
import java.util.Objects;
import java.util.Optional;

import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import hr.hsnopek.springjwtrtr.domain.base.BaseService;
import hr.hsnopek.springjwtrtr.domain.feature.authentication.dto.AuthenticateUserRequest;
import hr.hsnopek.springjwtrtr.domain.feature.authentication.dto.AuthenticateUserResponse;
import hr.hsnopek.springjwtrtr.domain.feature.refreshtoken.entity.RefreshToken;
import hr.hsnopek.springjwtrtr.domain.feature.refreshtoken.service.RefreshTokenService;
import hr.hsnopek.springjwtrtr.domain.feature.user.entity.User;
import hr.hsnopek.springjwtrtr.domain.feature.userdevice.entity.UserDevice;
import hr.hsnopek.springjwtrtr.domain.feature.userdevice.service.UserDeviceService;
import hr.hsnopek.springjwtrtr.general.localization.Message;
import hr.hsnopek.springjwtrtr.general.service.Translator;
import hr.hsnopek.springjwtrtr.security.exceptions.CustomAuthenticationException;
import hr.hsnopek.springjwtrtr.security.model.UserPrincipal;
import hr.hsnopek.springjwtrtr.security.service.UserDetailsService;
import hr.hsnopek.springjwtrtr.security.util.JWTTokenUtil;

@Service
public class AuthenticationService extends BaseService {
	
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JWTTokenUtil jwtTokenUtil;
	@Autowired
	UserDeviceService userDeviceService;
	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	RefreshTokenService refreshTokenService;

	@Transactional
	public AuthenticateUserResponse authenticate(AuthenticateUserRequest authenticateUserRequest) throws JsonProcessingException {
		AuthenticateUserResponse authenticateUserResponseDTO = null;
		
        Authentication auth = authenticate(authenticateUserRequest.getUsername(), authenticateUserRequest.getPassword());
                
        final UserDetails userDetails = (UserDetails) auth.getPrincipal();
        UserPrincipal userPrincipal = (UserPrincipal) userDetails;
        User user = userPrincipal.getUser();
        
        UserDevice userDevice = userDeviceService.createUserDeviceIfNotExists(authenticateUserRequest.getUserDeviceId(), user);
        RefreshToken refreshToken = refreshTokenService.createRefreshTokenIfNotExists((UserPrincipal) userDetails, userDevice);
        
        String accessToken = jwtTokenUtil.generateAccessToken(userPrincipal, user.getActive() ,refreshToken.getLoggedIn());
        authenticateUserResponseDTO = new AuthenticateUserResponse(refreshToken.getRefreshToken(), accessToken);
       		
		return authenticateUserResponseDTO;
	}
	
    private Authentication authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new DisabledException(Translator.toLocale(Message.ERROR_USER_INACTIVE));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(Translator.toLocale(Message.ERROR_USER_BAD_CREDENTIALS));
        } catch(Exception e) {
            throw new CustomAuthenticationException("Generic exception", e);
        }
    }
    
	public boolean verifyUserOneTimePassword(String username, String otp) throws UsernameNotFoundException {
    	UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    	UserPrincipal userPrincipal = (UserPrincipal) userDetails;
    	User user = userPrincipal.getUser();
   
		if (user != null) {
			Totp totp = new Totp(user.getSecret());
			return totp.verify(otp);
		}
		return false;
	}


    @Transactional
	public boolean verifyUserOneTimePassword(User user, String otp, String deviceId) throws UsernameNotFoundException {
		if (user != null) {
			Totp totp = new Totp(user.getSecret());
			if (totp.verify(otp)) {
				if(user.getActive().equals(Boolean.FALSE)) {
					user.setActive(true);					
				}
				
				// mark refresh token as logged in
				UserDevice userDevice = user.getUserDevices().stream()
						.filter(x -> x.getDeviceId().equals(deviceId))
						.findFirst()
						.orElseThrow(() -> new InvalidParameterException("Provided device id not linked to user account!"));
				
				// try to find active refresh token
				Optional<RefreshToken> optionalRefreshToken = userDevice.getRefreshTokens().stream()
				.filter( x -> x.getRevoked().equals(Boolean.FALSE))
				.findFirst();
				
				// if such token exists set it to logged in
				if(optionalRefreshToken.isPresent()) {
					RefreshToken refreshToken = optionalRefreshToken.get();
					refreshToken.setLoggedIn(true);
				}
				return true;
			}
		}
		return false;
	}
}
