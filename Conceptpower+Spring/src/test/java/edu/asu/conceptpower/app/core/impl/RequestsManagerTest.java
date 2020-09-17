package edu.asu.conceptpower.app.core.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.conceptpower.app.db4o.IRequestsDBManager;
import edu.asu.conceptpower.core.Comment;
import edu.asu.conceptpower.core.ReviewRequest;
import edu.asu.conceptpower.core.ReviewStatus;

public class RequestsManagerTest {
    
    @Mock
    private IRequestsDBManager dbClient;
 
    @InjectMocks
    private RequestsManager requestManager;
    
    private final String REVIEW_ID1 = "REVIEWf78d0a6e-a187-43df-b3be-3f22c040b5a1";
    private final String REVIEW_ID2 = "REVIEWf78d0a6e-a187-43df-b3be-3f22c040b5a2";
    private final String CONCEPT_ID1 = "WID-10126926-N-05-Einstein";
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        ReviewRequest req1 = new ReviewRequest();
        req1.setId(REVIEW_ID1);
        req1.setConceptId(CONCEPT_ID1);
        req1.setRequest("Review the Spelling again");
        req1.setRequester("admin");
        req1.setResolver("admin");
        req1.setCreatedAt(OffsetDateTime.parse("2020-01-21T10:47:02.873-07:00"));
        req1.setStatus(ReviewStatus.CLOSED);
        

        ReviewRequest req2 = new ReviewRequest();
        req2.setId(REVIEW_ID2);
        req2.setConceptId(CONCEPT_ID1);
        req2.setRequest("Review one more time");
        req2.setRequester("admin");
        req2.setResolver("admin");
        req2.setCreatedAt(OffsetDateTime.parse("2020-01-23T10:47:02.873-07:00"));
        req2.setStatus(ReviewStatus.OPENED);
        
        List<ReviewRequest> responseList = new ArrayList<>();  
        responseList.add(req1);
        responseList.add(req2);
        
        Mockito.when(dbClient.getReview(REVIEW_ID1)).thenReturn(req1);
        Mockito.when(dbClient.getAllReviews(CONCEPT_ID1)).thenReturn(responseList);
    }
    
    @Test
    public void test_addReviewRequest_success() {
        
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setStatus(ReviewStatus.OPENED);
        reviewRequest.setId(REVIEW_ID1);
        reviewRequest.setRequester("admin");
        reviewRequest.setConceptId(CONCEPT_ID1);
        reviewRequest.setRequest("Testing Method");
        
        requestManager.addReviewRequest(reviewRequest);
        Mockito.verify(dbClient).store(reviewRequest);
        
    }
    
    @Test
    public void test_getLatestReview_success() {
        ReviewRequest response = requestManager.getLatestReview(CONCEPT_ID1);
        
        assertNotNull(response);
        assertEquals(CONCEPT_ID1, response.getConceptId());
        assertEquals(REVIEW_ID2, response.getId());
        assertEquals("Review one more time", response.getRequest());
        assertEquals("admin", response.getRequester());
        assertEquals("admin", response.getResolver());
        assertEquals(ReviewStatus.OPENED, response.getStatus());
        
    }
    
    @Test
    public void test_getLatestReview_failure() {
        ReviewRequest fetchedReview = requestManager.getLatestReview("WID-10126926-N-05-Thomas");
        
        assertNull(fetchedReview);
    }
    
    @Test
    public void test_updateReview_success() {
        Comment comment = new Comment();     
        comment.setComment("Sample Comment");
        
        ReviewRequest response  = requestManager.updateReview(REVIEW_ID1, ReviewStatus.OPENED, comment, OffsetDateTime.now(), "admin");
        
        assertNotNull(response);
        assertEquals(1, response.getComments().size());
        assertEquals("Sample Comment",response.getComments().get(0).getComment());
        assertEquals(CONCEPT_ID1, response.getConceptId());
        assertEquals(REVIEW_ID1, response.getId());
        assertEquals("Review the Spelling again", response.getRequest());
        assertEquals("admin", response.getRequester());
        assertEquals(ReviewStatus.OPENED, response.getStatus());
    }
    
    @Test
    public void test_updateReview_invalidReviewId_failure() {
        ReviewRequest response  = requestManager.updateReview(REVIEW_ID2, ReviewStatus.OPENED, new Comment(), OffsetDateTime.now(), "admin");
        
        assertNull(response);
    }
    
    @Test
    public void test_updateReview_nullComment_failure() {
        ReviewRequest response  = requestManager.updateReview(REVIEW_ID1, ReviewStatus.OPENED, null, OffsetDateTime.now(), "admin");
        
        assertNull(response);
    }
    
    @Test
    public void test_getAllReviews_success() {
        List<ReviewRequest> response = requestManager.getAllReviews(CONCEPT_ID1);
        
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Review the Spelling again", response.get(0).getRequest());
        assertEquals("Review one more time", response.get(1).getRequest());
        assertEquals(REVIEW_ID1, response.get(0).getId());
        assertEquals(REVIEW_ID2, response.get(1).getId());
    }
    
    @Test
    public void test_getAllReviews_failure() {
        List<ReviewRequest> response = requestManager.getAllReviews("WID-10126926-N-05-Albert");
        assertEquals(true, response.isEmpty());
    }
}
