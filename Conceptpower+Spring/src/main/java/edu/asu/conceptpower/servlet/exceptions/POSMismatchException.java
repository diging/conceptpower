package edu.asu.conceptpower.servlet.exceptions;

public class POSMismatchException extends Exception {

    private static final long serialVersionUID = 1L;

    public POSMismatchException() {
        super();
    }

    public POSMismatchException(String msg) {
        super(msg);
    }
}
