package edu.asu.conceptpower.core;

import java.time.OffsetDateTime;

public class Comment{
	private String comment;
	private String createdBy;
	private OffsetDateTime createdAt;
	
	public String getComment() {
	    return comment;
	}
	
	public String getcreatedBy() {
	    return createdBy;
	}
	
	public OffsetDateTime getCreatedAt() {
	    return createdAt;
	}
	
	public void setComment(String comment) {
	    this.comment = comment;
	}
	
	public void setCreatedBy(String createdBy) {
	    this.createdBy = createdBy;
	}
	
	public void setCreatedAt(OffsetDateTime createdAt) {
	    this.createdAt = createdAt;
	}
}