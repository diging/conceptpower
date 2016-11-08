package edu.asu.conceptpower.app.exceptions;

public class IndexerRunningException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public IndexerRunningException() {
        super();
    }

    /**
     * Custom message in the exception
     * 
     * @param customMsg
     */
    public IndexerRunningException(String customMsg) {
        super(customMsg);
    }

    public IndexerRunningException(Exception e) {
        super(e);
    }

    public IndexerRunningException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
