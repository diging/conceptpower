package edu.asu.conceptpower.app.core;

import edu.asu.conceptpower.core.CommentStatus;
import edu.asu.conceptpower.core.ReviewRequest;

public interface ICommentsManager {

    /**
     * @param conceptId - the conceptId of the concept word.
     * @param comment - Comments provided by the user
     * @param status - the status of the comment -open,resolved,etc.
     * @param requestor - the user who provided the comments.
     */
    public abstract void addComment(String conceptId, String comment,CommentStatus status,String requestor);
    
    
    
    /**
     * @param conceptId - the conceptId of the concept word
     * @return ReviewRequest - the ReviewRequest ,stored row in db, corresponding to the conceptId.
     */
    public abstract ReviewRequest getEntry(String conceptId);

    
}
