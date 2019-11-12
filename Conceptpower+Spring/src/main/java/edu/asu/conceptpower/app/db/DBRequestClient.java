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
