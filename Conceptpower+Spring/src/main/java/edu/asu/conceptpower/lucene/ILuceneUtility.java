package edu.asu.conceptpower.lucene;

import java.util.List;

import edu.asu.conceptpower.core.ConceptEntry;

public interface ILuceneUtility {

    public ConceptEntry queryById(String id);

    public List<ConceptEntry> queryByListName(String listName);

    public void deleteById(String id);

    public void insertConcept(ConceptEntry entry);
}
