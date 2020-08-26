package edu.asu.conceptpower.app.db4o;

import java.util.List;

import edu.asu.conceptpower.core.ReviewRequest;


/**
 * 
 * @author Keerthivasan
 * 
 */
public interface IRequestsDBManager {

    
    public abstract void store(ReviewRequest reviewRequest);
    
    public List<ReviewRequest> getAllReviews(String conceptId);
    
    public List<ReviewRequest> getAllReviews();
    
    public ReviewRequest getReview(String reviewId);
}
