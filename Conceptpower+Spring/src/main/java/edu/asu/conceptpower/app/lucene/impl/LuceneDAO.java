package edu.asu.conceptpower.app.lucene.impl;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.conceptpower.app.lucene.ILuceneDAO;
import edu.asu.conceptpower.app.model.IndexingEvent;
import edu.asu.conceptpower.app.repository.IIndexRepository;

/**
 * This class access db40 database to store the lucene details such as number of
 * words indexed and time of indexing
 * 
 * @author karthikeyanmohan
 *
 */
public class LuceneDAO implements ILuceneDAO {
    

    private String dbPath;
    @Autowired
    private IIndexRepository luceneClient;
    /**
     * Stores the number of indexed word count along with the timestamp
     */
    public void storeValues(IndexingEvent bean) {
        luceneClient.save(bean);
    }

    /**
     * Retrieves the total number of words from the database
     */
    public IndexingEvent getTotalNumberOfWordsIndexed() {
        Iterable<IndexingEvent> result = luceneClient.findAll();
        return getTotalNumberFromResult(result);

    }

    /**
     * Retrives the total number of indexes and latest timestamp of indexing
     * from the database result object
     * 
     * @param result
     * @return
     */
    private IndexingEvent getTotalNumberFromResult(Iterable<IndexingEvent> result) {
        int totalIndex = 0;
        Date latestTimeStamp = null;
        for (IndexingEvent bean : result) {
            totalIndex += bean.getIndexedWordsCount();
            latestTimeStamp = bean.getLastRunDate();
            
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
