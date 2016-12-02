package edu.asu.conceptpower.app.lucene;

import java.util.Map;
import java.util.concurrent.Future;

import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.core.ConceptEntry;

public interface ILuceneUtility {

    public void deleteById(String id) throws LuceneException;

    public void insertConcept(ConceptEntry entry) throws LuceneException, IllegalAccessException;

    public void deleteIndexes() throws LuceneException;

    public Future<Integer> indexConcepts() throws LuceneException, IllegalArgumentException, IllegalAccessException;

    public ConceptEntry[] queryIndex(Map<String, String> fieldMap, String operator, int pageNumber,
            int numberOfRecordsPerPage) throws LuceneException, IllegalAccessException;

}