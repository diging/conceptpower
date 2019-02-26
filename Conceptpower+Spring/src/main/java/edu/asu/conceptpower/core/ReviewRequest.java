package edu.asu.conceptpower.core;

import org.hibernate.validator.constraints.NotEmpty;

public class ReviewRequest {


    private String conceptId;
    @NotEmpty(message="Enter Comments.")
    private String comment;
    private CommentStatus status;
    private String requestor;
    private String resolver;
    
    public String getConceptId() {
        return conceptId;
    }

    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
    }  
   
    public CommentStatus getStatus() {
        return status;
    }

    public void setStatus(CommentStatus status) {
        this.status = status;
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

    public String getResolver() {
        return resolver;
    }

    public void setResolver(String resolver) {
        this.resolver = resolver;
    }  

}
