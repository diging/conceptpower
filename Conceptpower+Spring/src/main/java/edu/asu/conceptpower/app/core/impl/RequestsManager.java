package edu.asu.conceptpower.app.core.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.IRequestsManager;
import edu.asu.conceptpower.app.db4o.IRequestsDBManager;
import edu.asu.conceptpower.core.ReviewRequest;

@Service
public  class RequestsManager implements IRequestsManager{

  
    @Autowired
    private IRequestsDBManager dbClient;
   
    
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.IRequestsManager#addReviewRequest(edu.asu.conceptpower.core.ReviewRequest)
     */
    public void addReviewRequest(ReviewRequest newReviewRequest) {
        
        dbClient.store(newReviewRequest);
    }
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.IRequestsManager#getReview(java.lang.String)
     */
    public ReviewRequest getReview(String conceptId) {
        
        return dbClient.getReviewRequestForConcept(conceptId);
    }
    
    public List<ReviewRequest> getAllReviews(){
        return dbClient.getAllReviewRequests();
    }
}
