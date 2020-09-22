package edu.asu.conceptpower.app.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.core.model.impl.ConceptList;

/**
 * Concept List repository
 *  
 * @author Keerthivasan Krishnamurthy
 * 
 */
@Repository
public interface IConceptListRepository extends PagingAndSortingRepository<ConceptList, String> {
    long deleteByConceptListName(String conceptListName);
    
    List<ConceptList> findAll();
    
    ConceptList findByConceptListName(String conceptListName);
    
}