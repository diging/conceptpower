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
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        
        objectContainerClient = Mockito.mock(ObjectContainer.class);
        
        reviewRequest.setConceptId("WID-10126926-N-05-Einstein");
        reviewRequest.setRequest("Review the Spelling");
        reviewRequest.setRequester("user2");
        reviewRequest.setResolver("admin");
        reviewRequest.setStatus(ReviewStatus.OPENED);

        Mockito.when(client.getReviewRequestForConcept(reviewRequest.getConceptId())).thenReturn(reviewRequest);
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
}