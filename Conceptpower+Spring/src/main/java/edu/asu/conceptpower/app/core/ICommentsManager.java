package edu.asu.conceptpower.app.core;

import edu.asu.conceptpower.core.ReviewRequest.Status;

public interface ICommentsManager {

    /** Used to add comments for each concept entry
     * @param wordId - the wordID of the concept word.
     * @param comment - Comments provided by the user
     * @param wordNetID - the concept Link for which the comments were provided
     * @param requester - the user who submitted the request
     * @param resolver - the user who will resolve the request
     * @param status - the status of the comment -open,resolved,etc.
     * @param reviewFlag - the flag which is set to true if the comment has been made on the concept.
     */
    public abstract  void addComment(String wordId,String comment, String wordNetID , String requester, String resolver,Status status,Boolean reviewFlag);
    
   
}
