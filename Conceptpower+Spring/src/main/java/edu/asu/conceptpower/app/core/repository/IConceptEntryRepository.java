package edu.asu.conceptpower.app.core.repository;

import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.core.model.impl.ConceptEntry;
import org.springframework.data.repository.PagingAndSortingRepository;

@Repository
public interface IConceptEntryRepository extends PagingAndSortingRepository<ConceptEntry, String>{
    
}
   
    
