package edu.asu.conceptpower.app.repository;

import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.model.ConceptEntry;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Concept Entry repository
 *  
 * @author Keerthivasan Krishnamurthy
 * 
 */
@Repository
public interface IConceptEntryRepository extends PagingAndSortingRepository<ConceptEntry, String>{
    
    List<ConceptEntry> findAll();
    
    List<ConceptEntry> findByWordnetId(String wordnetId);
    
    List<ConceptEntry> findByConceptList(String conceptList);
    
    List<ConceptEntry> findByTypeId(String typeId);
    
    //TODO
    @Query("SELECT id FROM ConceptEntry WHERE :fieldName = :value")
    List<ConceptEntry> getConceptsGivenFieldName(String fieldName, String value);
    
    //TODO
    @Query("SELECT id FROM ConceptEntry WHERE  id = :id")
    List<ConceptEntry> getConceptsForGivenSynonymId(String id);
}
   
    
