package edu.asu.conceptpower.app.core.model;

import java.time.OffsetDateTime;

public interface IComment {
    
    public abstract String getId();
    
    public abstract String getComment();
    
    public abstract String getcreatedBy();
    
    public abstract OffsetDateTime getCreatedAt();
    
    public abstract void setId(String id);
    
    public abstract void setComment(String comment);
    
    public abstract void setCreatedBy(String createdBy);
    
    public abstract void setCreatedAt(OffsetDateTime createdAt);
}