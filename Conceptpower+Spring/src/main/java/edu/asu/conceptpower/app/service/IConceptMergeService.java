package edu.asu.conceptpower.app.service;

import java.util.List;

import edu.asu.conceptpower.app.bean.ConceptsMergeBean;
import edu.asu.conceptpower.core.ConceptEntry;

public interface IConceptMergeService {
    public ConceptsMergeBean mergeConcepts(List<ConceptEntry> conceptEntries, ConceptsMergeBean mergeConceptsBean);
}
