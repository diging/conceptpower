package edu.asu.conceptpower.app.core.repository.impl;

import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.core.model.impl.ReviewRequest;
import edu.asu.conceptpower.app.core.repository.AbstractJPA;
import edu.asu.conceptpower.app.core.repository.IReviewRequestRepository;

/**
 * 
 * @author Keerthivasan
 * 
 */
@Repository
public class ReviewRequestRepository extends AbstractJPA<ReviewRequest> implements IReviewRequestRepository {
    
    public ReviewRequestRepository() {
        super();
        
        setClazz(ReviewRequest.class);
    }
}