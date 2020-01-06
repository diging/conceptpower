package edu.asu.conceptpower.app.db;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;

import edu.asu.conceptpower.core.ReviewRequest;

public class DBRequestClientTest {
    
    @InjectMocks
    private DBRequestClient client;
    
    @Mock
    private ObjectContainer objectContainerClient;
    
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void getAllReviewsTest() {
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setConceptId("WID-10126926-N-05-Einstein");
        
        client.getAllReviews(reviewRequest);
        Mockito.verify(objectContainerClient).queryByExample(reviewRequest);
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void getReviewTest() {
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setConceptId("WID-10126926-N-05-Einstein");
        
        client.getReview(reviewRequest);
        Mockito.verify(objectContainerClient).query(Mockito.<Predicate<ReviewRequest>>anyObject());
    }
}