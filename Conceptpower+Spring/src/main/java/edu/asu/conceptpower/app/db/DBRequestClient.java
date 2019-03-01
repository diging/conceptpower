package edu.asu.conceptpower.app.db;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

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
    public ObjectSet<ReviewRequest> getReviewRequestForConcept(Object reviewRequest){
       return client.queryByExample(reviewRequest);
    }
   
}
