package edu.asu.conceptpower.app.core.repository.impl;

import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.core.model.impl.ConceptList;
import edu.asu.conceptpower.app.core.repository.AbstractJPA;
import edu.asu.conceptpower.app.core.repository.IConceptListRepository;

/**
 * 
 * @author Keerthivasan
 * 
 */
@Repository
public class ConceptListRepository extends AbstractJPA<ConceptList> implements IConceptListRepository{
    
    public ConceptListRepository() {
        super();
        
        setClazz(ConceptList.class);
    }
}