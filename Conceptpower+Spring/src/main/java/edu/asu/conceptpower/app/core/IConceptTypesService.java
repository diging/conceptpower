package edu.asu.conceptpower.app.core;

import edu.asu.conceptpower.app.core.impl.ConceptTypesService.ConceptTypes;

public interface IConceptTypesService {

    public ConceptTypes getConceptTypeByConceptId(String id);
}
