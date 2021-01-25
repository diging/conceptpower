package edu.asu.conceptpower.app.repository;

import java.util.List;

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
    List<ConceptList> findAll();
    
    ConceptList findByConceptListName(String conceptListName);
    
}