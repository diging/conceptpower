package edu.asu.conceptpower.servlet.core;

import java.io.Serializable;
import java.sql.Timestamp;
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

    public IndexingEvent() {

    }

    public IndexingEvent(Date run, long indexWordCount, String action) {
        lastRun = run;
        indexedWordsCount = indexWordCount;
        this.action = action;
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

}
