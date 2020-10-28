package edu.asu.conceptpower.web;

import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.app.manager.RequestsManager;
import edu.asu.conceptpower.app.model.Comment;
import edu.asu.conceptpower.app.model.ReviewRequest;
import edu.asu.conceptpower.app.model.ReviewStatus;

@Controller
public class ReviewRequestController {
 
    @Autowired
    private RequestsManager requestsMgr;
           
    @RequestMapping(value = "/auth/request/add", method = RequestMethod.POST )
    public @ResponseBody ReviewRequest addNewReviewRequest( @ModelAttribute(value="reviewRequest") ReviewRequest reviewRequest,Principal principal) {
        reviewRequest.setRequester(principal.getName());
        reviewRequest.setCreatedAt(OffsetDateTime.now());
        requestsMgr.addReviewRequest(reviewRequest);
        
        return reviewRequest;
    }
    
    @RequestMapping(value = "/auth/request/resolve/{reviewId}", method = RequestMethod.POST )
    public @ResponseBody ReviewRequest resolveRequest(@PathVariable String reviewId,@RequestBody Comment comment,Principal principal) {
        return requestsMgr.updateReview(reviewId, ReviewStatus.RESOLVED, comment, OffsetDateTime.now(), principal.getName());
    }
    
    @RequestMapping(value = "/auth/request/reopen/{reviewId}", method = RequestMethod.POST )
    public @ResponseBody ReviewRequest reopenRequest(@PathVariable String reviewId,@RequestBody Comment comment,Principal principal) {
        return requestsMgr.updateReview(reviewId, ReviewStatus.OPENED, comment, OffsetDateTime.now(), principal.getName());
    }
    
    @RequestMapping(value = "/auth/request/{conceptId}/all", method = RequestMethod.GET)
    public @ResponseBody List<ReviewRequest> getAllReviews(@PathVariable String conceptId, Principal principal){
        return requestsMgr.getAllReviews(conceptId);  
    }
}
