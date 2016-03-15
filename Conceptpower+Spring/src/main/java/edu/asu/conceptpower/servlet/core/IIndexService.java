package edu.asu.conceptpower.servlet.core;

import java.util.Map;

import edu.asu.conceptpower.servlet.exceptions.LuceneException;

public interface IIndexService {

    public ConceptEntry[] searchForConceptsConnected(Map<String, String> fieldMap, String operator)
            throws LuceneException, IllegalAccessException;

    public void insertConcept(ConceptEntry entry) throws IllegalAccessException, LuceneException;

    public void deleteById(String id) throws LuceneException;

    public void deleteIndexes() throws LuceneException;

    public void indexConcepts() throws LuceneException, IllegalArgumentException, IllegalAccessException;

}
