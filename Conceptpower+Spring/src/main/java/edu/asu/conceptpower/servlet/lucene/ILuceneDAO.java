package edu.asu.conceptpower.servlet.lucene;

import edu.asu.conceptpower.servlet.core.LuceneBean;

public interface ILuceneDAO {

    public void storeValues(long numberOfIndexedWords);

    public LuceneBean getTotalNumberOfWordsIndexed();
}
