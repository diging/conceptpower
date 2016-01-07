package edu.asu.conceptpower.lucene;

import edu.asu.conceptpower.core.ConceptEntry;

public interface ILuceneUtility {

    public ConceptEntry[] queryLuceneIndex(String word, String pos, String listName, String id,String conceptType);

    public void deleteById(String id);

    public void insertConcept(ConceptEntry entry);
    
    public void deleteWordNetConcepts();
    
    public boolean indexLuceneDocuments();
    
    public boolean deleteUserDefinedConcepts();
}
