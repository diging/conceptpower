package edu.asu.conceptpower.app.core.impl;

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
    public ReviewRequest getReview(String conceptId) {
        
        ReviewRequest request = new ReviewRequest();
        request.setConceptId(conceptId);
        
        List<ReviewRequest> reviewRequests = dbClient.getAllReviews(request);
       
        if(reviewRequests!= null && reviewRequests.size()>0) {
            //ArrayList is initialized to make the instance mutable for sorting
            reviewRequests = new ArrayList<>(reviewRequests);
            Collections.sort(reviewRequests, (x, y) -> x.getCreatedAt().compareTo(y.getCreatedAt()));
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
    
    public ReviewRequest updateReview(ReviewRequest reviewRequest) {
        ReviewRequest request = new ReviewRequest();
        
        request.setId(reviewRequest.getId());
        
        ReviewRequest responses = dbClient.getReview(request);
        
        if(responses == null) {
            return null;
        }

        List<Comment> comments  = responses.getComments();
        comments.addAll(reviewRequest.getComments());
        responses.setResolver(reviewRequest.getResolver());
        responses.setComments(comments);
        responses.setStatus(reviewRequest.getStatus());
        
        dbClient.updateReviewRequest(responses);
        return responses;
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
