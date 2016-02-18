package edu.asu.conceptpower.servlet.core;

import java.io.Serializable;

public class LuceneBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String lastRun;
    private long indexedWordsCount;

    public String getLastRun() {
        return lastRun;
    }

    public void setLastRun(String lastRun) {
        this.lastRun = lastRun;
    }

    public long getIndexedWordsCount() {
        return indexedWordsCount;
    }

    public void setIndexedWordsCount(long indexedWordsCount) {
        this.indexedWordsCount = indexedWordsCount;
    }

}
