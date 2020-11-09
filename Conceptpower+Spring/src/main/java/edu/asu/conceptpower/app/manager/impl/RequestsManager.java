package edu.asu.conceptpower.app.manager.impl;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.db4o.IRequestsDBManager;
import edu.asu.conceptpower.app.manager.IRequestsManager;
import edu.asu.conceptpower.app.model.Comment;
import edu.asu.conceptpower.app.model.ReviewRequest;
import edu.asu.conceptpower.app.model.ReviewStatus;

@Service
public  class RequestsManager implements IRequestsManager{

    protected final String REVIEW_PREFIX = "REVIEW";
    
    @Autowired
    private IRequestsDBManager dbClient;
   
    
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.IRequestsManager#addReviewRequest(edu.asu.conceptpower.core.ReviewRequest)
     */
    public void addReviewRequest(ReviewRequest reviewRequest) {
        reviewRequest.setId(generateId(REVIEW_PREFIX));
        dbClient.store(reviewRequest);
    }
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.IRequestsManager#getLatestReview(java.lang.String)
     */
    public ReviewRequest getLatestReview(String conceptId) {
        List<ReviewRequest> reviewRequests = getAllReviews(conceptId);
       
        if(reviewRequests!= null && !reviewRequests.isEmpty()) {
            return reviewRequests.get(reviewRequests.size() - 1);
        }
        
        return null;
    }
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.IRequestsManager#getAllReviews(java.lang.String)
     */
    public List<ReviewRequest> getAllReviews(String conceptId){
        List<ReviewRequest> reviewRequests = dbClient.getAllReviews(conceptId);
        
        if(reviewRequests!= null && !reviewRequests.isEmpty()) {
            reviewRequests = new ArrayList<>(reviewRequests);
            Collections.sort(reviewRequests, Comparator.comparing(ReviewRequest :: getCreatedAt, Comparator.nullsFirst(Comparator.naturalOrder())));
            return reviewRequests;
        }
        
        return Collections.emptyList();
    }
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.IRequestsManager#updateReview(edu.asu.conceptpower.core.ReviewRequest)
     */
    
    public ReviewRequest updateReview(String reviewId, ReviewStatus reviewStatus, Comment comment, OffsetDateTime createdAt,String updatedBy) {  
        ReviewRequest storedRequest = dbClient.getReview(reviewId);
        
        if(storedRequest == null) {
            return null;
        }
        
        if(comment != null) {
            comment.setCreatedAt(createdAt);
            comment.setCreatedBy(updatedBy);
            
            if(storedRequest.getComments() == null) {
                storedRequest.setComments(new ArrayList<>());
            }
            storedRequest.getComments().add(comment);
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
            // if there doesn't exist an object with this id return id
            ReviewRequest results = dbClient.getReview(id);
            if (results == null )
                return id;

            // try other id
            id = prefix + UUID.randomUUID().toString();
        }
    }

    @Override
    public List<ReviewRequest> getAllReviews() {
       return dbClient.getAllReviews();
    }
}
