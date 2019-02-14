package edu.asu.conceptpower.core;


public class ReviewRequest {


    private String id;
    private String wordId;
    private String comment;
    private String wordNetId;
    public enum Status {OPENED,RESOLVED,CLOSED};
    private Status status;
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

    public String getWordNetId() {
        return wordNetId;
    }

    public void setWordNetId(String wordNetID) {
        this.wordNetId = wordNetID;
    }
    
    public boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
   

}
