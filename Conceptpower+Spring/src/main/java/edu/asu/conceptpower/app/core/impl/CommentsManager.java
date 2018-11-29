package edu.asu.conceptpower.app.core.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.db4o.ObjectContainer;

import edu.asu.conceptpower.app.core.ICommentsManager;
import edu.asu.conceptpower.app.db.DatabaseClient;
import edu.asu.conceptpower.app.db.DatabaseManager;
import edu.asu.conceptpower.app.db4o.DBNames;
import edu.asu.conceptpower.app.db4o.IConceptDBManager;
import edu.asu.conceptpower.core.ReviewRequest;
import edu.asu.conceptpower.servlet.core.ChangeEvent;
import edu.asu.conceptpower.servlet.core.ChangeEvent.ChangeEventTypes;

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
    public void addComment(String comment, String conceptLink , String requester, String resolver,String status) {

        ReviewRequest newRequest = new ReviewRequest();
        newRequest.setComment(comment);
        newRequest.setConceptLink(conceptLink);
        newRequest.setRequester(requester);
        newRequest.setResolver(resolver);
        newRequest.setStatus(status);
        
        client.store(newRequest);
        client.commit();
       
    }
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.ICommentsManager#deleteComment(java.lang.String)
     */
    public void deleteComment(ReviewRequest comment, String userName) {
  //      client.deleteComment(conceptLink);
        
        if (comment != null) {
            comment.setDeleted(true);
            ChangeEvent changeEvent = new ChangeEvent();
            changeEvent.setType(ChangeEventTypes.DELETION);
            changeEvent.setDate(new Date());
            changeEvent.setUserName(userName);
            comment.addNewChangeEvent(changeEvent);
       //     client.update(comment, DBNames.DICTIONARY_DB);
       //     indexService.deleteById(comment.get, userName);
        }
        
       
    }
    
    

    
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.ICommentsManager#getCommentDetails(java.lang.String)
     */
    public void getCommentDetails(String conceptId) {
      //  client.
    }
    
   
    
   
}
