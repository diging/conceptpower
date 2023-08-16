package edu.asu.conceptpower.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.model.ConceptEntry;

/**
 * Concept Entry repository
 *  
 * @author Keerthivasan Krishnamurthy, Digital Innovation Group
 * 
 */
@Repository
public interface IConceptEntryRepository extends PagingAndSortingRepository<ConceptEntry, String>, JpaSpecificationExecutor{
    
    Optional<ConceptEntry> findById(String id);
    
    void save(ConceptEntry element);
    
    List<ConceptEntry> findByWordnetId(String wordnetId);
    
    List<ConceptEntry> findByConceptList(String conceptList);
    
    List<ConceptEntry> findByTypeId(String typeId);
    
    List<ConceptEntry> findAllByTypeId(String typeId);
    
    List<ConceptEntry> findAllByConceptList(String listName);
    
    @Query("SELECT c from ConceptEntry c WHERE REPLACE(c.word, '_', ' ') LIKE LOWER(:word) AND c.isDeleted = false")
    List<ConceptEntry> findByWord(@Param("word") String word);
    
    @Query("SELECT c FROM ConceptEntry c WHERE c.synonymIds LIKE :id AND c.isDeleted = false")
    List<ConceptEntry> getConceptsForGivenSynonymId(@Param("id") String id);

    Iterable<ConceptEntry> findAll();
}
   
    
