package edu.asu.conceptpower.app.core.repository.impl;

import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.core.model.impl.ConceptEntry;
import edu.asu.conceptpower.app.core.repository.AbstractJPA;
import edu.asu.conceptpower.app.core.repository.IConceptEntryRepository;

@Repository
public class ConceptEntryRepository extends AbstractJPA<ConceptEntry> implements IConceptEntryRepository{
    
    public ConceptEntryRepository() {
        super();
        setClazz(ConceptEntry.class);
    }
    
}