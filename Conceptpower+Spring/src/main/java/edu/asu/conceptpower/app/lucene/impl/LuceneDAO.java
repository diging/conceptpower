package edu.asu.conceptpower.app.lucene.impl;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import edu.asu.conceptpower.app.db.DatabaseManager;
import edu.asu.conceptpower.app.lucene.ILuceneDAO;
import edu.asu.conceptpower.core.IndexingEvent;

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

    @Autowired
    @Qualifier("luceneDatabaseManager")
    private DatabaseManager luceneDatabase;

    @PostConstruct
    public void init() {
        this.luceneClient = luceneDatabase.getClient();
    }

    /**
     * Stores the number of indexed word count along with the timestamp
     */
    public void storeValues(IndexingEvent bean) {
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
