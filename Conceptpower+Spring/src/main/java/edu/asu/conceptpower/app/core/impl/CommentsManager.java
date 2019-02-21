package edu.asu.conceptpower.app.core.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.db4o.ObjectSet;

import edu.asu.conceptpower.app.core.ICommentsManager;
import edu.asu.conceptpower.app.db4o.ICommentsDBManager;
import edu.asu.conceptpower.core.CommentStatus;
import edu.asu.conceptpower.core.ReviewRequest;

@Service
@PropertySource("classpath:config.properties")
public class CommentsManager implements ICommentsManager{

  
    @Autowired
    private ICommentsDBManager dbClient;
   
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.ICommentsManager#addComment(java.lang.String, java.lang.String, edu.asu.conceptpower.core.CommentStatus)
     */
    public void addComment(String conceptId, String comment,CommentStatus status,String requestor) {

        ReviewRequest newRequest = new ReviewRequest();
        newRequest.setConceptId(conceptId);
        newRequest.setComment(comment);
        newRequest.setStatus(status);
        newRequest.setRequestor(requestor);
                
        dbClient.store(newRequest);
    }
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.ICommentsManager#getEntry(java.lang.String)
     */
    public ReviewRequest getEntry(String conceptId) {
        
        ReviewRequest exampleEntry = new ReviewRequest();
        exampleEntry.setConceptId(conceptId);
        
        ObjectSet<ReviewRequest> results = dbClient.queryByExample(exampleEntry);

       // getting the results to fetch the comments corresponding to the passed conceptId
        if (results!= null && results.size()>0) {
            return results.get(0);
        }
        return null;
    }
}
