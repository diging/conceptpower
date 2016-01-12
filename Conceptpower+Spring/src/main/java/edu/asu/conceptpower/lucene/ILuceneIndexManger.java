package edu.asu.conceptpower.lucene;

import edu.asu.conceptpower.exceptions.LuceneException;

public interface ILuceneIndexManger {

    public void deleteWordNetConcepts() throws LuceneException;

    public void indexConcepts() throws LuceneException;

    public boolean deleteUserDefinedConcepts() throws LuceneException;
}
