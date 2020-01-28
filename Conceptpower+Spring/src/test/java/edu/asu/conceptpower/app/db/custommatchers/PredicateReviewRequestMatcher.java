package edu.asu.conceptpower.app.db.custommatchers;

import org.mockito.ArgumentMatcher;

import com.db4o.query.Predicate;

import edu.asu.conceptpower.core.ReviewRequest;

/**
 * This class is a custom argument matcher to compare Predicate<ReviewRequest> classes
 * 
 * @author keerthivasan
 *
 */

public class PredicateReviewRequestMatcher implements ArgumentMatcher<Predicate<ReviewRequest>> {
 
    private Predicate<ReviewRequest> validRequest; 
    private ReviewRequest matcher;
    
    //reviewId is added in the constructor in order to build a matching object,
    //since Predicate implements match() method which requires an object to match
    
    public PredicateReviewRequestMatcher(Predicate<ReviewRequest> request, String reviewId) {
        this.validRequest = request;
        this.matcher = new ReviewRequest();
        matcher.setId(reviewId);
    }

    @Override
    public boolean matches(Predicate<ReviewRequest> toBeValidatedRequest) {
        //match will return a boolean after comparison
        return this.validRequest.match(matcher) == toBeValidatedRequest.match(matcher);
    }

}