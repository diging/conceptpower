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

    protected final String LIST_PREFIX = "REVIEW";
    
    @Autowired
    private IRequestsDBManager dbClient;
   
    
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.IRequestsManager#addReviewRequest(edu.asu.conceptpower.core.ReviewRequest)
     */
    public void addReviewRequest(ReviewRequest newReviewRequest) {
        newReviewRequest.setId(generateId(LIST_PREFIX));
        dbClient.store(newReviewRequest);
    }
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.IRequestsManager#getReview(java.lang.String)
     */
    public ReviewRequest getReview(String conceptId) {
        
        return dbClient.getReviewRequestForConcept(conceptId);
    }
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.IRequestsManager#getAllReviews(java.lang.String)
     */
    public List<ReviewRequest> getAllReviews(String conceptId){
        ReviewRequest request = new ReviewRequest();
        request.setConceptId(conceptId);
        
        List<ReviewRequest> reviewRequests = new ArrayList<>(dbClient.getAllReviews(request));
        
        Collections.sort(reviewRequests, (x, y) -> x.getCreatedAt().compareTo(y.getCreatedAt()));
        
        return reviewRequests;
    }
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.IRequestsManager#updateReview(edu.asu.conceptpower.core.ReviewRequest)
     */
    
    public ReviewRequest updateReview(ReviewRequest reviewRequest) {
        ReviewRequest rev = new ReviewRequest();
        
        rev.setId(reviewRequest.getId());
        
        List<ReviewRequest> requests = dbClient.getReview(rev);
        
        if(requests == null || requests.size() == 0) {
            return null;
        }

        List<Comment> comments  = requests.get(0).getComments();
        comments.addAll(reviewRequest.getComments());
        requests.get(0).setResolver(reviewRequest.getResolver());
        requests.get(0).setComments(comments);
        requests.get(0).setStatus(reviewRequest.getStatus());
        
        dbClient.updateReviewRequest(requests.toArray());
        return requests.get(0);
    }
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.IRequestsManager#reopenReview(edu.asu.conceptpower.core.ReviewRequest)
     */
    public ReviewRequest reopenReview(ReviewRequest reviewRequest) {
        ReviewRequest rev = new ReviewRequest();
        
        rev.setId(reviewRequest.getId());
        List<ReviewRequest> requests = dbClient.getReview(rev);
        
        if(requests == null || requests.size() == 0) {
            return null;
        }
        
        List<Comment> comments  = requests.get(0).getComments();
        comments.addAll(reviewRequest.getComments());
        
        requests.get(0).setResolver(reviewRequest.getResolver());
        requests.get(0).setComments(comments);
        requests.get(0).setStatus(reviewRequest.getStatus());
        
        dbClient.updateReviewRequest(requests.toArray()); 
        return requests.get(0);
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
            List<ReviewRequest> results = dbClient.getReview(example);
            if (results == null || results.size() == 0)
                return id;

            // try other id
            id = prefix + UUID.randomUUID().toString();
        }
    }
}
