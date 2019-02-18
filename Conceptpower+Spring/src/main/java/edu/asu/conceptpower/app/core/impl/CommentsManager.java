package edu.asu.conceptpower.app.core.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import edu.asu.conceptpower.app.core.ICommentsManager;
import edu.asu.conceptpower.app.db.DatabaseManager;
import edu.asu.conceptpower.core.CommentStatus;
import edu.asu.conceptpower.core.ReviewRequest;

@Service
public class CommentsManager implements ICommentsManager{

    @Autowired
    @Qualifier("conceptReviewDatabaseManager")
    private DatabaseManager dbManager;
    
    private ObjectContainer client;

    @PostConstruct
    public void init() {
        this.client = dbManager.getClient();
    }
   
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.ICommentsManager#addComment(java.lang.String, java.lang.String, edu.asu.conceptpower.core.CommentStatus)
     */
    public void addComment(String conceptId, String comment,CommentStatus status) {

        ReviewRequest newRequest = new ReviewRequest();
        newRequest.setConceptId(conceptId);
        newRequest.setComment(comment);
        newRequest.setStatus(status);
        
        client.store(newRequest);
        client.commit();
        
    }
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.ICommentsManager#getEntry(java.lang.String)
     */
    public ReviewRequest getEntry(String conceptId) {
        
        ReviewRequest exampleEntry = new ReviewRequest();
        exampleEntry.setConceptId(conceptId);
        
        this.client = dbManager.getClient();
        ObjectSet<ReviewRequest> results = client.queryByExample(exampleEntry);

        if (results.size()>0)
            return results.get(results.size()-1);

        return null;
    }
}
