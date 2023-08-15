package edu.asu.conceptpower.app.model;

import java.io.Serializable;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * This model helps in storing the information a specific comment
 * 
 * @author Keerthivasan Krishnamurthy, Digital Innovation Group
 * 
 */
@Entity
@Table(name="comment")
public class Comment implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name="comment_id")
    private String id;
    
    @Column(name="comment_desc")
    private String commentDesc;
    
    @Column(name="created_by")
    private String createdBy;
    
    @Column(name="created_at")
    private OffsetDateTime createdAt;
    
    @ManyToOne
    private ReviewRequest review;
    
    public String getId() {
        return id;
    }
   
    public void setId(String id) {
        this.id = id;
    }
    
    public String getComment() {
        return commentDesc;
    }
    
    public String getcreatedBy() {
        return createdBy;
    }
    
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setComment(String commentDesc) {
        this.commentDesc = commentDesc;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public ReviewRequest getConcept() {
        return this.review;
    }
    
    public void setConcept(ReviewRequest r) {
        this.review = r;
    }
    
}