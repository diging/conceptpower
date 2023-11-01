package edu.asu.conceptpower.app.lucene;

import java.util.List;
import java.util.Map;

import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.util.CCPSort;
import edu.asu.conceptpower.core.ConceptEntry;

public interface ILuceneUtility {

    public void deleteById(String id, String userName) throws LuceneException;

    public void insertConcept(ConceptEntry entry, String userName) throws LuceneException, IllegalAccessException;

    public void deleteIndexes(String userName) throws LuceneException;

    public void indexConcepts(String userName) throws LuceneException, IllegalArgumentException, IllegalAccessException;

    public ConceptEntry[] queryIndex(Map<String, String> fieldMap, String operator, int pageNumber,
            int numberOfRecordsPerPage, CCPSort ccpSort, boolean isSearchOnDescription)
                    throws LuceneException, IllegalAccessException;
    
    public ConceptEntry[] queryIndex(Map<String, String> fieldMap, String operator, int page, int numberOfRecordsPerPage, CCPSort ccpSort,
            boolean isSearchOnDescription, List<String> posList,
            List<String> conceptList) throws LuceneException, IllegalAccessException, IndexerRunningException;

}