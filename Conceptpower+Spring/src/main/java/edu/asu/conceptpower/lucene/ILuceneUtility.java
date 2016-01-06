package edu.asu.conceptpower.lucene;

import edu.asu.conceptpower.core.ConceptEntry;

public interface ILuceneUtility {

    public ConceptEntry[] queryLuceneIndex(String word, String pos, String listName, String id);

    public void deleteById(String id);

    public void insertConcept(ConceptEntry entry);
}
