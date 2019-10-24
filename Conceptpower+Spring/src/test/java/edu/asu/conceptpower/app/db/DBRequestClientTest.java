package edu.asu.conceptpower.app.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        
        objectContainerClient = Mockito.mock(ObjectContainer.class);
        
        reviewRequest.setConceptId("WID-10126926-N-05-Einstein");
        reviewRequest.setRequest("Review the Spelling");
        reviewRequest.setRequester("user2");
        reviewRequest.setResolver("admin");
        reviewRequest.setStatus(ReviewStatus.OPENED);
        
        updateRequest.setConceptId("WID-10126926-N-05-Einstein");
        updateRequest.setResolvingComment("Resolving Comment");
        updateRequest.setStatus(ReviewStatus.RESOLVED);
        updateRequest.setResolver("User3");

        Mockito.when(client.getReviewRequestForConcept(reviewRequest.getConceptId())).thenReturn(reviewRequest);
        
        ReviewRequest updatedRequest = new ReviewRequest();
        
        updatedRequest.setConceptId("WID-10126926-N-05-Einstein");
        updatedRequest.setRequest("Review the Spelling");
        updatedRequest.setRequester("user2");
        updatedRequest.setStatus(ReviewStatus.RESOLVED);
        updatedRequest.setResolver("User3");
        updatedRequest.setResolvingComment("Resolving Comment");
        
        Mockito.when(client.updateReviewRequest(updateRequest)).thenReturn(updatedRequest);
        
        ReviewRequest reopenedRequest = new ReviewRequest();
        
        reopenedRequest.setConceptId("WID-10126926-N-05-Einstein");
        reopenedRequest.setRequest("Review the Spelling");
        reopenedRequest.setRequester("user2");
        reopenedRequest.setStatus(ReviewStatus.OPENED);
        
        Mockito.when(client.reopenReviewRequest("WID-10126926-N-05-Einstein")).thenReturn(reopenedRequest);
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
        assertEquals("Resolving Comment", response.getResolvingComment());
    }
    
    @Test
    public void reopenReviewRequestTest() {
        ReviewRequest response = client.reopenReviewRequest("WID-10126926-N-05-Einstein");
        
        assertNotNull(response);
        
        assertEquals("WID-10126926-N-05-Einstein", response.getConceptId());
        assertEquals("Review the Spelling", response.getRequest());
        assertEquals("user2", response.getRequester());
        assertEquals(null, response.getResolver());
        assertEquals(ReviewStatus.OPENED, response.getStatus());
        assertEquals(null, response.getResolvingComment());
    }
}