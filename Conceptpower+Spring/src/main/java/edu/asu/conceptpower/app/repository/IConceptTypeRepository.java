package edu.asu.conceptpower.app.repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;
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
    
    /*Returns all the rows in the database*/
    List<ConceptType> findAll();
    
    /*Returns all rows after applying sort properties */
    Page<ConceptType> findAll(Pageable pageable);
}