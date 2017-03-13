package edu.asu.conceptpower.app.core.impl;

import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.IConceptTypesService;

@Service
public class ConceptTypesService implements IConceptTypesService {
    
    public enum ConceptTypes {
        GENERIC_WORDNET_CONCEPT, SPECIFIC_WORDNET_CONCEPT, LOCAL_CONCEPT
    }
    
    public ConceptTypes getConceptTypeByConceptId(String id) {
        if (id == null) {
            return null;
        }
        if (id.contains("??")) {
            return ConceptTypes.GENERIC_WORDNET_CONCEPT;
        }
        if (id.startsWith("WID")) {
            return ConceptTypes.SPECIFIC_WORDNET_CONCEPT;
        }
        return ConceptTypes.LOCAL_CONCEPT;
    }

}
