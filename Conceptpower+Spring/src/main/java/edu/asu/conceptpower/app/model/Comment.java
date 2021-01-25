package edu.asu.conceptpower.app.model;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This model helps in storing the information a specific comment
 * 
 * @author Keerthivasan Krishnamurthy
 * 
 */
@Entity
@Table(name="comment")
public class Comment implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name="comment_desc")
    private String commentDesc;
    
    @Column(name="created_by")
    private String createdBy;
    
    @Column(name="created_at")
    private OffsetDateTime createdAt;
    
    @Column(name="review_id")
    private String reviewId;
    
    public Integer getId() {
        return id;
    }
   
    public void setId(Integer id) {
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
    
    public String getReviewId() {
        return this.reviewId;
    }
    
    public void setReviewId(String id) {
        this.reviewId = id;
    }
    
}