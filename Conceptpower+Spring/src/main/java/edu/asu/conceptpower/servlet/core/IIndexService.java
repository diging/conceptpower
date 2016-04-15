package edu.asu.conceptpower.servlet.core;

import java.util.Map;

import edu.asu.conceptpower.servlet.exceptions.IndexerRunningException;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;

public interface IIndexService {

    public ConceptEntry[] searchForConcepts(Map<String, String> fieldMap, String operator)
            throws LuceneException, IllegalAccessException,IndexerRunningException;

    public void insertConcept(ConceptEntry entry) throws IllegalAccessException, LuceneException,IndexerRunningException;

    public void deleteById(String id) throws LuceneException, IndexerRunningException;

    public void deleteIndexes() throws LuceneException, IndexerRunningException;

    public void indexConcepts() throws LuceneException, IllegalArgumentException, IllegalAccessException, IndexerRunningException;
    
    public boolean isIndexerRunning();
    
    public IndexingEvent getTotalNumberOfWordsIndexed();
    
    public void updateConceptById(ConceptEntry entry)
            throws LuceneException, IndexerRunningException, IllegalAccessException;
}
