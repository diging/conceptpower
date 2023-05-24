package edu.asu.conceptpower.rest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.rest.msg.IConceptMessage;
import edu.asu.conceptpower.rest.msg.IMessageRegistry;
import jakarta.servlet.http.HttpServletRequest;

/**
 * This class handles the exceptions from rest interface calls.
 * 
 * @author karthikeyanmohan
 *
 */
@ControllerAdvice
public class RestExceptionHandler {

    @Autowired
    private IMessageRegistry messageRegistry;

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

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

    @org.springframework.web.bind.annotation.ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<String> handleAuthenticationFailure(BadCredentialsException ex) {
        return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
        return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = JsonProcessingException.class)
    public ResponseEntity<String> handleJsonProcessingException(JsonProcessingException ex) {
        return new ResponseEntity<String>("Problems in processing json.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = IndexerRunningException.class)
    public ResponseEntity<String> handleIndexerRunningException(HttpServletRequest req, IndexerRunningException ex)
            throws JsonProcessingException {
        logger.info("Indexer running exception", ex);
        IConceptMessage msg = messageRegistry.getMessageFactory(req.getHeader("Accept")).createConceptMessage();
        return new ResponseEntity<String>(msg.getErrorMessage(ex.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }
}
