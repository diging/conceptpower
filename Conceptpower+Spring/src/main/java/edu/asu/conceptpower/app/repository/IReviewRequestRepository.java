package edu.asu.conceptpower.app.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.model.ReviewRequest;
import edu.asu.conceptpower.app.model.ReviewStatus;

/**
 * Review Request repository
 * 
 * @author Keerthivasan Krishnamurthy
 * 
 */

@Repository
public interface IReviewRequestRepository extends PagingAndSortingRepository<ReviewRequest, String>{
    
    /*
     * Find all the reivew requests with the given conceptId
     * */
    List<ReviewRequest> findAllByConceptId(String conceptId);
    
    /*
     * Return all the review requests from the database
     * */
    List<ReviewRequest> findAll();
    
    /*
     * Find all the review requests with the given status
     * */
    List<ReviewRequest> findAllByStatus(ReviewStatus status, Pageable pageable);
    
    /*
     * Find all the review requests with the given status
     * */
    List<ReviewRequest> findAllByStatus(ReviewStatus status);
}