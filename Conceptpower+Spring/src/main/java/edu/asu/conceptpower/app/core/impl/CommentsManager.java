package edu.asu.conceptpower.app.core.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import edu.asu.conceptpower.app.core.ICommentsManager;
import edu.asu.conceptpower.app.db.DatabaseManager;
import edu.asu.conceptpower.core.ReviewRequest;
import edu.asu.conceptpower.core.ReviewRequest.Status;

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
     * @see edu.asu.conceptpower.app.core.ICommentsManager#addComment(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void addComment(String wordId, String comment, String wordNetId , String requester, String resolver,Status status, Boolean review_flag) {

        ReviewRequest newRequest = new ReviewRequest();
        newRequest.setWordId(wordId);
        newRequest.setComment(comment);
        newRequest.setWordNetId(wordNetId);
        newRequest.setRequester(requester);
        newRequest.setResolver(resolver);
        newRequest.setStatus(status);
        newRequest.setReviewFlag(review_flag);
        
        client.store(newRequest);
        client.commit();
        
    }
    
}
