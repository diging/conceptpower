package edu.asu.conceptpower.app.lucene;

import edu.asu.conceptpower.app.model.IndexingEvent;

public interface ILuceneDAO {

    public void storeValues(IndexingEvent bean);

    public IndexingEvent getTotalNumberOfWordsIndexed();
}
