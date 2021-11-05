package hr.hsnopek.springjwtrtr.general.exceptions;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.exception.GenericJDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import hr.hsnopek.springjwtrtr.general.model.ErrorCodes;
import hr.hsnopek.springjwtrtr.general.model.ErrorResponse;

@ControllerAdvice
public class ExceptionAdvice {
	
    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler({ InvalidParameterException.class, GenericJDBCException.class })
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse processValidationError(Exception ex, HttpServletRequest request) {
    	int errorCode = 400;
		final ErrorResponse response = new ErrorResponse(request.getRequestURI(), ex.getMessage(), errorCode, LocalDateTime.now());
		return response;
	}

	@ExceptionHandler({ BadCredentialsException.class })
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public ErrorResponse processAuthorizationError(final Exception ex, HttpServletRequest request) {
    	int errorCode = ErrorCodes.BAD_CREDENTIALS;
		final ErrorResponse response = new ErrorResponse(request.getRequestURI(),ex.getMessage(), errorCode, LocalDateTime.now());
		return response;
	}
	
	@ExceptionHandler({ UserRestrictedException.class, DisabledException.class })
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	@ResponseBody
	public ErrorResponse processForbiddenError(Exception ex, HttpServletRequest request) {
    	int errorCode = 403;
		ErrorResponse response = new ErrorResponse(request.getRequestURI(),ex.getMessage(), errorCode, LocalDateTime.now());
		return response;
	}
	
    /**
     * Handles JPA NoResultExceptions thrown from web service controller methods.  
     * @param noResultException A NoResultException instance.
     * @param request The HttpServletRequest in which the NoResultException was
     *        raised.
     * @return {@link ErrorResponse} containing exception message in the body and a HTTP status code 500.
     */
    @ExceptionHandler({NoResultException.class, UsernameNotFoundException.class})
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ResponseBody
    public ErrorResponse handleNoResultException(Exception ex, HttpServletRequest request) {
    	
    	int errorCode = 404;
		final ErrorResponse response = new ErrorResponse(request.getRequestURI(),ex.getMessage(), errorCode, LocalDateTime.now());
		return response;
    }

    
    /**
     * Handles all Exceptions not addressed by more specific
     * <code>@ExceptionHandler</code>
     * 
     * @param exception An Exception instance.
     * @param request The HttpServletRequest in which the Exception was raised.
     * @return {@link ErrorResponse} containing exception message in the body and a HTTP status code 500.
     */
    @ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
    public ErrorResponse handleException(Exception ex, HttpServletRequest request) {
    	
    	int errorCode = 500;
		final ErrorResponse response = new ErrorResponse(request.getRequestURI(), ex.getMessage(), errorCode, LocalDateTime.now());
		return response;
    }

}
