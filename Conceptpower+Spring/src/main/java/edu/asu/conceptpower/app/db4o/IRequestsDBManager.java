package edu.asu.conceptpower.app.db4o;

import java.util.List;

import edu.asu.conceptpower.core.ReviewRequest;

public interface IRequestsDBManager {

    
    public abstract void store(ReviewRequest reviewRequest);

    public ReviewRequest getReviewRequestForConcept(String conceptId);
    
    public ReviewRequest updateReviewRequest(ReviewRequest reviewRequest);
    
    public ReviewRequest reopenReviewRequest(ReviewRequest conceptId);
    
    public List<ReviewRequest> getAllReviewsForaConcept(String conceptId);
}
