package edu.asu.conceptpower.app.core.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.conceptpower.app.core.ICommentsManager;
import edu.asu.conceptpower.app.db.DatabaseClient;
import edu.asu.conceptpower.app.db4o.DBNames;
import edu.asu.conceptpower.app.db4o.IConceptDBManager;
import edu.asu.conceptpower.core.ReviewRequest;

public class CommentsManager implements ICommentsManager{

    @Autowired
    private IConceptDBManager client;

   
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
        
        DatabaseClient dbClient=new DatabaseClient();
        
        dbClient.store(newRequest, DBNames.COMMENTS_DB);
    }
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.ICommentsManager#deleteComment(java.lang.String)
     */
    public void deleteComment(String conceptLink) {
        client.deleteComment(conceptLink);
    }
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.ICommentsManager#getAllComments()
     */
    @SuppressWarnings("unchecked")
    public List<ReviewRequest> getAllComments() {
        List<?> results = client.getAllElementsOfType(ReviewRequest.class);
        if (results != null)
            return (List<ReviewRequest>) results;
        return null;
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
