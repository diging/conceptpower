package edu.asu.conceptpower.lucene;

public interface ILuceneIndexManger {

    public void deleteWordNetLuceneDocuments();

    public boolean indexLuceneDocuments();

    public boolean deleteUserDefinedConcepts();
}
