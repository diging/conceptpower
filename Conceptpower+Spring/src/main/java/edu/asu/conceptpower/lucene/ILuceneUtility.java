package edu.asu.conceptpower.lucene;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.exceptions.LuceneException;

public interface ILuceneUtility {

    public ConceptEntry[] queryLuceneIndex(String word, String pos, String listName, String id,String conceptType) throws LuceneException;

    public void deleteById(String id)throws LuceneException;

    public void insertConcept(ConceptEntry entry)throws LuceneException;
    
    public void deleteWordNetConcepts() throws LuceneException;
    
    public boolean deleteUserDefinedConcepts() throws LuceneException;
}
