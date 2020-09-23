package edu.asu.conceptpower.app.manager;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.IReviewRequestService;
import edu.asu.conceptpower.app.model.Comment;
import edu.asu.conceptpower.app.model.ReviewRequest;
import edu.asu.conceptpower.app.model.ReviewStatus;
import edu.asu.conceptpower.app.repository.IReviewRequestRepository;

@Service
public class ReviewRequestService implements IReviewRequestService{
    
    protected final String REVIEW_PREFIX = "REVIEW";
    
    
    @Autowired
    private IReviewRequestRepository reviewRequestRepository;

    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.IRequestsManager#addReviewRequest(edu.asu.conceptpower.core.ReviewRequest)
     */
    public void addReviewRequest(ReviewRequest reviewRequest) {
        reviewRequest.setId(generateId(REVIEW_PREFIX));
        
        reviewRequestRepository.save(reviewRequest);
    }
    
    public ReviewRequest getLatestReview(String conceptId) {
        List<ReviewRequest> reviewRequests = getAllReviews(conceptId);
       
        if(reviewRequests!= null && !reviewRequests.isEmpty()) {
            return reviewRequests.get(reviewRequests.size() - 1);
        }
        
        return null;
    }
  
    public List<ReviewRequest> getAllReviews(String conceptId){
        List<ReviewRequest> reviewRequests = reviewRequestRepository.findAllByConceptId(conceptId);
        
        if(reviewRequests!= null && !reviewRequests.isEmpty()) {
            reviewRequests = new ArrayList<>(reviewRequests);
            Collections.sort(reviewRequests, Comparator.comparing(ReviewRequest :: getCreatedAt, Comparator.nullsFirst(Comparator.naturalOrder())));
            return reviewRequests;
        }
        
        return Collections.emptyList();
    }
    
    /* (non-Javadoc)
     * @see edu.asu.conceptpower.app.core.IRequestsManager#updateReview(edu.asu.conceptpower.core.ReviewRequest)
     */
    
    public ReviewRequest updateReview(String reviewId, ReviewStatus reviewStatus, Comment comment, OffsetDateTime createdAt,String updatedBy) {  
        Optional<ReviewRequest> request = reviewRequestRepository.findById(reviewId);
        
        if(!request.isPresent()) {
            return null;
        }
        
        ReviewRequest storedRequest = request.get();
        
        if(comment != null) {
            comment.setCreatedAt(createdAt);
            comment.setCreatedBy(updatedBy);
            
            storedRequest.getComments().add(comment);
            storedRequest.setResolver(updatedBy);
            storedRequest.setStatus(reviewStatus);
            
            reviewRequestRepository.save(storedRequest);
            return storedRequest;
        }
        return null;
    }
    
    /*
     * =================================================================
     * Protected/Private methods
     * =================================================================
     */
    protected String generateId(String prefix) {
        String id = prefix + UUID.randomUUID().toString();

        while (true) {
            // if there doesn't exist an object with this id return id
            boolean results = reviewRequestRepository.existsById(id);
            if (!results)
                return id;

            // try other id
            id = prefix + UUID.randomUUID().toString();
        }
    }

    @Override
    public List<ReviewRequest> getAllReviews() {
       return reviewRequestRepository.findAll();
    }
}
