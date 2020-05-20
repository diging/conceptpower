package edu.asu.conceptpower.app.core.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.conceptpower.app.core.model.impl.ReviewRequest;
import edu.asu.conceptpower.app.core.repository.IReviewRequestRepository;
import edu.asu.conceptpower.app.core.service.IReviewRequestService;

/**
 * 
 * @author Keerthivasan
 * 
 */
@Service
@Transactional
public class ReviewRequestService implements IReviewRequestService{
    
    @Autowired
    private IReviewRequestRepository dao;

    public ReviewRequestService() {
        super();
    }

    // API

    public void create(final ReviewRequest entity) {
        dao.create(entity);
    }

    public ReviewRequest findOne(final long id) {
        return dao.findOne(id);
    }

    public List<ReviewRequest> findAll() {
        return dao.findAll();
    }

    
}