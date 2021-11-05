package hr.hsnopek.springjwtrtr.security.entrypoints;

import org.springframework.http.MediaType;
import org.springframework.security.web.AuthenticationEntryPoint;
import com.fasterxml.jackson.databind.ObjectMapper;

import hr.hsnopek.springjwtrtr.general.model.ErrorCodes;
import hr.hsnopek.springjwtrtr.general.model.ErrorResponse;
import hr.hsnopek.springjwtrtr.security.exceptions.AccessTokenInvalidException;
import hr.hsnopek.springjwtrtr.security.exceptions.AccountInactiveException;
import hr.hsnopek.springjwtrtr.security.exceptions.IpAddressNotValidException;
import hr.hsnopek.springjwtrtr.security.exceptions.RefreshTokenInvalidException;
import hr.hsnopek.springjwtrtr.security.exceptions.TwoFactorVerificationFailedException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.time.LocalDateTime;


public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

	@Override
	public void commence(
			HttpServletRequest request, 
			HttpServletResponse response,
			org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
		
		int errorCode = 0;
		
        ErrorResponse errorResponse = null;
        if(authException instanceof AccountInactiveException) {
        	errorCode = ErrorCodes.USER_INACTIVE;
        } else if(authException instanceof TwoFactorVerificationFailedException){
        	errorCode = ErrorCodes.TWO_FACTOR_VERIFICATION_FAILED;
        } else if(authException instanceof RefreshTokenInvalidException) {
        	errorCode = ErrorCodes.REFRESH_TOKEN_INVALID;
        } else if(authException instanceof AccessTokenInvalidException) {
        	errorCode = ErrorCodes.ACCESS_TOKEN_INVALID;
        } else if(authException instanceof IpAddressNotValidException) {
        	errorCode = ErrorCodes.INVALID_IP_ADDRESS;
        }
        
        errorResponse = new ErrorResponse(request.getRequestURI(), authException.getMessage(), errorCode, LocalDateTime.now());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        String resBody = objectMapper.writeValueAsString(errorResponse);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(resBody);
        printWriter.flush();
        printWriter.close();

	}
}