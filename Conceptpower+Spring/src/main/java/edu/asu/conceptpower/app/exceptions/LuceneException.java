package edu.asu.conceptpower.app.exceptions;


/**
 * @author mkarthik90
 *
 *This exception class is created for Lucene exceptions
 */
public class LuceneException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -3850218568287768164L;

    /**
     * default storage exception
     */
    public LuceneException() {
        super();
    }

    /**
     * Custom message in the exception
     * 
     * @param customMsg
     */
    public LuceneException(String customMsg) {
        super(customMsg);
    }

    public LuceneException(Exception e) {
        super(e);
    }

    public LuceneException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
    
}
