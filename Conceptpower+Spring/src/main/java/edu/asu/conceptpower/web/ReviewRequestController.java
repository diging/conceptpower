package edu.asu.conceptpower.web;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.app.core.impl.RequestsManager;
import edu.asu.conceptpower.core.ReviewRequest;
import edu.asu.conceptpower.core.ReviewStatus;

@Controller
public class ReviewRequestController {
 
    @Autowired
    private RequestsManager requestsMgr;
           
    @RequestMapping(value = "/auth/request/add", method = RequestMethod.POST )
    public @ResponseBody ReviewRequest addNewReviewRequest( @ModelAttribute(value="reviewRequest") ReviewRequest reviewRequest,Principal principal) {
        reviewRequest.setStatus(ReviewStatus.OPENED);
        reviewRequest.setRequester(principal.getName());
        
        requestsMgr.addReviewRequest(reviewRequest);
        
        return reviewRequest;
    }
    
    @RequestMapping(value = "/auth/request/all", method = RequestMethod.GET )
    public @ResponseBody List<ReviewRequest> getAllReviewRequests(){
        return  requestsMgr.getAllReviews();
    }
    
    @RequestMapping(value = "/auth/request/resolve", method = RequestMethod.POST )
    public @ResponseBody ReviewRequest resolveRequest(@ModelAttribute(value="reviewRequest") ReviewRequest reviewRequest,Principal principal) {
        reviewRequest.setStatus(ReviewStatus.RESOLVED);
        reviewRequest.setResolver(principal.getName());
        
        ReviewRequest updatedRequest = requestsMgr.updateReview(reviewRequest);
        
        return updatedRequest;
    }
    
    @RequestMapping(value = "/auth/request/reopen", method = RequestMethod.POST )
    public @ResponseBody ReviewRequest reopenRequest(@ModelAttribute(value="reviewRequest") ReviewRequest reviewRequest,Principal principal) {
        reviewRequest.setStatus(ReviewStatus.OPENED);
        
        ReviewRequest updatedRequest = requestsMgr.reopenReview(reviewRequest);
        
        return updatedRequest;
    }
}
