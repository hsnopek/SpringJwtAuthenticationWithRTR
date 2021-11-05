package hr.hsnopek.springjwtrtr.security.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import hr.hsnopek.springjwtrtr.security.authenticationtokens.JwtAuthenticationToken;
import hr.hsnopek.springjwtrtr.security.util.JWTTokenUtil;


public class JWTAuthenticationFilter extends OncePerRequestFilter  {
	
	private final String JWT_TOKEN_PREFIX = "Bearer ";

	JWTTokenUtil jwtTokenUtil;
	AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(JWTTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager) {
		this.jwtTokenUtil = jwtTokenUtil;
		this.authenticationManager = authenticationManager;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
				
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if(StringUtils.startsWithIgnoreCase(authorizationHeader, JWT_TOKEN_PREFIX)) {
			String jwt = parseJWTFromAuthorizationHeader(authorizationHeader);
			authenticateUser(jwt);
		} 
		
		filterChain.doFilter(request, response);
	}
	
	private void authenticateUser(String jwt) {
		JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(jwt);
		Authentication authentication = authenticationManager.authenticate(jwtAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private String parseJWTFromAuthorizationHeader(String authorizationHeader) {
		return authorizationHeader.substring(JWT_TOKEN_PREFIX.length());
	}
}
