
package edu.asu.conceptpower.app.mysql;

import edu.asu.conceptpower.app.model.ReviewRequest;

public interface IRequestsDBManager {

    
    public abstract void store(ReviewRequest reviewRequest);

    public String getReviewRequestForConcept(String conceptId);
    
    public Iterable<ReviewRequest> getAllReviewRequest();
    
    public void deleteRequest(ReviewRequest reviewRequest);
    
    
}