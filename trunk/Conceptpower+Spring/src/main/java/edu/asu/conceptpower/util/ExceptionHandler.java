package edu.asu.conceptpower.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	@org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
	public String handle(Exception e) {
		logger.error("ExceptionHandler caught exception.", e);
		return "exception";
	}

}
