package edu.asu.conceptpower.app.error;

public class CPError {

    private String errorCode;
    private String message;

    public CPError(String message) {
        this.message = message;
    }

    public CPError(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
