package edu.asu.conceptpower.servlet.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.servlet.core.ConceptEntry;
import edu.asu.conceptpower.servlet.core.ISearchManager;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.lucene.impl.LuceneUtility;

@Service
public class SearchManager implements ISearchManager {

    @Autowired
    private LuceneUtility luceneUtility;
    

    @Override
    public ConceptEntry[] searchForConceptsConnected(Map<String, String> fieldMap, String operator)
            throws LuceneException, IllegalAccessException {
        return luceneUtility.queryIndex(fieldMap, operator);
    }
}
