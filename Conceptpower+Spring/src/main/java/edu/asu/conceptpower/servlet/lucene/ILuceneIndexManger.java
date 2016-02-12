package edu.asu.conceptpower.servlet.lucene;

import edu.asu.conceptpower.servlet.exceptions.LuceneException;

public interface ILuceneIndexManger {

    public void deleteIndexes() throws LuceneException;

    public void indexConcepts() throws LuceneException;

}
