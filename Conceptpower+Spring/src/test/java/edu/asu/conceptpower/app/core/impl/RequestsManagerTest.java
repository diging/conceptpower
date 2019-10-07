package edu.asu.conceptpower.app.core.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.conceptpower.app.db4o.IRequestsDBManager;
import edu.asu.conceptpower.core.ReviewRequest;
import edu.asu.conceptpower.core.ReviewStatus;

public class RequestsManagerTest {
    
    @Mock
    private IRequestsDBManager dbClient;
 
    @InjectMocks
    private RequestsManager requestManager;
 
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        
    }
    
    @Test
    public void test_addReviewRequest_success() {
        
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setStatus(ReviewStatus.OPENED);
        reviewRequest.setRequester("admin");
        reviewRequest.setConceptId("WID-01006675-N-02-mental_test");
        reviewRequest.setRequest("Testing Method");
           
        requestManager.addReviewRequest(reviewRequest);
        Mockito.verify(dbClient).store(reviewRequest);
        
    }
    
    @Test
    public void test_getReview_success() {
        
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setStatus(ReviewStatus.OPENED); 
        reviewRequest.setRequester("admin");
        reviewRequest.setConceptId("WID-10126926-N-05-Einstein");
        reviewRequest.setRequest("Testing Method2");
        
        Mockito.when(dbClient.getReviewRequestForConcept("WID-10126926-N-05-Einstein")).thenReturn(reviewRequest);

        ReviewRequest fetchedReview = requestManager.getReview("WID-10126926-N-05-Einstein");
     
        Assert.assertEquals("WID-10126926-N-05-Einstein", fetchedReview.getConceptId());
        Assert.assertEquals("admin", fetchedReview.getRequester());
        Assert.assertEquals(ReviewStatus.OPENED, fetchedReview.getStatus());
        Assert.assertEquals("Testing Method2", fetchedReview.getRequest());
    }
    
    @Test
    public void test_getReview_failure() {
        
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setStatus(ReviewStatus.OPENED); 
        reviewRequest.setRequester("admin");
        reviewRequest.setConceptId("WID-10126926-N-05-Einstein");
        reviewRequest.setRequest("Testing Method2");
        
        Mockito.when(dbClient.getReviewRequestForConcept("WID-10126926-N-05-Einstein")).thenReturn(reviewRequest);

        ReviewRequest fetchedReview = requestManager.getReview("WID-10126926-N-05-Thomas");
        
        Assert.assertNull(fetchedReview);
    }
}
