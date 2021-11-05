package hr.hsnopek.springjwtrtr.domain.feature.refreshtoken.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import hr.hsnopek.springjwtrtr.domain.base.BaseService;
import hr.hsnopek.springjwtrtr.domain.feature.authentication.dto.AuthenticateUserResponse;
import hr.hsnopek.springjwtrtr.domain.feature.authentication.dto.RevokeTokenRequest;
import hr.hsnopek.springjwtrtr.domain.feature.refreshtoken.entity.RefreshToken;
import hr.hsnopek.springjwtrtr.domain.feature.refreshtoken.repository.RefreshTokenRepository;
import hr.hsnopek.springjwtrtr.domain.feature.userdevice.entity.UserDevice;
import hr.hsnopek.springjwtrtr.security.model.UserPrincipal;
import hr.hsnopek.springjwtrtr.security.service.UserDetailsService;
import hr.hsnopek.springjwtrtr.security.util.JWTTokenUtil;
import javassist.NotFoundException;

@Service
public class RefreshTokenService extends BaseService {
	
	@Autowired
	JWTTokenUtil jwtTokenUtil;
	@Autowired
	RefreshTokenRepository refreshTokenRepository;
	@Autowired
	UserDetailsService userDetailsService;
	
	private RefreshToken findOneByRefreshToken(final String refreshToken) {
		jwtTokenUtil.validateToken(refreshToken);
		String jti = jwtTokenUtil.getJtiFromJWT(refreshToken);
		return refreshTokenRepository.findOneByJti(jti);
	}
	
	public RefreshToken createRefreshTokenIfNotExists(UserPrincipal userPrincipal, UserDevice userDevice) {
		
		RefreshToken refreshToken = refreshTokenRepository.findOneByUserDeviceIdAndRevoked(userDevice.getId(), false);
		
		if(isTokenNull(refreshToken)) {
			
			String jti = UUID.randomUUID().toString();
			String token = jwtTokenUtil.generateRefreshToken(userPrincipal, jti);
			
			refreshToken = new RefreshToken();
			refreshToken.setRefreshToken(token);
			refreshToken.setExpiryDate(jwtTokenUtil.getTokenExpiryFromJWT(token).toInstant());
			refreshToken.setUserDevice(userDevice);
			refreshToken.setRevoked(false);
			// If 2-factor-verification is enabled do not log in user until he enters secret code
			refreshToken.setLoggedIn(userPrincipal.getUser().totpEnabled() ? false : true);
			refreshToken.setJti(jti);
		}
		
		return refreshTokenRepository.save(refreshToken);
	}
	
	public RefreshToken createNewTokenFromOldRefreshToken(UserPrincipal userPrincipal, RefreshToken oldRefreshToken) throws JsonMappingException, JsonProcessingException {

		String jti = UUID.randomUUID().toString();
				
		String token = jwtTokenUtil.generateRefreshToken(userPrincipal, jti);

		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setRefreshToken(token);
		if (oldRefreshToken.isExpired()) {
			refreshToken.setExpiryDate(jwtTokenUtil.getTokenExpiryFromJWT(token).toInstant());
		} else {
			refreshToken.setExpiryDate(oldRefreshToken.getExpiryDate());
		}
		refreshToken.setUserDevice(oldRefreshToken.getUserDevice());
		refreshToken.setRevoked(false);
		refreshToken.setLoggedIn(oldRefreshToken.getLoggedIn());
		refreshToken.setJti(jti);

		refreshTokenRepository.save(refreshToken);

		return refreshToken;
	}
    
	
	public AuthenticateUserResponse refreshToken(HttpServletRequest httpServletRequest)
			throws JsonProcessingException, NotFoundException {
		
		Cookie cookie = WebUtils.getCookie(httpServletRequest, "refreshToken");
    	
    	if(cookie != null && !StringUtils.isEmpty(cookie.getValue())) {
    		
    		RefreshToken refreshToken = findOneByRefreshToken(cookie.getValue());
    		
    		if(isTokenNull(refreshToken))
    			throw new NotFoundException("Token not found!");
    		
    		if(!isTokenRevoked(refreshToken)) {
        		UserPrincipal principal = getPrincipalFromRefreshTokenCookie(cookie);
        		// replace revoked token for new refresh token
    			RefreshToken newRefreshToken = rotateRefreshTokens(principal, refreshToken);
				return new AuthenticateUserResponse(newRefreshToken.getRefreshToken(),
						jwtTokenUtil.generateAccessToken(principal, principal.getUser().getActive(), newRefreshToken.getLoggedIn()));
    		} else {
    			// safety measures
                revokeDescendantRefreshTokens(refreshToken.getJti());
    		}
    	} else
			throw new NotFoundException("Token not found!");

		return new AuthenticateUserResponse();
	}
	
	@Transactional
	public void revokeRefreshToken(HttpServletRequest request, RevokeTokenRequest revokeTokenRequest) {
		String refreshToken = extractRefreshTokenFromRequest(revokeTokenRequest, request);
		revokeRefreshToken(refreshToken);
	}

	private void revokeRefreshToken(String token) {
		RefreshToken refreshToken = refreshTokenRepository.findOneByJti(jwtTokenUtil.getJtiFromJWT(token));
    	refreshToken.setRevoked(true);
    	refreshToken.setLoggedIn(false);
		refreshTokenRepository.save(refreshToken);
	}


	@Transactional
	private RefreshToken rotateRefreshTokens(UserPrincipal userPrincipal, RefreshToken oldRefreshToken) throws JsonMappingException, JsonProcessingException {
		
		RefreshToken newRefreshToken = createNewTokenFromOldRefreshToken(userPrincipal, oldRefreshToken);
		
		oldRefreshToken.setRevoked(true);
		oldRefreshToken.setExpiryDate(Instant.now());
		oldRefreshToken.setReplacedBy(newRefreshToken.getId());
		oldRefreshToken.setLoggedIn(false);
		refreshTokenRepository.save(oldRefreshToken);
		return newRefreshToken;
	}

	private UserPrincipal getPrincipalFromRefreshTokenCookie(Cookie refreshTokenCookie) {
		String username = jwtTokenUtil.getUsernameFromJWT(refreshTokenCookie.getValue());
		return (UserPrincipal) userDetailsService.loadUserByUsername(username);
	}


    @Transactional
	public void revokeDescendantRefreshTokens(String jti) {
		
    	List<RefreshToken> refreshTokenDescendants = refreshTokenRepository.findDescendantRefreshTokens(jti);
    	
    	refreshTokenDescendants.forEach( rt -> {
    		if(!isTokenRevoked(rt)) {
	    		revokeRefreshToken(rt.getRefreshToken());
    		}
    	});
	}

	private String extractRefreshTokenFromRequest(RevokeTokenRequest revokeTokenDTO, HttpServletRequest request) {
		
		// Strategy for getting refreshToken. RequestBody has priority over cookie.

		String refreshToken;
		if(revokeTokenDTO != null && !StringUtils.isEmpty(revokeTokenDTO.getToken())) {
    		refreshToken = revokeTokenDTO.getToken();
    	} else {
        	Cookie refreshTokenCookie = WebUtils.getCookie(request, "refreshToken");
    		refreshToken = refreshTokenCookie != null ? refreshTokenCookie.getValue() : null;
    	}
		
		if(refreshToken == null) {
			throw new RuntimeException("RefreshToken not found in request.");
		}
			
		
		return refreshToken;
	}
	private boolean isTokenNull(RefreshToken refreshToken) {
		return refreshToken == null;
	}

	private boolean isTokenRevoked(RefreshToken refreshToken) {
		return refreshToken.getRevoked();
	}

	public List<RefreshToken> findByExpiryDateBeforeAndRevoked(Instant now, Boolean revoked) {
		return refreshTokenRepository.findByExpiryDateBeforeAndRevoked(now, revoked);
	}

	public void deleteRefreshTokens(List<RefreshToken> refreshTokens) {
		refreshTokenRepository.deleteAll(refreshTokens);
	}

}
