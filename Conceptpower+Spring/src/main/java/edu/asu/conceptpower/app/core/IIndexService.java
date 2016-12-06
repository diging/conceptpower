package edu.asu.conceptpower.app.core;

import java.util.Map;

import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.IndexingEvent;

public interface IIndexService {

    public ConceptEntry[] searchForConcepts(Map<String, String> fieldMap, String operator)
            throws LuceneException, IllegalAccessException,IndexerRunningException;

    public void insertConcept(ConceptEntry entry) throws IllegalAccessException, LuceneException,IndexerRunningException;

    public void deleteById(String id) throws LuceneException, IndexerRunningException;

    public void deleteIndexes() throws LuceneException, IndexerRunningException;

    public void indexConcepts() throws LuceneException, IllegalArgumentException, IllegalAccessException, IndexerRunningException;
    
    public boolean isIndexerRunning();
    
    public IndexingEvent getTotalNumberOfWordsIndexed();

    public void updateConceptEntry(ConceptEntry entry)
            throws LuceneException, IndexerRunningException, IllegalAccessException;

    public ConceptEntry[] searchForConceptByPageNumberAndFieldMap(Map<String, String> fieldMap,
            String operator, int pageNumber, int numberOfRecordsPerPage)
                    throws LuceneException, IllegalAccessException, IndexerRunningException;

    public int getTotalNumberOfRecordsForSearch(Map<String, String> fieldMap, String operator)
            throws LuceneException, IllegalAccessException, IndexerRunningException;

    public boolean isIndexerRunningStatus();

}
