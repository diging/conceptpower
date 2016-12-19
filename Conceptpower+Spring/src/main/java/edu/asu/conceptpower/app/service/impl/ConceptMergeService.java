package edu.asu.conceptpower.app.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.bean.ConceptsMergeBean;
import edu.asu.conceptpower.app.core.IConceptTypeManger;
import edu.asu.conceptpower.app.service.IConceptMergeService;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;

@Service
public class ConceptMergeService implements IConceptMergeService {

    @Autowired
    private IConceptTypeManger conceptTypeManager;

    public ConceptsMergeBean mergeConcepts(List<ConceptEntry> conceptEntries, ConceptsMergeBean conceptsMergeBean) {
        // TODO
        // Decide on removing duplicates
        // Decide on which listname and type name to be held
        for (ConceptEntry entry : conceptEntries) {
            conceptsMergeBean.setWord(entry.getWord());
            conceptsMergeBean.getConceptList().add(entry.getConceptList());
            conceptsMergeBean.setDescription(conceptsMergeBean.getDescription() + " " + entry.getDescription());
            conceptsMergeBean.setSynonymsids(conceptsMergeBean.getSynonymsids() + "," + entry.getSynonymIds());
            conceptsMergeBean.getConceptTypeIdList().add(entry.getTypeId());
            conceptsMergeBean.setEquals(conceptsMergeBean.getEquals() + " " + entry.getEqualTo());
            conceptsMergeBean.setSimilar(conceptsMergeBean.getSimilar() + " " + entry.getSimilarTo());
        }

        // Fetch the types with the type id
        conceptsMergeBean.setTypes(getTypesByTypeId(conceptsMergeBean.getConceptTypeIdList()));
        return conceptsMergeBean;
    }

    private ConceptType[] getTypesByTypeId(Set<String> conceptTypesId) {
        ConceptType[] types = new ConceptType[conceptTypesId.size()];
        int i = 0;
        for (String typeId : conceptTypesId) {
            types[i] = conceptTypeManager.getType(typeId);
            i++;
        }
        return types;
    }
}
