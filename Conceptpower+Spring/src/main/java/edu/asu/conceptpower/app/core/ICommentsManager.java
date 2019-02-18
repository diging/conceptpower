package edu.asu.conceptpower.app.core;

import edu.asu.conceptpower.core.CommentStatus;
import edu.asu.conceptpower.core.ReviewRequest;

public interface ICommentsManager {

    /** Used to add comments for each concept entry
     * @param conceptId - the conceptId of the concept word.
     * @param comment - Comments provided by the user
     * @param status - the status of the comment -open,resolved,etc.
     */
    public abstract void addComment(String conceptId,String comment ,CommentStatus status);
    
    
    
    /**
     * @param conceptId - the conceptId of the concept word
     * @return ReviewRequest - the ReviewRequest ,stored row in db, corresponding to the conceptId.
     */
    public abstract ReviewRequest getEntry(String conceptId);

    
}
