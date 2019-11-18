package edu.asu.conceptpower.app.core.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
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
    
    private ReviewRequest request = new ReviewRequest();
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        
        List<ReviewRequest> responseList = new ArrayList<>();
        ReviewRequest updatedRequest = new ReviewRequest();
        request.setId("REVIEW1234-thisissupposedtobesecret");
        
        updatedRequest.setId("REVIEW1234-thisissupposedtobesecret2");
        updatedRequest.setConceptId("WID-10126926-N-05-Einstein");
        updatedRequest.setRequest("Review the Spelling again");
        updatedRequest.setRequester("admin");
        updatedRequest.setResolver("admin");
        updatedRequest.setCreatedAt(LocalDateTime.now());
        updatedRequest.setStatus(ReviewStatus.OPENED);
        
        responseList.add(updatedRequest);
        Mockito.when(dbClient.getReview(ArgumentMatchers.any(ReviewRequest.class))).thenReturn(updatedRequest);
        
        Mockito.when(dbClient.getAllReviews(ArgumentMatchers.any(ReviewRequest.class))).thenReturn(responseList);
    }
    
    @Test
    public void test_addReviewRequest_success() {
        
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setStatus(ReviewStatus.OPENED);
        reviewRequest.setRequester("admin");
        reviewRequest.setConceptId("WID-01006675-N-02-mental_test");
        reviewRequest.setRequest("Testing Method");
           
        Mockito.when(dbClient.getReview(ArgumentMatchers.any(ReviewRequest.class))).thenReturn(null);
        
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
        
        List<ReviewRequest> response = new ArrayList<>();
        response.add(reviewRequest);
        Mockito.when(dbClient.getAllReviews(ArgumentMatchers.any(ReviewRequest.class))).thenReturn(response);

        ReviewRequest fetchedReview = requestManager.getReview("WID-10126926-N-05-Einstein");
     
        Assert.assertEquals("WID-10126926-N-05-Einstein", fetchedReview.getConceptId());
        Assert.assertEquals("admin", fetchedReview.getRequester());
        Assert.assertEquals(ReviewStatus.OPENED, fetchedReview.getStatus());
        Assert.assertEquals("Testing Method2", fetchedReview.getRequest());
    }
    
    @Test
    public void test_getReview_failure() {
        Mockito.when(dbClient.getAllReviews(ArgumentMatchers.any(ReviewRequest.class))).thenReturn(null);

        ReviewRequest fetchedReview = requestManager.getReview("WID-10126926-N-05-Thomas");
        
        Assert.assertNull(fetchedReview);
    }
    
    @Test
    public void updateReviewTest() {
        ReviewRequest response  = requestManager.updateReview(request);
        
        assertNotNull(response);
        
        assertEquals("WID-10126926-N-05-Einstein", response.getConceptId());
        assertEquals("REVIEW1234-thisissupposedtobesecret2", response.getId());
        assertEquals("Review the Spelling again", response.getRequest());
        assertEquals("admin", response.getRequester());
    }
    
    @Test
    public void getAllReviewsTest() {
        List<ReviewRequest> response = requestManager.getAllReviews("WID-10126926-N-05-Einstein");
        
        assertNotNull(response);
        
        assertEquals(1, response.size());
        assertEquals("Review the Spelling again", response.get(0).getRequest());
        assertEquals("REVIEW1234-thisissupposedtobesecret2", response.get(0).getId());
    }
    
    @Test
    public void getReviewTest() {
        ReviewRequest response = requestManager.getReview("WID-10126926-N-05-Einstein");
        
        assertNotNull(response);
        
        assertEquals("WID-10126926-N-05-Einstein", response.getConceptId());
        assertEquals("REVIEW1234-thisissupposedtobesecret2", response.getId());
        assertEquals("Review the Spelling again", response.getRequest());
        assertEquals("admin", response.getRequester());
        assertEquals("admin", response.getResolver());
        assertEquals(ReviewStatus.OPENED, response.getStatus());
        
    }
}
