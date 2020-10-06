package edu.asu.conceptpower.app.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

import edu.asu.conceptpower.app.db.custommatchers.PredicateReviewRequestMatcher;
import edu.asu.conceptpower.app.db.custommatchers.ReviewRequestMatcher;
import edu.asu.conceptpower.app.db.customobjects.PredicateObjectSet;
import edu.asu.conceptpower.app.db.customobjects.ReviewRequestObjectSet;
import edu.asu.conceptpower.app.model.ReviewRequest;

public class DBRequestClientTest {
    
    @InjectMocks
    private DBRequestClient client;
    
    @Mock
    private ObjectContainer objectContainerClient;
    
    private static final String REVIEW_ID = "REVIEWf78d0a6e-a187-43df-b3be-3f22c040b5a1";
    private static final String CONCEPT_ID = "WID-10126926-N-05-Einstein";
    

    @SuppressWarnings("deprecation")
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        
        ReviewRequest request1 = new ReviewRequest();
        request1.setConceptId(CONCEPT_ID);
        ReviewRequestObjectSet reviewRequestObjectSet = new ReviewRequestObjectSet();
        reviewRequestObjectSet.add(request1);
        
        ReviewRequest request2 = new ReviewRequest();
        request2.setId(REVIEW_ID);        
        PredicateObjectSet predicateObjectSet = new PredicateObjectSet();
        predicateObjectSet.add(request2);

        //We are stubbing the methods with Mockito.any() as we verify the correctness of the parameter in the test method using customMatchers.
        Mockito.when(objectContainerClient.queryByExample(Mockito.any(ReviewRequest.class))).thenReturn((ObjectSet<Object>)reviewRequestObjectSet);
        Mockito.when(objectContainerClient.query(Mockito.<Predicate<ReviewRequest>>anyObject())).thenReturn(predicateObjectSet);
    }
    
    @Test
    public void test_getReview_success() {        
        Predicate<ReviewRequest> request = new Predicate<ReviewRequest>() {
            private static final long serialVersionUID = 6495914730735826451L;

            @Override
            public boolean match(ReviewRequest review) {
                return review.getId().equals(REVIEW_ID);
            }
            
        };
        
        ReviewRequest response  = client.getReview(REVIEW_ID);
        
        
        Mockito.verify(objectContainerClient).query(Mockito.argThat(new PredicateReviewRequestMatcher(request, REVIEW_ID)));
        assertNotNull(response);
        assertEquals(REVIEW_ID, response.getId());
    }
    
    @Test
    public void test_getAllReviews_success() {
        ReviewRequest request = new ReviewRequest();
        request.setConceptId(CONCEPT_ID);
        
        List<ReviewRequest> response = client.getAllReviews(CONCEPT_ID);
        
        
        Mockito.verify(objectContainerClient).queryByExample(Mockito.argThat(new ReviewRequestMatcher(request)));
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(CONCEPT_ID, response.get(0).getConceptId());
    }
}