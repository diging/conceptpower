package edu.asu.conceptpower.servlet.core;

import java.io.Serializable;
import java.sql.Timestamp;

public class LuceneBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Timestamp lastRun;
    private long indexedWordsCount;
    private String message;
    
    public LuceneBean(){
        
    }

    public LuceneBean(Timestamp run, long indexWordCount) {
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
        if(lastRun == null)
            return "";
        return lastRun.toString();
    }
    
    public Timestamp getLastRunTimeStamp(){
        return lastRun;
    }

    public void setLastRun(Timestamp lastRun) {
        this.lastRun = lastRun;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
