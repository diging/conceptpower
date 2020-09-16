package edu.asu.conceptpower.app.db.custommatchers;

import org.mockito.ArgumentMatcher;

import edu.asu.conceptpower.core.ReviewRequest;

/**
 * This class is a custom argument matcher to compare ReviewRequest classes
 * 
 * @author Keerthivasan Krishnamurthy
 *
 */

public class ReviewRequestMatcher implements ArgumentMatcher<ReviewRequest> {
 
    private ReviewRequest validRequest;
 
    public ReviewRequestMatcher(ReviewRequest request) {
        this.validRequest = request;
    }
 
    @Override
    public boolean matches(ReviewRequest toBeValidatedRequest) {
        return validRequest.getConceptId().equals(toBeValidatedRequest.getConceptId());
    }
}