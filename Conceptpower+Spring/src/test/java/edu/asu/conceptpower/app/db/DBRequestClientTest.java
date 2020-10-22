package edu.asu.conceptpower.app.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.conceptpower.app.model.ReviewRequest;
import edu.asu.conceptpower.app.repository.IReviewRequestRepository;

public class DBRequestClientTest {
    
    @InjectMocks
    private DBRequestClient client;
    
    @Mock
    private IReviewRequestRepository reviewRequestRepository;
    
    private static final String REVIEW_ID = "REVIEWf78d0a6e-a187-43df-b3be-3f22c040b5a1";
    private static final String CONCEPT_ID = "WID-10126926-N-05-Einstein";
    

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        
        ReviewRequest request1 = new ReviewRequest();
        request1.setConceptId(CONCEPT_ID);
        List<ReviewRequest> resultList = new ArrayList<>();
        resultList.add(request1);
        
        ReviewRequest request2 = new ReviewRequest();
        request2.setId(REVIEW_ID); 
        Optional<ReviewRequest> optionalRequest = Optional.of(request2);
        
        //We are stubbing the methods with Mockito.any() as we verify the correctness of the parameter in the test method using customMatchers.
        Mockito.when(reviewRequestRepository.findById(REVIEW_ID)).thenReturn(optionalRequest);
        Mockito.when(reviewRequestRepository.findAllByConceptId(CONCEPT_ID)).thenReturn(resultList);
    }
    
    @Test
    public void test_getReview_success() {        
        ReviewRequest response  = client.getReview(REVIEW_ID);
        
        
        Mockito.verify(reviewRequestRepository).findById(REVIEW_ID);
        assertNotNull(response);
        assertEquals(REVIEW_ID, response.getId());
    }
    
    @Test
    public void test_getAllReviews_success() {
        ReviewRequest request = new ReviewRequest();
        request.setConceptId(CONCEPT_ID);
        
        List<ReviewRequest> response = client.getAllReviews(CONCEPT_ID);
        
        
        Mockito.verify(reviewRequestRepository).findAllByConceptId(CONCEPT_ID);
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(CONCEPT_ID, response.get(0).getConceptId());
    }
}