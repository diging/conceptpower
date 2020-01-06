package edu.asu.conceptpower.app.core.impl;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.IRequestsManager;
import edu.asu.conceptpower.app.db4o.IRequestsDBManager;
import edu.asu.conceptpower.core.Comment;
import edu.asu.conceptpower.core.ReviewRequest;
import edu.asu.conceptpower.core.ReviewStatus;

@Service
public  class RequestsManager implements IRequestsManager{

    protected final String REVIEW_PREFIX = "REVIEW";
    
    @Autowired
    private IRequestsDBManager dbClient;
   
    
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.IRequestsManager#addReviewRequest(edu.asu.conceptpower.core.ReviewRequest)
     */
    public void addReviewRequest(ReviewRequest newReviewRequest) {
        newReviewRequest.setId(generateId(REVIEW_PREFIX));
        dbClient.store(newReviewRequest);
    }
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.IRequestsManager#getReview(java.lang.String)
     */
    public ReviewRequest getLatestReview(String conceptId) {
        
        List<ReviewRequest> reviewRequests = getAllReviews(conceptId);
       
        if(reviewRequests!= null && reviewRequests.size() > 0) {
            return reviewRequests.get(reviewRequests.size() - 1);
        }
        
        return null;
    }
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.IRequestsManager#getAllReviews(java.lang.String)
     */
    public List<ReviewRequest> getAllReviews(String conceptId){
        ReviewRequest request = new ReviewRequest();
        request.setConceptId(conceptId);
        
        List<ReviewRequest> reviewRequests = dbClient.getAllReviews(request);
        
        if(reviewRequests!= null && reviewRequests.size() > 0) {
            reviewRequests = new ArrayList<>(reviewRequests);
            Collections.sort(reviewRequests, (x, y) -> x.getCreatedAt().compareTo(y.getCreatedAt()));
        }
        
        return reviewRequests;
    }
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.IRequestsManager#updateReview(edu.asu.conceptpower.core.ReviewRequest)
     */
    
    public ReviewRequest updateReview(String reviewId, ReviewStatus reviewStatus, Comment comment, OffsetDateTime createdAt,String updatedBy) {
        ReviewRequest request = new ReviewRequest();
        request.setId(reviewId);
        
        ReviewRequest storedRequest = dbClient.getReview(request);
        
        if(storedRequest == null) {
            return null;
        }
        
        if(comment != null) {
            //There is only a possibility of single comment at a time
            comment.setCreatedAt(createdAt);
            comment.setCreatedBy(updatedBy);
            
            //Used setComments instead of addAll to comments parameter because db4o is not properly getting indexed to the old object. SetComments solves this issue.
            List<Comment> commentList = new ArrayList<>();
            commentList.addAll(storedRequest.getComments());
            commentList.add(comment);
            storedRequest.setComments(commentList);

            storedRequest.setResolver(updatedBy);
            storedRequest.setStatus(reviewStatus);
            
            dbClient.store(storedRequest);
            return storedRequest;
        }
        return null;
    }
    
    /*
     * =================================================================
     * Protected/Private methods
     * =================================================================
     */
    protected String generateId(String prefix) {
        String id = prefix + UUID.randomUUID().toString();

        while (true) {
            ReviewRequest example = null;
            
            example = new ReviewRequest();
            example.setId(id);
            
            // if there doesn't exist an object with this id return id
            ReviewRequest results = dbClient.getReview(example);
            if (results == null )
                return id;

            // try other id
            id = prefix + UUID.randomUUID().toString();
        }
    }
}
