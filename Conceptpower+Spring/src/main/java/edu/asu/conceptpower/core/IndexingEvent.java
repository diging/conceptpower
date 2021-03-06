package edu.asu.conceptpower.core;

import java.io.Serializable;
import java.util.Date;

public class IndexingEvent implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Date lastRun;
    private long indexedWordsCount;
    private String message;
    private String action;
    private String userName;

    public IndexingEvent() {

    }

    public IndexingEvent(Date run, long indexWordCount, String action, String userName) {
        lastRun = run;
        indexedWordsCount = indexWordCount;
        this.action = action;
        this.userName = userName;
    }
    
    public IndexingEvent(long indexWordCount){
        this.indexedWordsCount = indexedWordsCount;
    }

    public IndexingEvent(Date run, long indexWordCount) {
        lastRun = run;
        indexedWordsCount = indexWordCount;
    }

    public long getIndexedWordsCount() {
        return indexedWordsCount;
    }

    public void setIndexedWordsCount(long indexedWordsCount) {
        this.indexedWordsCount = indexedWordsCount;
    }

    public String getLastRun() {
        if (lastRun == null)
            return "";
        return lastRun.toString();
    }

    public Date getLastRunDate() {
        return lastRun;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLastRun(Date lastRun) {
        this.lastRun = lastRun;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
