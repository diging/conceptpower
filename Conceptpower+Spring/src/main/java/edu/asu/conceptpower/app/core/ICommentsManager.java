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
   
}
