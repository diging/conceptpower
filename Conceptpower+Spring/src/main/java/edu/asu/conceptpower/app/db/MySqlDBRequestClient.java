package edu.asu.conceptpower.app.db;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.app.db.repository.UserRepository;
import edu.asu.conceptpower.app.model.ReviewRequest;
import edu.asu.conceptpower.app.mysql.IRequestsDBManager;



@Component
public class MySqlDBRequestClient implements IRequestsDBManager{


    @Autowired
    private CrudRepository<ReviewRequest,Integer> usrRepository;
    
    @Override
    public void store(ReviewRequest reviewRequest) {
        
        usrRepository.save(reviewRequest);
        
    }

    @Override
    @Query("Select request from ReviewRequest where conceptid = ?1 ")
    public String getReviewRequestForConcept(String conceptId) {
        
        
        return null;
    }

    @Override
    public Iterable<ReviewRequest> getAllReviewRequest() {
       
        return usrRepository.findAll();
    }

    @Override
    public void deleteRequest(ReviewRequest reviewRequest) {

        usrRepository.delete(reviewRequest);
        
    }
    
    
   
}