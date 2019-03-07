package edu.asu.conceptpower.app.db4o;


import edu.asu.conceptpower.core.ReviewRequest;

public interface IRequestsDBManager {

    
    public abstract void store(ReviewRequest reviewRequest);

    public ReviewRequest getReviewRequestForConcept(String conceptId);
    
}
