package edu.asu.conceptpower.app.core.model;

import edu.asu.conceptpower.core.ReviewStatus;

/**
 * 
 * @author Keerthivasan
 * 
 */

public interface IReviewRequest{
        
    public abstract String getId();
    
    public abstract void setId(String id);
    
    public abstract String getConceptId();
    
    public abstract void setConceptId(String conceptId);
    
    public abstract ReviewStatus getStatus();
    
    public abstract void setStatus(ReviewStatus status);

    public abstract String getRequest();

    public abstract void setRequest(String request) ;

    public abstract String getRequester();

    public abstract void setRequester(String requester);

    public abstract String getResolver();

    public abstract void setResolver(String resolver);
}