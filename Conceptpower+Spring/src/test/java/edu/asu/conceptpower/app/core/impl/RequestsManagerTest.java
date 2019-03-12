package edu.asu.conceptpower.app.core.impl;



import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.db4o.ObjectContainer;

import edu.asu.conceptpower.app.db.DatabaseManager;
import edu.asu.conceptpower.app.db4o.IRequestsDBManager;
import edu.asu.conceptpower.core.ReviewRequest;
import edu.asu.conceptpower.core.ReviewStatus;

@RunWith(MockitoJUnitRunner.class)
public class RequestsManagerTest {
    
   
    
    @InjectMocks
    ReviewRequest reviewRequest;
    
    @Mock
    RequestsManager requestsManager;

    @Mock
    IRequestsDBManager dbClient = Mockito.mock(IRequestsDBManager.class);
 
    @Mock
    private DatabaseManager dbManager;
    
    @Mock 
    private ObjectContainer client;

 
    @Test
    public void test_addReviewRequest() {
        
        reviewRequest.setStatus(ReviewStatus.OPENED);
        reviewRequest.setRequester("admin");
        reviewRequest.setConceptId("WID-01006675-N-02-mental_test");
        reviewRequest.setRequest("Testing Method");
        
        requestsManager.addReviewRequest(reviewRequest);
        
        Mockito.when(requestsManager.getReview("WID-01006675-N-02-mental_test")).thenReturn(reviewRequest);
        ReviewRequest fetchedReview = requestsManager.getReview("WID-01006675-N-02-mental_test");
        Mockito.verify(requestsManager).getReview("WID-01006675-N-02-mental_test");
        Assert.assertEquals("WID-01006675-N-02-mental_test", fetchedReview.getConceptId());
        Assert.assertEquals("Testing Method", fetchedReview.getRequest());


    }
    
    @Test
    public void test_getReview() {
        
        ReviewRequest reviewRequest =new ReviewRequest();
        reviewRequest.setStatus(ReviewStatus.OPENED);
        reviewRequest.setRequester("admin");
        reviewRequest.setConceptId("WID-10126926-N-05-Einstein");
        reviewRequest.setRequest("Testing Method");
        
        Mockito.when(requestsManager.getReview("WID-10126926-N-05-Einstein")).thenReturn(reviewRequest);

        ReviewRequest fetchedReview = requestsManager.getReview("WID-10126926-N-05-Einstein");
        Mockito.verify(requestsManager).getReview("WID-10126926-N-05-Einstein");

        Assert.assertEquals("WID-10126926-N-05-Einstein", fetchedReview.getConceptId());
        Assert.assertEquals("Testing Method", fetchedReview.getRequest());

    }
}
