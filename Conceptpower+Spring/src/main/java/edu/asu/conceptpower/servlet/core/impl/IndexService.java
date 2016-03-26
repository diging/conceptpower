package edu.asu.conceptpower.servlet.core.impl;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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
     * This field makes sure indexer runs only once when two different admins gives index command at same time
     */
    private AtomicBoolean indexerRunningFlag = new AtomicBoolean(false);

    /**
     * This method searches in lucene on a particular condition fed through
     * fieldMap
     */
    @Override
    public ConceptEntry[] searchForConcepts(Map<String, String> fieldMap, String operator)
            throws LuceneException, IllegalAccessException {
        return luceneUtility.queryIndex(fieldMap, operator);
    }

    /**
     * This method inserts concepts into lucene
     */
    @Override
    public boolean insertConcept(ConceptEntry entry) throws IllegalAccessException, LuceneException {
        if (!indexerRunningFlag.get()) {
            luceneUtility.insertConcept(entry);    
            return true;
        }
        return false;
    }

    /**
     * This method deletes the concept in lucene index based on id of the
     * concept
     */
    @Override
    public boolean deleteById(String id) throws LuceneException {
        if (!indexerRunningFlag.get()) {
            luceneUtility.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * This method deletes the index in lucene
     */
    @Override
    public boolean deleteIndexes() throws LuceneException {
        if(indexerRunningFlag.compareAndSet(false, true)){
            luceneUtility.deleteIndexes();
            indexerRunningFlag.set(false);
            return true;
        }
       return false;
    }

    /**
     * This method indexes concepts in lucene and runs indexer only once
     */
    @Override
    public boolean indexConcepts() throws LuceneException, IllegalArgumentException, IllegalAccessException {
        if (indexerRunningFlag.compareAndSet(false, true)) {
            luceneUtility.indexConcepts();
            indexerRunningFlag.set(false);
            return true;
        }
        return false;
    }
    
    public boolean checkStatus(){
        return indexerRunningFlag.get();
    }
}
