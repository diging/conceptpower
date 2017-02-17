package edu.asu.conceptpower.app.service;

import java.util.List;

import edu.asu.conceptpower.app.bean.ConceptsMergeBean;
import edu.asu.conceptpower.app.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.app.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.core.ConceptEntry;

public interface IConceptMergeService {
    public ConceptsMergeBean prepareMergeConcepts(List<ConceptEntry> conceptEntries,
            ConceptsMergeBean mergeConceptsBean);

    public void mergeConcepts(ConceptsMergeBean conceptsMergeBean, String userName) throws LuceneException,
            IndexerRunningException, IllegalAccessException, DictionaryDoesNotExistException, DictionaryModifyException;

}
