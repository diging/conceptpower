package edu.asu.conceptpower.app.core.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.core.model.impl.ReviewRequest;

/**
 * Review Request repository
 * 
 * @author Keerthivasan
 * 
 */

@Repository
public interface IReviewRequestRepository extends PagingAndSortingRepository<ReviewRequest, String>{
    
}