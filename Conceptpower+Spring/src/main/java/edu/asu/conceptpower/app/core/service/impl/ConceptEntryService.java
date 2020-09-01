package edu.asu.conceptpower.app.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.conceptpower.app.core.model.impl.ConceptEntry;
import edu.asu.conceptpower.app.core.repository.IConceptEntryRepository;
import edu.asu.conceptpower.app.core.service.IConceptEntryService;

/**
 * 
 * @author Keerthivasan
 * 
 */
@Service
@Transactional
public class ConceptEntryService implements IConceptEntryService{
    
    @Autowired
    private IConceptEntryRepository dao;

    public ConceptEntryService() {
        super();
    }

    public void create(final ConceptEntry entity) {
        dao.create(entity);
    }

    public ConceptEntry findOne(final long id) {
        return dao.findOne(id);
    }

    public List<ConceptEntry> findAll() {
        return dao.findAll();
    }

}