package edu.asu.conceptpower.app.repository.impl;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.core.ConceptType;

@Repository
public interface ConceptTypeRepository extends CrudRepository<ConceptType, Integer>{
    
}