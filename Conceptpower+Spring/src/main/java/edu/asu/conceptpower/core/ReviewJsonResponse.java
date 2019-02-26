package edu.asu.conceptpower.core;

import java.util.Map;

public class ReviewJsonResponse {
    
    private String conceptId;
    private String comment;
    private String requestor;
    //--
    private boolean validated;
    private Map<String, String> errorMessages;
   
    //--
    public String getConceptId() {
        return conceptId;
    }
    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getRequestor() {
        return requestor;
    }
    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }
    public boolean isValidated() {
        return validated;
    }
    public void setValidated(boolean validated) {
        this.validated = validated;
    }
    public Map<String, String> getErrorMessages() {
        return errorMessages;
    }
    public void setErrorMessages(Map<String, String> errorMessages) {
        this.errorMessages = errorMessages;
    }

}
