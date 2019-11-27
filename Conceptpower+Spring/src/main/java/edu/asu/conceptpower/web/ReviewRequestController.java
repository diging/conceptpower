package edu.asu.conceptpower.web;

import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
        reviewRequest.setRequester(principal.getName());
        reviewRequest.setCreatedAt(OffsetDateTime.now());
        requestsMgr.addReviewRequest(reviewRequest);
        
        return reviewRequest;
    }
    
    @RequestMapping(value = "/auth/request/resolve", method = RequestMethod.POST )
    public @ResponseBody ReviewRequest resolveRequest(@RequestBody ReviewRequest reviewRequest,Principal principal) {
        return requestsMgr.updateReview(reviewRequest.getId(), ReviewStatus.RESOLVED, reviewRequest.getComments(), principal.getName());
    }
    
    @RequestMapping(value = "/auth/request/reopen", method = RequestMethod.POST )
    public @ResponseBody ReviewRequest reopenRequest(@RequestBody ReviewRequest reviewRequest,Principal principal) {
        return requestsMgr.updateReview(reviewRequest.getId(), ReviewStatus.OPENED, reviewRequest.getComments(), principal.getName());
    }
    
    @RequestMapping(value = "/auth/request/{conceptId}/all", method = RequestMethod.GET)
    public @ResponseBody List<ReviewRequest> getAllReviews(@PathVariable String conceptId, Principal principal){
        return requestsMgr.getAllReviews(conceptId);  
    }
}
