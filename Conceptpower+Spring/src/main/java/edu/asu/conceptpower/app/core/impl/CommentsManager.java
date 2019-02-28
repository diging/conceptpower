package edu.asu.conceptpower.app.core.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db4o.ObjectSet;

import edu.asu.conceptpower.app.core.ICommentsManager;
import edu.asu.conceptpower.app.db4o.ICommentsDBManager;
import edu.asu.conceptpower.core.ReviewRequest;

@Service
public  class CommentsManager implements ICommentsManager{

  
    @Autowired
    private ICommentsDBManager dbClient;
   
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.ICommentsManager#addReviewRequest(edu.asu.conceptpower.core.ReviewRequest)
     */
    public void addReviewRequest(ReviewRequest newReviewRequest) {
        
        dbClient.store(newReviewRequest);
    }
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.ICommentsManager#getEntry(java.lang.String)
     */
    public ReviewRequest getReview(String conceptId) {
        
        ReviewRequest exampleEntry = new ReviewRequest();
        exampleEntry.setConceptId(conceptId);
        
        ObjectSet<ReviewRequest> results = dbClient.getReviewRequestForConcept(exampleEntry);

       // getting the results to fetch the comments corresponding to the passed conceptId
        if (results!= null && results.size()>0) {
            return results.get(0);
        }
        return null;
    }
}
