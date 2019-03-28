package edu.asu.conceptpower.app.db;

import java.util.List;

import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.app.model.ConceptList;
import edu.asu.conceptpower.app.mysql.IConceptDBManager;

public class DatabaseMySqlClient implements IConceptDBManager {

    @Override
    public ConceptEntry getEntry(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Object> queryByExample(Object example) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConceptEntry[] getEntriesByFieldContains(String field, String containsString) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConceptEntry[] getEntriesForWord(String word, String pos) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConceptEntry[] getSynonymsPointingToId(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConceptEntry[] getEntriesForWord(String word) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConceptList getConceptList(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<?> getAllElementsOfType(Class<?> clazz) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ConceptEntry> getAllEntriesFromList(String conceptList, int pageNo, int pageSize, String sortBy,
            int sortDirection) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void store(Object element, String databasename) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(ConceptEntry entry, String databasename) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteConceptList(String name) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(ConceptList list, String listname, String databasename) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<ConceptEntry> getAllEntriesFromList(String listname) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ConceptEntry> getAllEntriesByTypeId(String typeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ConceptEntry> getWrapperEntryByWordnetId(String wordnetId) {
        // TODO Auto-generated method stub
        return null;
    }

}
