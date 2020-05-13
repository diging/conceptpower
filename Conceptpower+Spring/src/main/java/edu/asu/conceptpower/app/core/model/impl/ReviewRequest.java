package edu.asu.conceptpower.app.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Index;

import edu.asu.conceptpower.app.core.model.IReviewRequest;
import edu.asu.conceptpower.core.ReviewStatus;

/**
 * 
 * @author Keerthivasan
 * 
 */

@Table(
        name="",
        indexes= {
        @Index(name="conceptId", columnList=""),
        @Index(name="request", columnList=""),
        @Index(name="status", columnList=""),
        @Index(name="requester", columnList=""),
        @Index(name="resolver", columnList=""),
})
public class ReviewRequest implements IReviewRequest{

    @Id private String id;
    
    private String conceptId;
    private String request;
    private ReviewStatus status;
    private String requester;
    private String resolver;
    
    
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
}