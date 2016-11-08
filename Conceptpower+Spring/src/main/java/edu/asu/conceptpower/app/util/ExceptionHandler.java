package edu.asu.conceptpower.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * This class handle exceptions thrown in the controllers. So far, there
 * is only one exception page shown if something goes wrong.
 * 
 * @author Julia Damerow
 *
 */
@ControllerAdvice
public class ExceptionHandler {
	
	private static final Logger logger = LoggerFactory
			.getLogger(ExceptionHandler.class);

	@org.springframework.web.bind.annotation.ExceptionHandler(value = BadCredentialsException.class)
	public ResponseEntity<String> handleAuthenticationFailure(BadCredentialsException ex) {
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(value = AccessDeniedException.class)
	public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
	public String handle(Exception e) {
		logger.error("ExceptionHandler caught exception.", e);
		return "exception";
	}
	
    @org.springframework.web.bind.annotation.ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("Illegal argument exception.", ex);
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = IllegalAccessException.class)
    public ResponseEntity<String> handleIllegalAccessException(IllegalAccessException ex) {
        logger.error("Illegal access exception.", ex);
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
