package edu.asu.conceptpower.app.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ConceptPowerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ConceptPowerExceptionHandler.class);
    
    @Value("#{messages['INDEXER_RUNNING']}")
    private String indexerRunning;

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

    /**
     * This method has been introduced to handle IndexRunningExceptions. All
     * controller level classes check for index running status and proceed to
     * the service layer. In case new user has started indexer in that time gap
     * IndexerRunningException is passed and handled in this advice
     * 
     * @param req
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = IndexerRunningException.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("show_error_alert", true);
        mav.addObject("error_alert_msg", indexerRunning);
        mav.setViewName("conceptsearch");
        return mav;
    }
}
