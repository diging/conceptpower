package edu.asu.conceptpower.web;

import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.app.manager.IConceptDBManager;
import edu.asu.conceptpower.app.manager.impl.RequestsManager;
import edu.asu.conceptpower.app.model.Comment;
import edu.asu.conceptpower.app.model.ReviewRequest;
import edu.asu.conceptpower.app.model.ReviewStatus;

/**
 * This controller helps in managing review request related operations
 * 
 * @author Keerthivasan Krishnamurthy
 * 
 */

@Controller
public class ReviewRequestController {
 
    @Autowired
    private RequestsManager requestsMgr;
    
    @Autowired
    private IConceptDBManager conceptDBManager;
           
    @PostMapping(value = "/auth/request/add")
    public @ResponseBody ReviewRequest addNewReviewRequest( @ModelAttribute(value="reviewRequest") ReviewRequest reviewRequest,Principal principal) {
        reviewRequest.setRequester(principal.getName());
        reviewRequest.setCreatedAt(OffsetDateTime.now());
        requestsMgr.addReviewRequest(reviewRequest);
        
        return reviewRequest;
    }
    
    @PostMapping(value = "/auth/request/resolve/{reviewId}")
    public @ResponseBody ReviewRequest resolveRequest(@PathVariable String reviewId,@RequestBody Comment comment,Principal principal) {
        return requestsMgr.updateReview(reviewId, ReviewStatus.RESOLVED, comment, OffsetDateTime.now(), principal.getName());
    }
    
    @PostMapping(value = "/auth/request/reopen/{reviewId}")
    public @ResponseBody ReviewRequest reopenRequest(@PathVariable String reviewId,@RequestBody Comment comment,Principal principal) {
        return requestsMgr.updateReview(reviewId, ReviewStatus.OPENED, comment, OffsetDateTime.now(), principal.getName());
    }
    
    @GetMapping(value = "/auth/request/{conceptId}/all")
    public @ResponseBody List<ReviewRequest> getAllReviews(@PathVariable String conceptId, Principal principal){
        return requestsMgr.getAllReviews(conceptId);  
    }
    
    @GetMapping(value = "/auth/request/validate/{conceptId}")
    public @ResponseBody boolean validateReviewRequest(@PathVariable String conceptId, Principal principal) {
        return conceptDBManager.getEntry(conceptId) != null;
    }
}
