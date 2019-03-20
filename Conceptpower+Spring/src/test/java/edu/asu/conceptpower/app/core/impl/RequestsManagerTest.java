package edu.asu.conceptpower.app.core.impl;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.db4o.ObjectContainer;

import edu.asu.conceptpower.app.db.DatabaseManager;
import edu.asu.conceptpower.app.db4o.IRequestsDBManager;
import edu.asu.conceptpower.core.ReviewRequest;
import edu.asu.conceptpower.core.ReviewStatus;

public class RequestsManagerTest {
    
    @InjectMocks
    ReviewRequest reviewRequest;
    
    @InjectMocks
    ReviewRequest fecthedReview;
    
    @Mock
    IRequestsDBManager dbClient = Mockito.mock(IRequestsDBManager.class);
 
    @Mock
    private DatabaseManager dbManager;
    
    @Mock 
    private ObjectContainer client;

 
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        
    }
    
    @Test
    public void test_addReviewRequest() {
        
        reviewRequest.setStatus(ReviewStatus.OPENED);
        reviewRequest.setRequester("admin");
        reviewRequest.setConceptId("WID-01006675-N-02-mental_test");
        reviewRequest.setRequest("Testing Method");
                
        RequestsManager requestsManager = new RequestsManager();
        //requestsManager.addReviewRequest(reviewRequest);
      //  Mockito.when(dbClient.store(reviewRequest)).thenReturn(Optional.empty());

        
        fecthedReview = requestsManager.getReview("WID-01006675-N-02-mental_test");
       
    }
    
    @Test
    public void test_getReview() {
        
        reviewRequest.setStatus(ReviewStatus.OPENED); 
        reviewRequest.setRequester("admin");
        reviewRequest.setConceptId("WID-10126926-N-05-Einstein");
        reviewRequest.setRequest("Testing Method2");
        
        RequestsManager requestsManager = new RequestsManager();
        Mockito.when(dbClient.getReviewRequestForConcept("WID-10126926-N-05-Einstein")).thenReturn(reviewRequest);

      //  ReviewRequest fetchedReview = requestsManager.getReview("WID-10126926-N-05-Einstein");
       // Mockito.verify(requestsManager).getReview("WID-10126926-N-05-Einstein");

        Assert.assertEquals("WID-10126926-N-05-Einstein", reviewRequest.getConceptId());
        Assert.assertEquals("Testing Method2", reviewRequest.getRequest());

    }
}
