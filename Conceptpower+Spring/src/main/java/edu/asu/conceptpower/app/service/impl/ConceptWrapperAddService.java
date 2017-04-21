package edu.asu.conceptpower.app.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.IConceptListManager;
import edu.asu.conceptpower.app.core.IConceptTypeManger;
import edu.asu.conceptpower.app.service.IConceptWrapperAddService;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptType;

@Service
public class ConceptWrapperAddService implements IConceptWrapperAddService {

    @Autowired
    private IConceptListManager conceptListManager;

    @Autowired
    private IConceptTypeManger conceptTypesManager;

    @Override
    public Map<String, String> fetchAllConceptTypes() {
        ConceptType[] allTypes = conceptTypesManager.getAllTypes();
        Map<String, String> types = new LinkedHashMap<String, String>();
        for (ConceptType conceptType : allTypes) {
            types.put(conceptType.getTypeId(), conceptType.getTypeName());
        }
        return types;
    }

    @Override
    public Map<String, String> fetchAllConceptLists() {
        List<ConceptList> allLists = conceptListManager.getAllConceptLists();
        Map<String, String> lists = new LinkedHashMap<String, String>();
        for (ConceptList conceptList : allLists) {
            lists.put(conceptList.getConceptListName(), conceptList.getConceptListName());
        }
        return lists;
    }

}
