package edu.asu.conceptpower.app.db.CustomMatchers;

import org.mockito.ArgumentMatcher;

import edu.asu.conceptpower.core.ReviewRequest;

/**
 * This class is a custom argument matcher to compare ReviewRequest classes
 * 
 * @author keerthivasan
 *
 */

public class ReviewRequestMatcher implements ArgumentMatcher<ReviewRequest> {
 
    private ReviewRequest left;
 
    public ReviewRequestMatcher(ReviewRequest request) {
        this.left = request;
    }
 
    @Override
    public boolean matches(ReviewRequest right) {
        return left.getConceptId().equals(right.getConceptId());
    }
}