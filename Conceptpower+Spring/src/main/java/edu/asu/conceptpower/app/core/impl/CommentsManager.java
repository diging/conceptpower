package edu.asu.conceptpower.app.core.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.db4o.ObjectContainer;

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
   
    public void addComment(String wordId, String comment, String wordNetId ,Status status) {

        ReviewRequest newRequest = new ReviewRequest();
        newRequest.setWordId(wordId);
        newRequest.setComment(comment);
        newRequest.setWordNetId(wordNetId);
        newRequest.setStatus(status);
        
        client.store(newRequest);
        client.commit();
        
    }
    
}
