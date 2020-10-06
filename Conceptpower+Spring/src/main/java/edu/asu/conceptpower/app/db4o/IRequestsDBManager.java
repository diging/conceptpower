package edu.asu.conceptpower.app.db4o;

import java.util.List;

import edu.asu.conceptpower.app.model.ReviewRequest;


/**
 * Manager interface holding all Review Request related services
 * 
 * @author Keerthivasan Krishnamurthy
 * 
 */
public interface IRequestsDBManager {

    
    public abstract void store(ReviewRequest reviewRequest);
    
    public List<ReviewRequest> getAllReviews(String conceptId);
    
    public List<ReviewRequest> getAllReviews();
    
    public ReviewRequest getReview(String reviewId);
}
