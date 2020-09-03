package edu.asu.conceptpower.app.core.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.asu.conceptpower.app.core.model.impl.ConceptList;

public interface IConceptListRepository extends PagingAndSortingRepository<ConceptList, String> {
    
}