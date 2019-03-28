package edu.asu.conceptpower.app.db;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.app.db.repository.UserRepository;
import edu.asu.conceptpower.app.model.ReviewRequest;
import edu.asu.conceptpower.app.mysql.IRequestsDBManager;



@Component
public class MySqlDBRequestClient implements IRequestsDBManager{


    @Autowired
    private UserRepository usrRepository;
    
    @Override
    public void store(ReviewRequest reviewRequest) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ReviewRequest getReviewRequestForConcept(String conceptId) {
        // TODO Auto-generated method stub
        return null;
    }
   
}