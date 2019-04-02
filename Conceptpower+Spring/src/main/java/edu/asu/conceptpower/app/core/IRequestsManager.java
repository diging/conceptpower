package edu.asu.conceptpower.app.core;

import edu.asu.conceptpower.core.ReviewRequest;
@Deprecated
public interface IRequestsManager {

    /**
     * @param newReviewRequest - New Review Request for a concept.
     */
    public void addReviewRequest(ReviewRequest newReviewRequest);
    
    /**
     * @param conceptId - the conceptId of the concept word
     * @return ReviewRequest - the ReviewRequest created for the concept with the provided conceptId.
     */
    public abstract ReviewRequest getReview(String conceptId);

    
}
