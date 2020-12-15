package edu.asu.conceptpower.app.manager;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.lucene.ILuceneDAO;
import edu.asu.conceptpower.app.lucene.ILuceneUtility;
import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.app.util.CCPSort;
import edu.asu.conceptpower.app.model.IndexingEvent;

/**
 * This class acts as a single point of access to lucene. This class performs
 * searching, insertion, deletion and indexing in lucene
 * 
 * @author karthikeyanmohan
 *
 */
@Service
public class IndexService implements IIndexService {

    @Autowired
    private ILuceneUtility luceneUtility;

    @Autowired
    @Qualifier("luceneDAO")
    private ILuceneDAO dao;

    @Value("#{messages['INDEXER_RUNNING']}")
    private String indexerRunning;

    @Autowired
    private IAlternativeIdService alternativeIdService;

    /**
     * This field makes sure indexer runs only once when two different admins
     * gives index command at same time
     */
    private AtomicBoolean indexerRunningFlag = new AtomicBoolean(false);

    /**
     * This method searches in lucene on a particular condition fed through
     * fieldMap
     */
    @Override
    public ConceptEntry[] searchForConcepts(Map<String, String> fieldMap, String operator)
            throws LuceneException, IllegalAccessException, IndexerRunningException {

        if (indexerRunningFlag.get()) {
            throw new IndexerRunningException(indexerRunning);
        }
        // Fetches all the pages
        return luceneUtility.queryIndex(fieldMap, operator, 0, -1, null);
    }

    /**
     * This method searches in lucene on a particular condition fed through
     * fieldMap
     */
    @Override
    public ConceptEntry[] searchForConceptByPageNumberAndFieldMap(Map<String, String> fieldMap, String operator,
            int pageNumber, int numberOfRecordsPerPage, CCPSort sort)
            throws LuceneException, IllegalAccessException, IndexerRunningException {

        if (indexerRunningFlag.get()) {
            throw new IndexerRunningException(indexerRunning);
        }

        ConceptEntry[] entries = luceneUtility.queryIndex(fieldMap, operator, pageNumber, numberOfRecordsPerPage, sort);
        alternativeIdService.addAlternativeIds(entries);
        return entries;
    }

    /**
     * This method inserts concepts into lucene
     * 
     * @throws IndexerRunningException
     */
    @Override
    public void insertConcept(ConceptEntry entry, String userName)
            throws IllegalAccessException, LuceneException, IndexerRunningException {
        if (indexerRunningFlag.get()) {
            throw new IndexerRunningException(indexerRunning);

        }
        luceneUtility.insertConcept(entry, userName);
    }

    /**
     * This method deletes the concept in lucene index based on id of the
     * concept
     */
    @Override
    public void deleteById(String id, String userName) throws LuceneException, IndexerRunningException {
        if (indexerRunningFlag.get()) {
            throw new IndexerRunningException(indexerRunning);
        }
        luceneUtility.deleteById(id, userName);

    }

    /**
     * This method deletes the index in lucene
     */
    @Override
    public void deleteIndexes(String userName) throws LuceneException, IndexerRunningException {
        if (!indexerRunningFlag.compareAndSet(false, true)) {
            throw new IndexerRunningException(indexerRunning);
        }
        luceneUtility.deleteIndexes(userName);
        indexerRunningFlag.set(false);
    }

    /**
     * This method indexes concepts in lucene and runs indexer only once
     */
    @Override
    @Async("indexExecutor")
    public void indexConcepts(String userName)
            throws LuceneException, IllegalArgumentException, IllegalAccessException, IndexerRunningException {
        if (!indexerRunningFlag.compareAndSet(false, true)) {
            throw new IndexerRunningException(indexerRunning);
        }
        try {
            luceneUtility.indexConcepts(userName);
        } finally {
            indexerRunningFlag.set(false);
        }
    }

    @Override
    public boolean isIndexerRunning() {
        return indexerRunningFlag.get();
    }

    @Override
    public IndexingEvent getTotalNumberOfWordsIndexed() {
        return dao.getTotalNumberOfWordsIndexed();
    }

    /**
     * This method updates the concept in lucene index by deleting the concept
     * entry based on the id and inserting the concept entry
     * 
     * @throws IllegalAccessException
     */
    @Override
    public void updateConceptEntry(ConceptEntry entry, String userName)
            throws LuceneException, IndexerRunningException, IllegalAccessException {
        if (indexerRunningFlag.get()) {
            throw new IndexerRunningException(indexerRunning);
        }
        luceneUtility.deleteById(entry.getId(), userName);
        luceneUtility.insertConcept(entry, userName);
    }

    @Override
    public int getTotalNumberOfRecordsForSearch(Map<String, String> fieldMap, String operator)
            throws LuceneException, IllegalAccessException, IndexerRunningException {
        return searchForConcepts(fieldMap, operator).length;
    }

}
