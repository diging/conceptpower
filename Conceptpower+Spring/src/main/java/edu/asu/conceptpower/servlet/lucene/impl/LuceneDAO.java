package edu.asu.conceptpower.servlet.lucene.impl;

import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.config.TSerializable;

import edu.asu.conceptpower.servlet.core.IndexingEvent;
import edu.asu.conceptpower.servlet.lucene.ILuceneDAO;

/**
 * This class access db40 database to store the lucene details such as number of
 * words indexed and time of indexing
 * 
 * @author karthikeyanmohan
 *
 */
public class LuceneDAO implements ILuceneDAO {

    ObjectContainer luceneClient;

    private String dbPath;

    @PostConstruct
    public void init() {

        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        config.common().objectClass(Timestamp.class).translate(new TSerializable());
        luceneClient = Db4oEmbedded.openFile(config, getDbPath());
    }

    /**
     * Stores the number of indexed word count along with the timestamp
     */
    public void storeValues(long numberOfIndexedWords,String action) {
        long now = System.currentTimeMillis();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        IndexingEvent bean = new IndexingEvent(new Timestamp(now), numberOfIndexedWords,action,auth.getName());
        luceneClient.store(bean);
        luceneClient.commit();
    }

    /**
     * Retrieves the total number of words from the database
     */
    public IndexingEvent getTotalNumberOfWordsIndexed() {
        IndexingEvent bean = new IndexingEvent(0);
        ObjectSet result = luceneClient.queryByExample(bean);
        return getTotalNumberFromResult(result);

    }

    /**
     * Retrives the total number of indexes and latest timestamp of indexing
     * from the database result object
     * 
     * @param result
     * @return
     */
    private IndexingEvent getTotalNumberFromResult(ObjectSet result) {
        int totalIndex = 0;
        Date latestTimeStamp = null;
        for (Object item : result) {
            IndexingEvent bean = (IndexingEvent) item;
            totalIndex += bean.getIndexedWordsCount();

            Date t = bean.getLastRunDate();

            if (latestTimeStamp == null) {
                latestTimeStamp = t;
            }

            else if (t.after(latestTimeStamp)) {
                latestTimeStamp = t;
            }
        }
        return new IndexingEvent(latestTimeStamp, totalIndex);
    }

    public String getDbPath() {
        return dbPath;
    }

    public void setDbPath(String dbPath) {
        this.dbPath = dbPath;
    }

}
