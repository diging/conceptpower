package edu.asu.conceptpower.app.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="indexing_event")
public class IndexingEvent implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Column(name="last_run")
    private Date lastRun;
    
    @Column(name="indexed_wordscount")
    private long indexedWordsCount;
    
    @Column(name="message")
    private String message;
    
    @Column(name="action")
    private String action;
    
    @Id
    @Column(name="user_name")
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
        this.indexedWordsCount = indexWordCount;
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
