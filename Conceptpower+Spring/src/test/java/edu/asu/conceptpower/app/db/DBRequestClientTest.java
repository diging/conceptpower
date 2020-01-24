package edu.asu.conceptpower.app.db;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;

import edu.asu.conceptpower.app.db.CustomMatchers.PredicateReviewRequestMatcher;
import edu.asu.conceptpower.app.db.CustomMatchers.ReviewRequestMatcher;
import edu.asu.conceptpower.core.ReviewRequest;

public class DBRequestClientTest {
    
    @InjectMocks
    private DBRequestClient client;
    
    @Mock
    private ObjectContainer objectContainerClient;
    
    private static final String REVIEW_ID = "REVIEWf78d0a6e-a187-43df-b3be-3f22c040b5a1";
    private static final String CONCEPT_ID = "WID-10126926-N-05-Einstein";
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
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
        

        client.getReview(REVIEW_ID);
        Mockito.verify(objectContainerClient).query(Mockito.argThat(new PredicateReviewRequestMatcher(request, REVIEW_ID)));
    }
    
    @Test
    public void test_getAllReviews_success() {
        ReviewRequest request = new ReviewRequest();
        request.setConceptId(CONCEPT_ID);
        
        client.getAllReviews(CONCEPT_ID);
        Mockito.verify(objectContainerClient).queryByExample(Mockito.argThat(new ReviewRequestMatcher(request)));
    }
}