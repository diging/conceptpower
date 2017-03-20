package edu.asu.conceptpower.app.core;

import edu.asu.conceptpower.app.core.impl.ConceptTypesService.ConceptTypes;

public interface IConceptTypesService {

    /**
     * This method returns the concept type based on the id parameter.
     * 
     * @param id
     * @return ConceptTypes
     */
    public ConceptTypes getConceptTypeByConceptId(String id);
}
