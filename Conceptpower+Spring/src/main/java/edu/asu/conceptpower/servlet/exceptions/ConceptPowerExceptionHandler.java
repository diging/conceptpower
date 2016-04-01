package edu.asu.conceptpower.servlet.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ConceptPowerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ConceptPowerExceptionHandler.class);

    /**
     * For now this method handles all exceptions thrown in Controller classes.
     * Eventually this method can be replaced by methods that handle individual
     * exceptions.
     * 
     * @param ex
     *            The exception thrown in a controller.
     * @return Information about the exception page.
     */
    @ExceptionHandler({ IllegalArgumentException.class, IllegalAccessException.class, LuceneException.class })
    public ModelAndView handleNotImplementedEx(Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("auth/notImplemented");
        modelAndView.addObject("ex_name", ex.getClass().getName());
        modelAndView.addObject("ex_message", ex.getMessage());
        logger.error(ex.getMessage(), ex);
        return modelAndView;
    }
    
    @ExceptionHandler(value = IndexerRunningException.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
      //  if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
        //    throw e;

        // Otherwise setup and send the user to a default error-view.
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        System.out.println(req.getHeaderNames().hasMoreElements());
        
        	System.out.println(req.getHeader("referer"));
        	
        	
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(req.getRequestURL().toString());
        return mav;
    }
}
