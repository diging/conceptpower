package edu.asu.conceptpower.core;

import java.util.ArrayList;
import java.util.List;

public class ReviewRequest {


    private String conceptId;
    private String request;
    private ReviewStatus status;
    private String requester;
    private String resolver;
    private List<String> resolvingComment;
    
    public ReviewRequest() {
        resolvingComment = new ArrayList<>();
    }
    
    public String getConceptId() {
        return conceptId;
    }

    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
    }  
   
    public ReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getResolver() {
        return resolver;
    }

    public void setResolver(String resolver) {
        this.resolver = resolver;
    }  
    
    public List<String> getResolvingComment() {
        return resolvingComment;
    }
    
    public void setResolvingComment(List<String> resolvingComment) {
        this.resolvingComment = resolvingComment;
    }

}
