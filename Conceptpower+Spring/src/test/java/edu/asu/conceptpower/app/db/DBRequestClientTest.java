package edu.asu.conceptpower.app.db;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
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
        client.getAllReviews("WID-10126926-N-05-Einstein");
        Mockito.verify(objectContainerClient).queryByExample(ArgumentMatchers.any(ReviewRequest.class));
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void getReviewTest() {
        client.getReview("REVIEW1234-thisissupposedtobesecret2");
        Mockito.verify(objectContainerClient).query(Mockito.<Predicate<ReviewRequest>>anyObject());
    }
}