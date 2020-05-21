package edu.asu.conceptpower.app.core.model.impl;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import edu.asu.conceptpower.app.core.model.IReviewRequest;
import edu.asu.conceptpower.core.ReviewStatus;

/**
 * 
 * @author Keerthivasan
 * 
 */
@Entity
public class ReviewRequest implements IReviewRequest, Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id private String id;
    
    private String conceptId;
    private String request;
    private ReviewStatus status;
    private String requester;
    private String resolver;
    private OffsetDateTime createdAt;
    
    @Override
    public String getConceptId() {
        return conceptId;
    }

    @Override
    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
    }  
   
    @Override
    public ReviewStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    @Override
    public String getRequest() {
        return request;
    }

    @Override
    public void setRequest(String request) {
        this.request = request;
    }

    @Override
    public String getRequester() {
        return requester;
    }

    @Override
    public void setRequester(String requester) {
        this.requester = requester;
    }

    @Override
    public String getResolver() {
        return resolver;
    }

    @Override
    public void setResolver(String resolver) {
        this.resolver = resolver;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
        
    }  
    
    @Override
    public OffsetDateTime getCreatedAt() {
       return createdAt;
    }

    @Override
    public void setCreatedAt(OffsetDateTime createdAt) {
       this.createdAt = createdAt;
    }  
}