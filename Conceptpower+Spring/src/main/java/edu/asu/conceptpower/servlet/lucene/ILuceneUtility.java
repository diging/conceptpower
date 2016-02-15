package edu.asu.conceptpower.servlet.lucene;

import java.util.Map;

import edu.asu.conceptpower.servlet.core.ConceptEntry;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;

public interface ILuceneUtility {

    public void deleteById(String id)throws LuceneException;

    public void insertConcept(ConceptEntry entry)throws LuceneException, IllegalAccessException;
    
    public void deleteIndexes() throws LuceneException;
    
    public void indexConcepts() throws LuceneException, IllegalArgumentException, IllegalAccessException ;
    
    public ConceptEntry[] queryIndex(Map<String, String> fieldMap,String operator) throws LuceneException, IllegalAccessException;
    
}