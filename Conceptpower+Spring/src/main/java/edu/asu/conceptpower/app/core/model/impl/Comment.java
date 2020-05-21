package edu.asu.conceptpower.app.core.model.impl;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import edu.asu.conceptpower.app.core.model.IComment;

@Entity
public class Comment implements IComment, Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = -6643011518760788400L;
    @Id
    private String id;
    private String comment;
    private String createdBy;
    private OffsetDateTime createdAt;
    
    @Override
    public String getId() {
        return id;
    }
   
    @Override
    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public String getComment() {
        return comment;
    }
    
    @Override
    public String getcreatedBy() {
        return createdBy;
    }
    
    @Override
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    @Override
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
}