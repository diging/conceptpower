package edu.asu.conceptpower.app.core.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db4o.ObjectSet;

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
        
        ReviewRequest exampleEntry = new ReviewRequest();
        exampleEntry.setConceptId(conceptId);
        
        ObjectSet<ReviewRequest> results = dbClient.getReviewRequestForConcept(exampleEntry);

       // getting the results to fetch the request corresponding to the given conceptId
        if (results!= null && results.size()>0) {
            for (int i=0;i<results.size();i++) {
                if(results.get(i).getRequest()!=null) { //getting the only entry of conceptId that has the request
                    return results.get(i);
                }
            } 
        }
        return null;
    }
}
