package edu.asu.conceptpower.servlet.core.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.servlet.core.ConceptEntry;
import edu.asu.conceptpower.servlet.core.IIndexService;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.lucene.impl.LuceneUtility;

@Service
public class IndexService implements IIndexService {

    @Autowired
    private LuceneUtility luceneUtility;
    

    @Override
    public ConceptEntry[] searchForConceptsConnected(Map<String, String> fieldMap, String operator)
            throws LuceneException, IllegalAccessException {
        return luceneUtility.queryIndex(fieldMap, operator);
    }

    @Override
    public void insertConcept(ConceptEntry entry) throws IllegalAccessException, LuceneException {
       luceneUtility.insertConcept(entry);
        
    }

    @Override
    public void deleteById(String id) throws LuceneException {
       luceneUtility.deleteById(id);
    }
    
    @Override
    public void deleteIndexes() throws LuceneException {
        luceneUtility.deleteIndexes();
    }
    
    @Override
    public void indexConcepts() throws LuceneException, IllegalArgumentException, IllegalAccessException {
        luceneUtility.indexConcepts();
    }
}
