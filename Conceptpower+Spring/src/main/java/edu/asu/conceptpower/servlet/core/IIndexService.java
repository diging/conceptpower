package edu.asu.conceptpower.servlet.core;

import java.util.Map;

import edu.asu.conceptpower.servlet.exceptions.IndexerRunningException;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;

public interface IIndexService {

    public ConceptEntry[] searchForConcepts(Map<String, String> fieldMap, String operator)
            throws LuceneException, IllegalAccessException,IndexerRunningException;

    public boolean insertConcept(ConceptEntry entry) throws IllegalAccessException, LuceneException;

    public boolean deleteById(String id) throws LuceneException;

    public boolean deleteIndexes() throws LuceneException;

    public boolean indexConcepts() throws LuceneException, IllegalArgumentException, IllegalAccessException;
    
}
