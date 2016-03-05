package edu.asu.conceptpower.servlet.core.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.servlet.core.ConceptEntry;
import edu.asu.conceptpower.servlet.core.IIndexService;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.lucene.impl.LuceneUtility;

/**
 * This class acts as a single point of access to lucene. This class performs
 * searching, insertion, deletion and indexing in lucene
 * 
 * @author karthikeyanmohan
 *
 */
@Service
public class IndexService implements IIndexService {

    @Autowired
    private LuceneUtility luceneUtility;

    /**
     * This method searches in lucene on a particular condition fed through
     * fieldMap
     */
    @Override
    public ConceptEntry[] searchForConceptsConnected(Map<String, String> fieldMap, String operator)
            throws LuceneException, IllegalAccessException {
        return luceneUtility.queryIndex(fieldMap, operator);
    }

    /**
     * This method inserts concepts into lucene
     */
    @Override
    public void insertConcept(ConceptEntry entry) throws IllegalAccessException, LuceneException {
        luceneUtility.insertConcept(entry);
    }

    /**
     * This method deletes the concept in lucene index based on id of the
     * concept
     */
    @Override
    public void deleteById(String id) throws LuceneException {
        luceneUtility.deleteById(id);
    }

    /**
     * This method deletes the index in lucene
     */
    @Override
    public void deleteIndexes() throws LuceneException {
        luceneUtility.deleteIndexes();
    }

    /**
     * This method indexes concepts in lucene
     */
    @Override
    public void indexConcepts() throws LuceneException, IllegalArgumentException, IllegalAccessException {
        luceneUtility.indexConcepts();
    }
}
