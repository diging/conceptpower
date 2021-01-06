package edu.asu.conceptpower.app.db;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.app.manager.IRequestsDBManager;
import edu.asu.conceptpower.app.model.ReviewRequest;
import edu.asu.conceptpower.app.repository.IReviewRequestRepository;

@Component
public class DBRequestClient implements IRequestsDBManager{
     
    @Autowired
    private IReviewRequestRepository reviewRequestRepository;
    
    @Override
    public void store(ReviewRequest reviewRequest) { 
        reviewRequestRepository.save(reviewRequest);
    }
    
    @Override
    public ReviewRequest getReview(String reviewId) {
        Optional<ReviewRequest> response =  reviewRequestRepository.findById(reviewId);
        
        return response.isPresent() ? response.get() : null;
    }
    
    @Override
    public List<ReviewRequest> getAllReviews(String  conceptId){
        return reviewRequestRepository.findAllByConceptId(conceptId);
    }

    @Override
    public List<ReviewRequest> getAllReviews() {
        return reviewRequestRepository.findAll();
    }
    
}
