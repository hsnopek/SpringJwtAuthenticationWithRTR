package hr.hsnopek.springjwtrtr.web.rest;

import java.awt.image.BufferedImage;
import java.security.Principal;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.zxing.WriterException;
import hr.hsnopek.springjwtrtr.domain.base.BaseController;
import hr.hsnopek.springjwtrtr.domain.feature.authentication.service.AuthenticationService;
import hr.hsnopek.springjwtrtr.domain.feature.user.entity.User;
import hr.hsnopek.springjwtrtr.general.util.ImageUtils;
import hr.hsnopek.springjwtrtr.security.model.UserPrincipal;
import hr.hsnopek.springjwtrtr.security.service.UserDetailsService;
import hr.hsnopek.springjwtrtr.security.util.JWTTokenUtil;

@RestController
@RequestMapping("/totp")
public class TotpController extends BaseController {

	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	AuthenticationService authenticationService;
	@Autowired
	JWTTokenUtil jwtTokenUtil;

    /**
     *  Used during registration process to check if 2fa works as it should. 
     *  
     * @param username username
	 * @param otp one-time-password generated in Authenticator app

     *  @return true if successful
     */
	@PostMapping("/registration-confirm-secret")
	public ResponseEntity<Boolean> registrationConfirmSecret(
			@RequestParam("username") String username,
			@RequestParam(name = "otp", required = true) String otp
			) {
		boolean success = authenticationService.verifyUserOneTimePassword(username, otp);
		return ResponseEntity.status(HttpStatus.OK).body(success);
	}
	
    /**
     *  Verifies OTP (if user-enabled) for currently logged in user via JWT token.
     *  User must provide valid one-time-password for his to be activated.
     *  Not called if user opted-out out of 2FA protection.

	 * @param code one-time-password generated in Authenticator app
	 * @param principal currently logged-in user via JWT
	 * @param deviceId device currently in use by user
	 * @return void
     */
	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOtp(
			@RequestParam String code, 
			Principal principal, 
			@RequestParam String deviceId,
			HttpServletResponse httpServletResponse
			) throws JsonProcessingException {

		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		UserPrincipal userPrincipal = (UserPrincipal) userDetails;
		User user = ((UserPrincipal) userDetails).getUser();
		boolean otpVerified = authenticationService.verifyUserOneTimePassword(((UserPrincipal) userDetails).getUser(), code, deviceId);
		
		String accessToken = jwtTokenUtil.generateAccessToken(userPrincipal, user.getActive(), otpVerified);

		return ResponseEntity.status(HttpStatus.OK).body(accessToken);
	}
	
    /**
     *  Generates QR code image from provided url otp key uri format
     * 
	 * @param url otp key uri format
	 * @return {@link BufferedImage}
     */
	@GetMapping(path = "/get-qr-code", produces = MediaType.IMAGE_PNG_VALUE)
	public BufferedImage getQRCode(@RequestParam String url) throws WriterException{
		return ImageUtils.generateQRCode(url, 200, 200);
	}

}
