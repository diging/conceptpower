package edu.asu.conceptpower.app.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.db4o.ObjectContainer;

import edu.asu.conceptpower.core.ReviewRequest;
import edu.asu.conceptpower.core.ReviewStatus;

public class DBRequestClientTest {
    
    @Mock
    private DBRequestClient client;
    
    @Mock
    private ObjectContainer objectContainerClient;
    
    private ReviewRequest reviewRequest = new ReviewRequest();
    
    private ReviewRequest updateRequest = new ReviewRequest();

    private ReviewRequest reopenRequest  = new ReviewRequest();
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        
        client = Mockito.mock(DBRequestClient.class);
        
        reviewRequest.setConceptId("WID-10126926-N-05-Einstein");
        reviewRequest.setRequest("Review the Spelling");
        reviewRequest.setRequester("user2");
        reviewRequest.setResolver("admin");
        reviewRequest.setStatus(ReviewStatus.OPENED);
        
        updateRequest.setConceptId("WID-10126926-N-05-Einstein");
        updateRequest.setResolvingComment(new ArrayList<>(Arrays.asList("Resolving Comment")));
        updateRequest.setStatus(ReviewStatus.RESOLVED);
        updateRequest.setResolver("User3");

        Mockito.when(client.getReviewRequestForConcept(reviewRequest.getConceptId())).thenReturn(reviewRequest);
        
        ReviewRequest updatedRequest = new ReviewRequest();
        
        updatedRequest.setConceptId("WID-10126926-N-05-Einstein");
        updatedRequest.setRequest("Review the Spelling");
        updatedRequest.setRequester("user2");
        updatedRequest.setStatus(ReviewStatus.RESOLVED);
        updatedRequest.setResolver("User3");
        updatedRequest.setResolvingComment(new ArrayList<>(Arrays.asList("Resolving Comment")));
        
        Mockito.when(client.updateReviewRequest(updateRequest)).thenReturn(updatedRequest);
        
        reopenRequest.setConceptId("WID-10126926-N-05-Einstein");
        reopenRequest.setStatus(ReviewStatus.OPENED);
        reopenRequest.setResolvingComment(new ArrayList<>(Arrays.asList("Reopening Comment")));
        
        ReviewRequest reopenedRequest = new ReviewRequest();
        
        reopenedRequest.setConceptId("WID-10126926-N-05-Einstein");
        reopenedRequest.setRequest("Review the Spelling");
        reopenedRequest.setRequester("user2");
        reopenedRequest.setStatus(ReviewStatus.OPENED);
        reopenedRequest.setResolvingComment(new ArrayList<>(Arrays.asList("Reopening Comment")));
        
        Mockito.when(client.reopenReviewRequest(reopenRequest)).thenReturn(reopenedRequest);
        
        List<ReviewRequest> responseList = new ArrayList<>();
        ReviewRequest newReview = new ReviewRequest();
        
        newReview.setConceptId("WID-10126926-N-05-Einstein");
        newReview.setRequest("Review the Spelling again");
        newReview.setRequester("user2");
        newReview.setResolver("admin");
        newReview.setStatus(ReviewStatus.OPENED);
        
        responseList.add(newReview);
        responseList.add(reopenedRequest);
        Mockito.when(client.getAllReviewsForaConcept(reviewRequest.getConceptId())).thenReturn(responseList);
    }
    @Test
    public void getReviewRequestTest() {
        ReviewRequest response = client.getReviewRequestForConcept("WID-10126926-N-05-Einstein");
        
        assertNotNull(response);
        
        assertEquals("WID-10126926-N-05-Einstein", response.getConceptId());
        assertEquals("Review the Spelling", response.getRequest());
        assertEquals("user2", response.getRequester());
        assertEquals("admin", response.getResolver());
        assertEquals(ReviewStatus.OPENED, response.getStatus());
    }
    
    @Test
    public void updateReviewRequestTest() {
        ReviewRequest response  = client.updateReviewRequest(updateRequest);
        
        assertNotNull(response);
        
        assertEquals("WID-10126926-N-05-Einstein", response.getConceptId());
        assertEquals("Review the Spelling", response.getRequest());
        assertEquals("user2", response.getRequester());
        assertEquals("User3", response.getResolver());
        assertEquals(ReviewStatus.RESOLVED, response.getStatus());
        assertEquals(new ArrayList<>(Arrays.asList("Resolving Comment")), response.getResolvingComment());
    }
    
    @Test
    public void reopenReviewRequestTest() {
        ReviewRequest response = client.reopenReviewRequest(reopenRequest);
        
        assertNotNull(response);
        
        assertEquals("WID-10126926-N-05-Einstein", response.getConceptId());
        assertEquals("Review the Spelling", response.getRequest());
        assertEquals("user2", response.getRequester());
        assertEquals(null, response.getResolver());
        assertEquals(ReviewStatus.OPENED, response.getStatus());
        assertEquals(new ArrayList<>(Arrays.asList("Reopening Comment")), response.getResolvingComment());
    }
    
    @Test
    public void getAllReviewsForaConceptTest() {
        List<ReviewRequest> response = client.getAllReviewsForaConcept("WID-10126926-N-05-Einstein");
        
        assertNotNull(response);
        
        assertEquals(2, response.size());
        assertEquals("Review the Spelling again", response.get(0).getRequest());
        assertEquals("Review the Spelling", response.get(1).getRequest());
    }
}