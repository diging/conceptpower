package edu.asu.conceptpower.app.error;

public class CPError {

    private String errorCode;

    public CPError(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
