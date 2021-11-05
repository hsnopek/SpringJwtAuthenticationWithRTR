package hr.hsnopek.springjwtrtr.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hr.hsnopek.springjwtrtr.domain.feature.role.enumeration.RoleNameEnum;
import hr.hsnopek.springjwtrtr.general.util.ApplicationProperties;
import hr.hsnopek.springjwtrtr.security.model.UserPrincipal;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTTokenUtil {

    private static final String AUTHORITIES_CLAIM = "authorities";
    private static final String ACTIVE_CLAIM = "active";
    private static final String LOGGED_IN_CLAIM = "loggedIn";
    private static final String TOKEN_TYPE_CLAIM = "tokenType";
        

    @Autowired
    ObjectMapper objectMapper;

    /**
     * Generates refresh token for authenticated user without OTP.
     * 
     * @param principal authenticated user principal
     * @param jti JWT ID
     * 
     * 
     * @return {@link String}
     */
    public String generateRefreshToken(UserPrincipal principal, String jti) {
        Instant expiryDate = Instant.now().plusMillis(ApplicationProperties.REFRESH_TOKEN_EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiryDate))
                .setId(jti)
                .claim(TOKEN_TYPE_CLAIM, TokenType.REFRESH_TOKEN.toString())
                .signWith(SignatureAlgorithm.HS512, ApplicationProperties.JWT_SECRET)
                .compact();
    }
    
    /**
     * Generates refresh token for authenticated user with OTP.
     * 
     * @param principal authenticated user principal
     * @param jti JWT ID
     * @param loggedIn true if refresh token is verified by correct OTP code
     * 
     * @return {@link String}
     */
    public String generateRefreshToken(UserPrincipal principal, String jti, boolean loggedIn) {
        Instant expiryDate = Instant.now().plusMillis(ApplicationProperties.REFRESH_TOKEN_EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiryDate))
                .setId(jti)
                .claim(LOGGED_IN_CLAIM, loggedIn)
                .claim(TOKEN_TYPE_CLAIM, TokenType.REFRESH_TOKEN.toString())
                .signWith(SignatureAlgorithm.HS512, ApplicationProperties.JWT_SECRET)
                .compact();
    }
    
    /**
     * Generates access token for authenticated user with OTP.
     * 
     * @param principal authenticated user principal
     * @param active true if user activated his account via e-mail
     * @param loggedIn true if refresh token is verified by correct OTP code
     * 
     * @return {@link String}
     */
    public String generateAccessToken(UserPrincipal principal, boolean active, boolean loggedIn) throws JsonProcessingException {
    	
        Instant expiryDate = Instant.now().plusMillis(ApplicationProperties.ACCESS_TOKEN_EXPIRATION_TIME);
        String authorities = getUserAuthorities(principal);
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiryDate))
                .signWith(SignatureAlgorithm.HS512, ApplicationProperties.JWT_SECRET)
                .claim(AUTHORITIES_CLAIM, authorities)
                .claim(ACTIVE_CLAIM, active)
                .claim(LOGGED_IN_CLAIM, loggedIn)
                .claim(TOKEN_TYPE_CLAIM, TokenType.ACCESS_TOKEN.toString())
                .compact();
    }
    
    /**
     * Extracts token type claim from token.
     * @param token jwt
     * 
     * @return {@link String} claim
     */
    public String getTokenTypeFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(ApplicationProperties.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return (String) claims.get(TOKEN_TYPE_CLAIM);
    }
    
    /**
     * Extracts subject claim from token.
     * @param token jwt
     * 
     * @return {@link String} claim
     */
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(ApplicationProperties.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
    
    /**
     * Extracts JWT ID from token.
     * @param token jwt
     * 
     * @return {@link String} claim
     */
    public String getJtiFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(ApplicationProperties.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getId();
    }
    
	
    /**
	 * Extracts authorized claim from token. If 2FA has been enabled, token is
	 * authorized only if user account is active (registration mail confirmed) and
	 * OTP code has been verified.
	 * 
	 * @param token jwt
	 * 
	 * @return {@link Boolean}
	 */
	public boolean getAuthorizedClaimFromJWT(String token) throws JsonMappingException, JsonProcessingException {
        Claims claims = Jwts.parser()
                .setSigningKey(ApplicationProperties.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        
        return (boolean) claims.get(LOGGED_IN_CLAIM);
	}
	
    /**
	 * Extracts active claim from token. Returns true if account activated via e-mail.
	 * 
	 * @param token jwt
	 * 
	 * @return {@link Boolean}
	 */
	public boolean getActiveClaimFromJWT(String token) throws JsonMappingException, JsonProcessingException {
        Claims claims = Jwts.parser()
                .setSigningKey(ApplicationProperties.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        
        return (boolean) claims.get(ACTIVE_CLAIM);
	}

    /**
     * Gets token expiry date.
     * @param token jwt
     * 
     * @return {@link Date}
     */
    public Date getTokenExpiryFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(ApplicationProperties.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }


    /**
     * Gets list of {@link UserPrincipal} granted authorities from token.
     * @param token jwt
     * 
     * @return {@link List<GrantedAuthority>}
     */
    public List<GrantedAuthority> getAuthoritiesFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(ApplicationProperties.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return Arrays.stream(claims.get(AUTHORITIES_CLAIM).toString().split(","))
                .map( role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    /**
     * Returns comma separated {@link UserPrincipal} authorities
     * @param principal authenticated user
     * 
     * @return {@link String}
     */
    private String getUserAuthorities(UserPrincipal principal) {
        return principal
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
    
    /**
	 * Validates JWT token with server provided secret key.
	 * 
	 * @param token jwt
	 * @throws {@link SignatureException}, {@link MalformedJwtException},
	 *                {@link ExpiredJwtException}, {@link UnsupportedJwtException},
	 *                {@link IllegalArgumentException}
	 * @return {@link String}
	 */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(ApplicationProperties.JWT_SECRET).parseClaimsJws(token);
        } catch (SignatureException ex) {
        	throw ex;
        } catch (MalformedJwtException ex) {
        	throw ex;
        } catch (ExpiredJwtException ex) {
        	throw ex;
        } catch (UnsupportedJwtException ex) {
        	throw ex;
        } catch (IllegalArgumentException ex) {
        	throw ex;
        }
        return true;
    }

    public enum TokenType {

        REFRESH_TOKEN("refresh_token"),
        ACCESS_TOKEN("access_token");
        
        private final String tokenType;

        private TokenType(String tokenType) {
            this.tokenType = tokenType;
        }
    	
        public static TokenType fromString(String roleName) throws IllegalArgumentException {
            return Arrays.stream(TokenType.values())
                    .filter(x -> x.tokenType.equals(roleName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown value: " + roleName));
        }

    	@Override
    	public String toString() {
    		return tokenType;
    	}
        
    }

}
