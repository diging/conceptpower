package edu.asu.conceptpower.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.model.ConceptType;

/**
 * Concept Type repository
 *  
 * @author Keerthivasan Krishnamurthy
 * 
 */
@Repository
public interface IConceptTypeRepository extends PagingAndSortingRepository<ConceptType, String>{
    
    List<ConceptType> findAll();
    
    Optional<ConceptType> findByTypeName(String typeName);
    
    Page<ConceptType> findAll(Pageable pageable);
}