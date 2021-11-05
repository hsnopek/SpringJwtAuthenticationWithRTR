package hr.hsnopek.springjwtrtr.security.providers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;

import hr.hsnopek.springjwtrtr.general.localization.Message;
import hr.hsnopek.springjwtrtr.general.service.Translator;
import hr.hsnopek.springjwtrtr.general.util.ApplicationProperties;
import hr.hsnopek.springjwtrtr.security.authenticationtokens.JwtAuthenticationToken;
import hr.hsnopek.springjwtrtr.security.exceptions.AccessTokenInvalidException;
import hr.hsnopek.springjwtrtr.security.exceptions.AccountInactiveException;
import hr.hsnopek.springjwtrtr.security.exceptions.IpAddressNotValidException;
import hr.hsnopek.springjwtrtr.security.exceptions.RefreshTokenInvalidException;
import hr.hsnopek.springjwtrtr.security.exceptions.TwoFactorVerificationFailedException;
import hr.hsnopek.springjwtrtr.security.model.UserPrincipal;
import hr.hsnopek.springjwtrtr.security.util.JWTTokenUtil;
import hr.hsnopek.springjwtrtr.security.util.JWTTokenUtil.TokenType;


public class JwtAuthenticationProvider implements AuthenticationProvider {
	
	private final JWTTokenUtil jwtTokenUtil;
	
	public JwtAuthenticationProvider(JWTTokenUtil jwtTokenUtil) {

		if (ApplicationProperties.IP_WHITELISTING_ENABLED && !StringUtils.isBlank(ApplicationProperties.IP_WHITELIST)) {
			String[] values = ApplicationProperties.IP_WHITELIST.split(";");
			whitelist = new HashSet<String>(Arrays.asList(values));
		}
		this.jwtTokenUtil = new JWTTokenUtil();

	}

	Set<String> whitelist = new HashSet<String>();


	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		
		String jwt = (String) auth.getPrincipal();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		String userIp = request.getRemoteAddr();
		if (ApplicationProperties.IP_WHITELISTING_ENABLED && !whitelist.contains(userIp)) {
			throw new IpAddressNotValidException(Translator.toLocale(Message.ERROR_AUTHENTICATION_IP_NOT_VALID));
		}
		
		JwtAuthenticationToken jwtAuthenticationToken = null;
		if(isProtectedRoute(request)) {
		
			checkIfUserIsAuthorized(jwt, request);
			String tokenType = jwtTokenUtil.getTokenTypeFromJWT(jwt);
			boolean tokenValid = false;
			try {
				tokenValid = jwtTokenUtil.validateToken(jwt);
			} catch (Exception e) {
				if(tokenType.equals(TokenType.REFRESH_TOKEN.toString())) {
					throw new RefreshTokenInvalidException(Translator.toLocale(Message.ERROR_AUTHENTICATION_REFRESH_TOKEN_NOT_VALID), e);
				} else {
					throw new AccessTokenInvalidException(Translator.toLocale(Message.ERROR_AUTHENTICATION_ACCESS_TOKEN_NOT_VALID), e);
				}
			}

			String username = jwtTokenUtil.getUsernameFromJWT(jwt);
			List<GrantedAuthority> authorities = jwtTokenUtil.getAuthoritiesFromJWT(jwt);

			UserPrincipal principal = new UserPrincipal(username, authorities);

			jwtAuthenticationToken = new JwtAuthenticationToken(principal, authorities);
			jwtAuthenticationToken.setAuthenticated(tokenValid);
		}
		return jwtAuthenticationToken;
	}


	@Override
	public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
	}
	
    /**
	 * Checks if all authentication steps are complete. E-mail address must be confirmed for account to be deemed active.
	 * For users who enabled 2fa, one time password must be verified as well.
	 * 
	 * @param accessToken
	 * @param httpServletRequest
	 * 
	 * @throws {@link AccountInactiveException}, {@link TwoFactorVerificationFailedException}
	 * @return void
	 */
	private void checkIfUserIsAuthorized(String accessToken, HttpServletRequest httpServletRequest) {
		
		//confirmed registration mail
		boolean active = false;
		// Check if user is loggedIn (2FA code valid)
		boolean loggedIn = false;
		try {
			active = jwtTokenUtil.getActiveClaimFromJWT(accessToken);
			loggedIn = jwtTokenUtil.getAuthorizedClaimFromJWT(accessToken);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		if(!active)
			throw new AccountInactiveException(Translator.toLocale(Message.ERROR_USER_INACTIVE));

		if (isTotpVerifyPath(httpServletRequest) && !loggedIn) {
			throw new TwoFactorVerificationFailedException(Translator.toLocale(Message.ERROR_AUTHENTICATION_USER_UNAUTHORIZED));
		}
	}


	private boolean isTotpVerifyPath(HttpServletRequest request) {
		return !new AntPathRequestMatcher("/totp/verify-otp**").matches(request);
	}

	private boolean isProtectedRoute(HttpServletRequest request) {
		
		AntPathRequestMatcher requestMatcher;
		boolean isNotProtectedRoute = false;
		
		for(String nonProtectedRoute : ApplicationProperties.WHITELISTED_PATHS) {
			requestMatcher = new AntPathRequestMatcher(nonProtectedRoute); 
			if(requestMatcher.matches(request)) {
				isNotProtectedRoute = true;
				break;
			}
		}
		
		return !isNotProtectedRoute;
	}

	
}
