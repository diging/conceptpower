package edu.asu.conceptpower.app.db;

import java.util.List;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;

import edu.asu.conceptpower.app.db4o.IRequestsDBManager;
import edu.asu.conceptpower.core.ReviewRequest;

@Component
public class DBRequestClient implements IRequestsDBManager{

    @Autowired
    @Qualifier("conceptReviewDatabaseManager")
    private DatabaseManager dbManager;
    
    private ObjectContainer client;

    @PostConstruct
    public void init() {
        this.client = dbManager.getClient();
    }
    
    @Override
    public void store(ReviewRequest reviewRequest) {
        ReviewRequest storeRequest= new ReviewRequest();
        
        storeRequest.setConceptId(reviewRequest.getConceptId());
        storeRequest.setStatus(reviewRequest.getStatus());
        storeRequest.setRequest(reviewRequest.getRequest());
        storeRequest.setRequester(reviewRequest.getRequester());
        
        client.store(storeRequest);
        client.commit();
    }

    @Override
    public ReviewRequest getReviewRequestForConcept(String conceptId){
        
        ReviewRequest request = new ReviewRequest();
        request.setConceptId(conceptId);
        
        List<ReviewRequest> reviewRequests = client.queryByExample(request);
        if(reviewRequests!= null && reviewRequests.size()>0) {
            return reviewRequests.get(reviewRequests.size() - 1);
        }
        
        return null;
    }
    
    @Override
    public List<ReviewRequest> getAllReviewsForaConcept(String conceptId){
        ReviewRequest request = new ReviewRequest();
        request.setConceptId(conceptId);
        
        List<ReviewRequest> reviewRequests = client.queryByExample(request);
        
        return reviewRequests;
    }
    
    @Override
    public ReviewRequest updateReviewRequest(ReviewRequest reviewRequest) {
        
        List<ReviewRequest> requests = client.query(new Predicate<ReviewRequest>() {
            private static final long serialVersionUID = 6495914730735826451L;

            @Override
            public boolean match(ReviewRequest review) {
                return review.getConceptId().equals(reviewRequest.getConceptId());
            }
            
        });
       
        List<String> comments  = requests.get(requests.size() - 1).getResolvingComment();
        comments.addAll(reviewRequest.getResolvingComment());
        
        requests.get(requests.size() - 1).setResolvingComment(comments);
        requests.get(requests.size() - 1).setStatus(reviewRequest.getStatus());
        requests.get(requests.size() - 1).setResolver(reviewRequest.getResolver());
        
        ReviewRequest updatedRequest = requests.get(requests.size() - 1);
        
        client.store(requests.toArray());
        client.commit();
        
        return updatedRequest;
    }
    
    @Override
    public ReviewRequest reopenReviewRequest(ReviewRequest reviewRequest) {
        List<ReviewRequest> requests = client.query(new Predicate<ReviewRequest>() {
            private static final long serialVersionUID = 6495914730735826451L;

            @Override
            public boolean match(ReviewRequest review) {
                return review.getConceptId().equals(reviewRequest.getConceptId());
            }
            
        });
        ReviewRequest requestToBeUpdated = requests.get(requests.size() - 1);
        
        requestToBeUpdated.setStatus(reviewRequest.getStatus());
        requestToBeUpdated.setResolver(reviewRequest.getResolver());
        
        client.store(requestToBeUpdated);
        client.commit();
        
        return requestToBeUpdated;
    }
}
