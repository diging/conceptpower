package edu.asu.conceptpower.servlet.core.impl;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.IndexingEvent;
import edu.asu.conceptpower.servlet.core.IIndexService;
import edu.asu.conceptpower.servlet.exceptions.IndexerRunningException;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.lucene.ILuceneDAO;
import edu.asu.conceptpower.servlet.lucene.impl.LuceneUtility;

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
	private LuceneUtility luceneUtility;
	
	@Autowired
    @Qualifier("luceneDAO")
    private ILuceneDAO dao;
	
	@Value("#{messages['INDEXER_RUNNING']}")
    private String indexerRunning;
	
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
        return luceneUtility.queryIndex(fieldMap, operator, Integer.MIN_VALUE);
	}

    /**
     * This method searches in lucene on a particular condition fed through
     * fieldMap
     */
    @Override
    public ConceptEntry[] searchForConceptByPageNumberAndFieldMap(Map<String, String> fieldMap, String operator,
            int pageNumber) throws LuceneException, IllegalAccessException, IndexerRunningException {

        if (indexerRunningFlag.get()) {
            throw new IndexerRunningException(indexerRunning);
        }

        return luceneUtility.queryIndex(fieldMap, operator, pageNumber);
    }

	/**
	 * This method inserts concepts into lucene
	 * 
	 * @throws IndexerRunningException
	 */
	@Override
	public void insertConcept(ConceptEntry entry)
			throws IllegalAccessException, LuceneException, IndexerRunningException {
		if (indexerRunningFlag.get()) {
			throw new IndexerRunningException(indexerRunning);

		}
		luceneUtility.insertConcept(entry);
	}

	/**
	 * This method deletes the concept in lucene index based on id of the
	 * concept
	 */
	@Override
	public void deleteById(String id) throws LuceneException, IndexerRunningException {
		if (indexerRunningFlag.get()) {
			throw new IndexerRunningException(indexerRunning);
		}
		luceneUtility.deleteById(id);

	}

	/**
	 * This method deletes the index in lucene
	 */
	@Override
	public void deleteIndexes() throws LuceneException, IndexerRunningException {
		if (!indexerRunningFlag.compareAndSet(false, true)) {
			throw new IndexerRunningException(indexerRunning);
		}
		luceneUtility.deleteIndexes();
		indexerRunningFlag.set(false);
	}

	/**
	 * This method indexes concepts in lucene and runs indexer only once
	 */
	@Override
	public void indexConcepts()
			throws LuceneException, IllegalArgumentException, IllegalAccessException, IndexerRunningException {
		if (!indexerRunningFlag.compareAndSet(false, true)) {
			throw new IndexerRunningException(indexerRunning);
		}
		luceneUtility.indexConcepts();
		indexerRunningFlag.set(false);
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
    public void updateConceptEntry(ConceptEntry entry)
            throws LuceneException, IndexerRunningException, IllegalAccessException {
        if (indexerRunningFlag.get()) {
            throw new IndexerRunningException(indexerRunning);
        }
        luceneUtility.deleteById(entry.getId());
        luceneUtility.insertConcept(entry);
    }

    @Override
    public int getTotalNumberOfRecordsForSearch(Map<String, String> fieldMap, String operator)
            throws LuceneException, IllegalAccessException, IndexerRunningException {
        return searchForConcepts(fieldMap, operator).length;
    }

}
