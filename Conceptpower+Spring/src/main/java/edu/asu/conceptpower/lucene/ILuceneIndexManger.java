package edu.asu.conceptpower.lucene;

import edu.asu.conceptpower.exceptions.LuceneException;

public interface ILuceneIndexManger {

    public void deleteIndexes() throws LuceneException;

    public void indexConcepts() throws LuceneException;

}
