package edu.asu.conceptpower.app.core;

import edu.asu.conceptpower.core.ReviewRequest.Status;

public interface ICommentsManager {

    /** Used to add comments for each concept entry
     * @param comment - Comments provided by the user
     * @param conceptLink - the concept Link for which the comments were provided
     * @param requester - the user who submitted the request
     * @param resolver - the user who will resolve the request
     * @param status - the status of the comment -open,resolved,etc.
     */
    public abstract  void addComment(String comment, String conceptLink , String requester, String resolver,Status status);
    
   
}
