package edu.asu.conceptpower.app.core;

import edu.asu.conceptpower.core.ReviewRequest.Status;

public interface ICommentsManager {

    /** Used to add comments for each concept entry
     * @param wordId - the wordID of the concept word.
     * @param comment - Comments provided by the user
     * @param wordNetID - the concept Link for which the comments were provided
     * @param status - the status of the comment -open,resolved,etc.
     */
    public abstract  void addComment(String wordId,String comment, String wordNetID , Status status);
    
   
}
