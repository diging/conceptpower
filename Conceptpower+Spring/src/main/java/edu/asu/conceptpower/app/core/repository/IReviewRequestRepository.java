package edu.asu.conceptpower.app.core.repository;

import java.util.List;

import edu.asu.conceptpower.app.core.model.impl.ReviewRequest;

/**
 * 
 * @author Keerthivasan
 * 
 */
public interface IReviewRequestRepository {
    
    ReviewRequest findOne(long id);

    List<ReviewRequest> findAll();

    ReviewRequest create(ReviewRequest entity);

    ReviewRequest update(ReviewRequest entity);

    void delete(ReviewRequest entity);

    void deleteById(long entityId);
}