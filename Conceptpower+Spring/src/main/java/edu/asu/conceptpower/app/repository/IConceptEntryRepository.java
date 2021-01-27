package edu.asu.conceptpower.app.repository;

import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.app.model.ReviewStatus;

import java.util.List;

import org.springframework.data.domain.Pageable;
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
public interface IConceptEntryRepository extends PagingAndSortingRepository<ConceptEntry, String>, JpaSpecificationExecutor<ConceptEntry>{
    
    List<ConceptEntry> findByWordnetId(String wordnetId);
    
    List<ConceptEntry> findByConceptList(String conceptList);
    
    List<ConceptEntry> findByTypeId(String typeId);
    
    List<ConceptEntry> findAllByTypeId(String typeId);
    
    List<ConceptEntry> findAllByConceptList(String listName);
    
    @Query("SELECT c from ConceptEntry c WHERE REPLACE(c.word, '_', ' ') LIKE LOWER(word) AND c.isDeleted = 0")
    List<ConceptEntry> findByWord(@Param("word") String word);
    
    @Query("SELECT c FROM ConceptEntry c WHERE c.synonymIds LIKE :id AND c.isDeleted = 0")
    List<ConceptEntry> getConceptsForGivenSynonymId(@Param("id") String id);
    
    @Query("SELECT c FROM ConceptEntry c INNER JOIN c.reviewRequests r WHERE c.id = r.conceptId and r.status = :status")
    List<ConceptEntry> getConceptsForGivenStatus(@Param("status") ReviewStatus status);
    
    @Query("SELECT count(*) FROM ConceptEntry c INNER JOIN c.reviewRequests r WHERE c.id = r.conceptId and r.status = :status")
    Integer getNumberofConceptsForGivenStatus(@Param("status") ReviewStatus status);
    
    @Query(value = "SELECT * FROM concept_entry c INNER JOIN review_request r WHERE c.concept_id = r.concept_id and r.status = :status", 
    		countQuery = "SELECT count(*) FROM concept_entry c INNER JOIN review_request r WHERE c.concept_id = r.concept_id and r.status = :status",
    		nativeQuery = true)
    List<ConceptEntry> getConceptsForGivenStatus(@Param("status") ReviewStatus status, Pageable pageable);
}
   
    
