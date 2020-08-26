package edu.asu.conceptpower.app.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.conceptpower.app.core.model.impl.ConceptList;
import edu.asu.conceptpower.app.core.repository.IConceptListRepository;
import edu.asu.conceptpower.app.core.service.IConceptListService;

/**
 * 
 * @author Keerthivasan
 * 
 */
@Service
@Transactional
public class ConceptListService implements IConceptListService{
    
    @Autowired
    private IConceptListRepository dao;

    public ConceptListService() {
        super();
    }

    public void create(final ConceptList entity) {
        dao.create(entity);
    }

    public ConceptList findOne(final long id) {
        return dao.findOne(id);
    }

    public List<ConceptList> findAll() {
        return dao.findAll();
    }

}