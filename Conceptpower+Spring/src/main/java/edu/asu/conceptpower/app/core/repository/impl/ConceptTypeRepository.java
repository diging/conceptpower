package edu.asu.conceptpower.app.core.repository.impl;

import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.core.model.impl.ConceptType;
import edu.asu.conceptpower.app.core.repository.AbstractJPA;
import edu.asu.conceptpower.app.core.repository.IConceptTypeRepository;

/**
 * 
 * @author Keerthivasan
 * 
 */
@Repository
public class ConceptTypeRepository extends AbstractJPA<ConceptType> implements IConceptTypeRepository{
    
    public ConceptTypeRepository() {
        super();
        
        setClazz(ConceptType.class);
    }
    
}