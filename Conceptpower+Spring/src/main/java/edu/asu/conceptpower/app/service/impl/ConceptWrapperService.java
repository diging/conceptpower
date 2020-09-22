package edu.asu.conceptpower.app.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.IConceptListService;
import edu.asu.conceptpower.app.core.IConceptTypeService;
import edu.asu.conceptpower.app.core.model.impl.ConceptList;
import edu.asu.conceptpower.app.core.model.impl.ConceptType;
import edu.asu.conceptpower.app.service.IConceptWrapperService;

/**
 * This class acts as a helper class for ConceptWrapperAddController. This class
 * helps for fetching the concept list and concept type details within the
 * Conceptpower system.
 * 
 * @author karthikeyanmohan
 *
 */
@Service
public class ConceptWrapperService implements IConceptWrapperService {

    @Autowired
    private IConceptListService conceptListService;

    @Autowired
    private IConceptTypeService conceptTypeService;

    /**
     * This method fetches all the concept types within the Conceptpower system
     * and returns a map containing the concept type id as the key and concept
     * type name as the value.
     * 
     * If there are no ConceptTypes in the Conceptpower system, then an empty
     * map is returned.
     */
    @Override
    public Map<String, String> fetchAllConceptTypes() {
        List<ConceptType> allTypes = conceptTypeService.getAllTypes();
        Map<String, String> types = new LinkedHashMap<>();
        if (allTypes == null || allTypes.isEmpty()) {
            return types;
        }
        
        for (ConceptType conceptType : allTypes) {
            types.put(conceptType.getTypeId(), conceptType.getTypeName());
        }
        return types;
    }

    /**
     * This method fetches all the concept lists within the Conceptpower system
     * and returns a map containing the concept list name as the key and concept
     * list name as the value as well.
     * 
     * If there are no ConceptLists in the Conceptpower system, then an empty
     * map is returned.
     * 
     * @return
     */
    @Override
    public Map<String, String> fetchAllConceptLists() {
        List<ConceptList> allLists = conceptListService.getAllConceptLists();
        Map<String, String> lists = new LinkedHashMap<>();
        if (allLists == null || allLists.isEmpty()) {
            return lists;
        }
        for (ConceptList conceptList : allLists) {
            lists.put(conceptList.getConceptListName(), conceptList.getConceptListName());
        }
        return lists;
    }

}
