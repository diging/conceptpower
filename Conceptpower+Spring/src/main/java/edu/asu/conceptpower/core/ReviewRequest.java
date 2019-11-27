package edu.asu.conceptpower.core;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReviewRequest {

    private String id;
    private String conceptId;
    private String request;
    private ReviewStatus status;
    private String requester;
    private String resolver;
    private List<Comment> comments;
    private OffsetDateTime createdTime;
    
    
    public ReviewRequest() {
        comments = new ArrayList<>();
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
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
    
    public List<Comment> getComments() {
        return comments;
    }
    
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    
    public OffsetDateTime getCreatedAt() {
        return createdTime;
    }
    
    public void setCreatedAt(OffsetDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
