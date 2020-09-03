package edu.asu.conceptpower.app.core.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.asu.conceptpower.app.core.model.impl.ReviewRequest;

/**
 * 
 * @author Keerthivasan
 * 
 */
public interface IReviewRequestRepository extends PagingAndSortingRepository<ReviewRequest, String>{
    
}