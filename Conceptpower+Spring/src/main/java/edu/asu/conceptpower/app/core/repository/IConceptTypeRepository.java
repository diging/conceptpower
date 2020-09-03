package edu.asu.conceptpower.app.core.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.asu.conceptpower.app.core.model.impl.ConceptType;

public interface IConceptTypeRepository extends PagingAndSortingRepository<ConceptType, String>{
}