package edu.asu.conceptpower.core;

public class ReviewJsonResponse {
    
    private String conceptId;
    private String comment;
    private String requestor;
    //--
    private String status = null;
    private Object result = null;
    public String getStatus() {
            return status;
    }
    public void setStatus(String status) {
            this.status = status;
    }
    public Object getResult() {
            return result;
    }
    public void setResult(Object result) {
            this.result = result;
    }
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

}
