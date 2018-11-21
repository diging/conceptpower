package edu.asu.conceptpower.app.core.impl;

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
    public void deleteComment(String conceptLink) {
  //      client.deleteComment(conceptLink);
    }
    
    

    
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.ICommentsManager#getCommentDetails(java.lang.String)
     */
    public ReviewRequest getCommentDetails(String conceptId) {
        return new ReviewRequest();
    }
    
   
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.ICommentsManager#storeModifiedComments(edu.asu.conceptpower.core.ReviewRequest, java.lang.String)
     */
    public void storeModifiedComments(ReviewRequest requestInfo, String conceptId) {
      //  client.update(list, listname, DBNames.DICTIONARY_DB);
    }
    
}
