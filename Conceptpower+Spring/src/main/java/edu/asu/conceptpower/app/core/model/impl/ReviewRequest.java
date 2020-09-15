package edu.asu.conceptpower.app.core.model.impl;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import edu.asu.conceptpower.core.ReviewStatus;

/**
 * This model helps to keep track of information about a specific review request
 * 
 * @author Keerthivasan Krishnamurthy
 * 
 */

@Entity
@Table(name="review_request")
public class ReviewRequest implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="review_id")
    private String id;
    
    @Column(name="concept_id")
    private String conceptId;
    
    @Column(name="request_desc")
    private String request;
    
    @Column(name="status")
    private ReviewStatus status;
    
    @Column(name="requestor")
    private String requester;
    
    @Column(name="resolver")
    private String resolver;
    
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<Comment> comments;
    
    @Column(name="created_at")
    private OffsetDateTime createdAt;
    
    
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

    public String getId() {
        return id;
    }
  
    public void setId(String id) {
        this.id = id;
    } 
    
    public List<Comment> getComments(){
        return this.comments;
    }
    
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
      
    public OffsetDateTime getCreatedAt() {
       return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
       this.createdAt = createdAt;
    }  
}