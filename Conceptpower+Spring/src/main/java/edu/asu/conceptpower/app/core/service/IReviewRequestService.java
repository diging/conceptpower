package edu.asu.conceptpower.app.core.service;

import java.util.List;

import edu.asu.conceptpower.app.core.model.impl.ReviewRequest;

/**
 * 
 * @author Keerthivasan
 * 
 */
public interface IReviewRequestService {
    
    public abstract void create(final ReviewRequest entity);
    
    public abstract ReviewRequest findOne(final long id);

    public abstract List<ReviewRequest> findAll();
}