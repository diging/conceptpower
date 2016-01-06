package edu.asu.conceptpower.lucene;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LuceneIndexManager implements ILuceneIndexManger {

    @Autowired
    private ILuceneUtility luceneUtility;

    public void deleteWordNetLuceneDocuments() {
        luceneUtility.deleteWordNetConcepts();
    }

    public boolean indexLuceneDocuments() {
        return luceneUtility.indexLuceneDocuments();
    }

    @Override
    public void deleteUserDefinedConcepts() {
        luceneUtility.deleteUserDefinedConcepts();

    }
}
