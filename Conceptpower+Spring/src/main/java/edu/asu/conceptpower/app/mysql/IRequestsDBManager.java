
package edu.asu.conceptpower.app.mysql;

import edu.asu.conceptpower.app.model.ReviewRequest;

public interface IRequestsDBManager {

    
    public abstract void store(ReviewRequest reviewRequest);

    public ReviewRequest getReviewRequestForConcept(String conceptId);
    
}