package edu.asu.conceptpower.app.repository;

import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.model.ConceptEntry;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Concept Entry repository
 *  
 * @author Keerthivasan Krishnamurthy
 * 
 */
@Repository
public interface IConceptEntryRepository extends PagingAndSortingRepository<ConceptEntry, String>{
    
}
   
    
