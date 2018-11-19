package edu.asu.conceptpower.app.core.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.conceptpower.app.db4o.DBNames;
import edu.asu.conceptpower.app.db4o.IConceptDBManager;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ReviewRequest;

public class CommentsManager {

    @Autowired
    private IConceptDBManager client;

   
    public void addComment(String comment, String conceptLink , String requester, String resolver,String status) {

        ReviewRequest newRequest = new ReviewRequest();
        newRequest.setComment(comment);
        newRequest.setConceptLink(conceptLink);
        newRequest.setRequester(requester);
        newRequest.setResolver(resolver);
        
        client.store(newRequest, DBNames.DICTIONARY_DB);
    }
    
    public void deleteComment(String conceptLink) {
        client.deleteComment(conceptLink);
    }
    
    @SuppressWarnings("unchecked")
    public List<ReviewRequest> getAllComments() {
        List<?> results = client.getAllElementsOfType(ReviewRequest.class);
        if (results != null)
            return (List<ReviewRequest>) results;
        return null;
    }

    
    
    public ReviewRequest getCommentDetails(String conceptId) {
        return new ReviewRequest();
    }
    
   
    
    public void storeModifiedComments(ReviewRequest requestInfo, String conceptId) {
      //  client.update(list, listname, DBNames.DICTIONARY_DB);
    }
    
}
