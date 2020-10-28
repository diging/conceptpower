package edu.asu.conceptpower.app.core;

import java.time.OffsetDateTime;
import java.util.List;

import edu.asu.conceptpower.app.model.Comment;
import edu.asu.conceptpower.app.model.ReviewRequest;
import edu.asu.conceptpower.app.model.ReviewStatus;

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
     * @param conceptId - The id of the concept for which review requests are being retrieved.
     * @return ReviewRequest - the Latest ReviewRequest for the given conceptId.
     */
    public abstract ReviewRequest getLatestReview(String conceptId);
    
    /**
     * @param conceptId - The id of the concept for which review requests are being retrieved
     * @return List<ReviewRequest> - list of reviewRequests corresponding to the given conceptId.
     */
    public List<ReviewRequest> getAllReviews(String conceptId);
    /**
     * Method to Update(reopen/resolve) the reviewRequest
     * @param reviewId - The id of the ReviewRequest to be updated.
     * @param reviewStatus - status to which it needs to be updated(RESOLVED/CLOSED/OPENED).
     * @param comments - corresponding comment if any, along with this update.
     * @param createdAt - update requested date and time.
     * @param updatedBy - The user performing this update.
     * @Return ReviewRequest - Returns back the updated reviewRequest
     */
    public ReviewRequest updateReview(String reviewId, ReviewStatus reviewStatus, Comment comment, OffsetDateTime createdAt, String updatedBy);
    
    /**
     * 
     * @return List<ReviewRequest> - return all the reviewRequests in the database.
     */
    public List<ReviewRequest> getAllReviews();
}
