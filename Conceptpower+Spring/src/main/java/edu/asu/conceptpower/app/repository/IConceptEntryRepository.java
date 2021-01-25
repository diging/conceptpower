package edu.asu.conceptpower.app.repository;

import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.model.ConceptEntry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Concept Entry repository
 *  
 * @author Keerthivasan Krishnamurthy
 * 
 */
@Repository
public interface IConceptEntryRepository extends PagingAndSortingRepository<ConceptEntry, String>, JpaSpecificationExecutor{
    
    List<ConceptEntry> findByWordnetId(String wordnetId);
    
    List<ConceptEntry> findByConceptList(String conceptList);
    
    List<ConceptEntry> findByTypeId(String typeId);
    
    List<ConceptEntry> findAllByTypeId(String typeId);
    
    List<ConceptEntry> findAllByConceptList(String listName);
    
    @Query("SELECT c from ConceptEntry c WHERE REPLACE(c.word, '_', ' ') LIKE LOWER(word) AND c.isDeleted = 0")
    List<ConceptEntry> findByWord(@Param("word") String word);
    
    @Query("SELECT c FROM ConceptEntry c WHERE c.synonymIds LIKE :id AND c.isDeleted = 0")
    List<ConceptEntry> getConceptsForGivenSynonymId(@Param("id") String id);
}
   
    
