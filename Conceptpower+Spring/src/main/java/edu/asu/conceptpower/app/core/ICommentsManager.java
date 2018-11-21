package edu.asu.conceptpower.app.core;

import java.util.List;

import edu.asu.conceptpower.core.ReviewRequest;

public interface ICommentsManager {

    /**
     * @param comment
     * @param conceptLink
     * @param requester
     * @param resolver
     * @param status
     */
    public abstract  void addComment(String comment, String conceptLink , String requester, String resolver,String status);
    
    /**
     * @param conceptLink
     */
    public abstract void deleteComment(String conceptLink);
    
    /**
     * @return
     */
    public abstract List<ReviewRequest> getAllComments();
    
    /**
     * @param conceptId
     * @return
     */
    public abstract ReviewRequest getCommentDetails(String conceptId);
    
    /**
     * @param requestInfo
     * @param conceptId
     */
    public abstract void storeModifiedComments(ReviewRequest requestInfo, String conceptId);
}
