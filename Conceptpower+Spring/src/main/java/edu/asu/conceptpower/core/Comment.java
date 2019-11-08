package edu.asu.conceptpower.core;

import java.time.LocalDateTime;

public class Comment{
	private String comment;
	private String createdBy;
	private LocalDateTime createdAt;
	
	public String getComment() {
	    return comment;
	}
	
	public String getcreatedBy() {
	    return createdBy;
	}
	
	public LocalDateTime getCreatedAt() {
	    return createdAt;
	}
	
	public void setComment(String comment) {
	    this.comment = comment;
	}
	
	public void setCreatedBy(String createdBy) {
	    this.createdBy = createdBy;
	}
	
	public void setCreatedAt(LocalDateTime createdAt) {
	    this.createdAt = createdAt;
	}
}