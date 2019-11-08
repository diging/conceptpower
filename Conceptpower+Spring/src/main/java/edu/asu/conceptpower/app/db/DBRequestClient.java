package edu.asu.conceptpower.app.db;

import java.util.ArrayList;
import java.util.Collections;
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
        client.store(reviewRequest);
        client.commit();
    }

    @Override
    public ReviewRequest getReviewRequestForConcept(String conceptId){
        
        ReviewRequest request = new ReviewRequest();
        request.setConceptId(conceptId);
        
        //ArrayList is initialized to make the instance mutable for sorting
        List<ReviewRequest> reviewRequests = new ArrayList<>(client.queryByExample(request));
       
        if(reviewRequests!= null && reviewRequests.size()>0) {
            Collections.sort(reviewRequests, (x, y) -> x.getCreatedAt().compareTo(y.getCreatedAt()));
            return reviewRequests.get(reviewRequests.size() - 1);
        }
        
        return null;
    }
    
    public List<ReviewRequest> getReview(ReviewRequest request) {
        return client.query(new Predicate<ReviewRequest>() {
            private static final long serialVersionUID = 6495914730735826451L;

            @Override
            public boolean match(ReviewRequest review) {
                return review.getId().equals(request.getId());
            }
            
        });
    }
    
    @Override
    public List<ReviewRequest> getAllReviews(ReviewRequest reviewRequest){
        return client.queryByExample(reviewRequest);
    }
    
    @Override
    public void updateReviewRequest(Object reviewRequest) {
        client.store(reviewRequest);
        client.commit();
    }
}
