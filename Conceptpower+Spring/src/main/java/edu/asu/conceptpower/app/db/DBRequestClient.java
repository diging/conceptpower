package edu.asu.conceptpower.app.db;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.db4o.ObjectContainer;

import edu.asu.conceptpower.app.db4o.IRequestsDBManager;
import edu.asu.conceptpower.core.ReviewRequest;
import jakarta.annotation.PostConstruct;

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
        
        List<ReviewRequest> reviewRequests = client.queryByExample(request);
        
        if(reviewRequests!= null && reviewRequests.size()>0) {
            return reviewRequests.get(0);
        }
        return null;
    }
   
}
