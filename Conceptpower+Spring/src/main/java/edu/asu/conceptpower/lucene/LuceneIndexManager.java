package edu.asu.conceptpower.lucene;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.exceptions.LuceneException;

@Service
public class LuceneIndexManager implements ILuceneIndexManger {

    @Autowired
    private ILuceneUtility luceneUtility;
    
    @Autowired
    private ILuceneIndexUtility luceneIndexUtility;

    public void deleteWordNetConcepts() throws LuceneException {
        luceneUtility.deleteWordNetConcepts();
    }

    public void indexConcepts() throws LuceneException {
        luceneIndexUtility.indexConcepts();
    }

    @Override
    public boolean deleteUserDefinedConcepts() throws LuceneException {
        return luceneUtility.deleteUserDefinedConcepts();

    }
}
