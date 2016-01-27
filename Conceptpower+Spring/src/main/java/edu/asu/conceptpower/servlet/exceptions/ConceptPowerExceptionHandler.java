package edu.asu.conceptpower.servlet.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ConceptPowerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ConceptPowerExceptionHandler.class);
    
    /**
     * For now this method handles all exceptions thrown in Controller classes. Eventually this method can be
     * replaced by methods that handle individual exceptions.
     * 
     * @param ex The exception thrown in a controller.
     * @return Information about the exception page.
     */
    @ExceptionHandler(LuceneException.class)
    public ModelAndView handleNotImplementedEx(Exception ex) {
        
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("auth/notImplemented");
        modelAndView.addObject("ex_name", ex.getClass().getName());
        modelAndView.addObject("ex_message", ex.getMessage());
        logger.error(ex.getMessage(), ex);
        return modelAndView;
    }
    
}
