package edu.asu.conceptpower.app.core.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.conceptpower.app.core.model.impl.ConceptType;
import edu.asu.conceptpower.app.core.repository.IConceptTypeRepository;
import edu.asu.conceptpower.app.core.service.IConceptTypeService;

/**
 * 
 * @author Keerthivasan
 * 
 */
@Service
@Transactional
public class ConceptTypeService implements IConceptTypeService{
    
    @Autowired
    private IConceptTypeRepository dao;

    public ConceptTypeService() {
        super();
    }

    public void create(final ConceptType entity) {
        dao.create(entity);
    }

    public ConceptType findOne(final long id) {
        return dao.findOne(id);
    }

    public List<ConceptType> findAll() {
        return dao.findAll();
    }

    
}