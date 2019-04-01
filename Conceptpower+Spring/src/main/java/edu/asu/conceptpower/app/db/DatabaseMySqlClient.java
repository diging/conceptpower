package edu.asu.conceptpower.app.db;

import java.util.List;

import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.app.model.ConceptList;
import edu.asu.conceptpower.app.mysql.IConceptDBManager;

public class DatabaseMySqlClient implements IConceptDBManager {

    @Override
    public ConceptEntry getEntry(String id) {
       
        return null;
    }

    @Override
    public List<Object> queryByExample(Object example) {
       
        return null;
    }

    @Override
    public ConceptEntry[] getEntriesByFieldContains(String field, String containsString) {
       
        return null;
    }

    @Override
    public ConceptEntry[] getEntriesForWord(String word, String pos) {
       
        return null;
    }

    @Override
    public ConceptEntry[] getSynonymsPointingToId(String id) {
       
        return null;
    }

    @Override
    public ConceptEntry[] getEntriesForWord(String word) {
       
        return null;
    }

    @Override
    public ConceptList getConceptList(String name) {
       
        return null;
    }

    @Override
    public List<?> getAllElementsOfType(Class<?> clazz) {
       
        return null;
    }

    @Override
    public List<ConceptEntry> getAllEntriesFromList(String conceptList, int pageNo, int pageSize, String sortBy,
            int sortDirection) {
       
        return null;
    }

    @Override
    public void store(Object element, String databasename) {
       
        
    }

    @Override
    public void update(ConceptEntry entry, String databasename) {
       
        
    }

    @Override
    public void deleteConceptList(String name) {
       
        
    }

    @Override
    public void update(ConceptList list, String listname, String databasename) {
       
        
    }

    @Override
    public List<ConceptEntry> getAllEntriesFromList(String listname) {
       
        return null;
    }

    @Override
    public List<ConceptEntry> getAllEntriesByTypeId(String typeId) {
       
        return null;
    }

    @Override
    public List<ConceptEntry> getWrapperEntryByWordnetId(String wordnetId) {
       
        return null;
    }

}
