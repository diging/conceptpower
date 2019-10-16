package edu.asu.conceptpower.app.core;

import java.util.List;

import edu.asu.conceptpower.core.ReviewRequest;

/**This interface provides function declaration for adding new Request for review and 
 *      fetching an existing Review for the required conceptId.
 *      
 * @author abhishekkumar
 *
 */
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

    public List<ReviewRequest> getAllReviews();
}
