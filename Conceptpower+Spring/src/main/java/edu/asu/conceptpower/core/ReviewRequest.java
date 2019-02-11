package edu.asu.conceptpower.core;


public class ReviewRequest {


    private String id;
    private String wordId;
    private String comment;
    private String requester;
    private String resolver;
    private String wordNetId;
    public enum Status {OPENED,RESOLVED,CLOSED};
    private Status status;
    private Boolean reviewFlag = new Boolean(false); // Flag is set to true if any comment has been posted on the concept.
    private boolean isDeleted;
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getWordId() {
        return wordId;
    }

    public void setWordId(String word) {
        this.wordId = word;
    }
    
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getWordNetId() {
        return wordNetId;
    }

    public void setWordNetId(String wordNetID) {
        this.wordNetId = wordNetID;
    }
    
    public Boolean isReviewFlag() {
        return reviewFlag;
    }

    public void setReviewFlag(Boolean reviewFlag) {
        this.reviewFlag = reviewFlag;
    }

    public boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
   

}
