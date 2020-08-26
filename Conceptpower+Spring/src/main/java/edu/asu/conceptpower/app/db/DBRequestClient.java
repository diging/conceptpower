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
        client.store(reviewRequest);
        client.commit();
    }
    
    @Override
    public ReviewRequest getReview(String reviewId) {
        List<ReviewRequest> responses =  client.query(new Predicate<ReviewRequest>() {
            private static final long serialVersionUID = 6495914730735826451L;

            @Override
            public boolean match(ReviewRequest review) {
                return review.getId().equals(reviewId);
            }
            
        });
        
        if(responses == null || responses.size() == 0) {
            return null;
        }
        return responses.get(0);
    }
    
    @Override
    public List<ReviewRequest> getAllReviews(String  conceptId){
        ReviewRequest request = new ReviewRequest();
        request.setConceptId(conceptId);
        
        return client.queryByExample(request);
    }

    @Override
    public List<ReviewRequest> getAllReviews() {
        return client.query(ReviewRequest.class);
    }
    
}
