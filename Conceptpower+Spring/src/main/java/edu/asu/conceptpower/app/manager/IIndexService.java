package edu.asu.conceptpower.app.manager;

import java.util.Map;

import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.app.util.CCPSort;
import edu.asu.conceptpower.app.model.IndexingEvent;

public interface IIndexService {

    public ConceptEntry[] searchForConcepts(Map<String, String> fieldMap, String operator)
            throws LuceneException, IllegalAccessException,IndexerRunningException;

    public void insertConcept(ConceptEntry entry, String userName)
            throws IllegalAccessException, LuceneException, IndexerRunningException;

    public void deleteById(String id, String userName) throws LuceneException, IndexerRunningException;

    public void deleteIndexes(String userName) throws LuceneException, IndexerRunningException;

    public void indexConcepts(String userName)
            throws LuceneException, IllegalArgumentException, IllegalAccessException, IndexerRunningException;
    
    public boolean isIndexerRunning();
    
    public IndexingEvent getTotalNumberOfWordsIndexed();

    public void updateConceptEntry(ConceptEntry entry, String userName)
            throws LuceneException, IndexerRunningException, IllegalAccessException;

    public ConceptEntry[] searchForConceptByPageNumberAndFieldMap(Map<String, String> fieldMap, String operator,
            int pageNumber, int numberOfRecordsPerPage, CCPSort sort)
                    throws LuceneException, IllegalAccessException, IndexerRunningException;

    public int getTotalNumberOfRecordsForSearch(Map<String, String> fieldMap, String operator)
            throws LuceneException, IllegalAccessException, IndexerRunningException;

}
