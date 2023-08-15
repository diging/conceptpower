package edu.asu.conceptpower.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.model.ConceptList;

/**
 * Concept List repository
 *  
 * @author Keerthivasan Krishnamurthy
 * 
 */
@Repository
public interface IConceptListRepository extends PagingAndSortingRepository<ConceptList, String> {
    
    /*Used List<> instead of Iterable<> to avoid Refactoring the underlying logics in the services and controllers*/
    List<ConceptList> findAll();
    
    void save(ConceptList element);
    
    ConceptList findByConceptListName(String conceptListName);

    Optional<ConceptList> findById(String id);

    boolean existsById(String id);

    void deleteById(String id);
    
}