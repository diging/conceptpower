package edu.asu.conceptpower.servlet.lucene.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.lucene.ILuceneIndexManger;
import edu.asu.conceptpower.servlet.lucene.ILuceneUtility;

@Service
public class LuceneIndexManager implements ILuceneIndexManger {

    @Autowired
    private ILuceneUtility luceneUtility;
    
    public void deleteIndexes() throws LuceneException {
        luceneUtility.deleteIndexes();
    }

    public void indexConcepts() throws LuceneException, IllegalArgumentException, IllegalAccessException {
        luceneUtility.indexConcepts();
    }
}
