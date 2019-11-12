package edu.asu.conceptpower.web;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.app.core.impl.RequestsManager;
import edu.asu.conceptpower.core.ReviewRequest;

@Controller
public class ReviewRequestController {
 
    @Autowired
    private RequestsManager requestsMgr;
           
    @RequestMapping(value = "/auth/request/add", method = RequestMethod.POST )
    public @ResponseBody ReviewRequest addNewReviewRequest( @ModelAttribute(value="reviewRequest") ReviewRequest reviewRequest,Principal principal) {
        reviewRequest.setRequester(principal.getName());
        reviewRequest.setCreatedAt(LocalDateTime.now());
        requestsMgr.addReviewRequest(reviewRequest);
        
        return reviewRequest;
    }
    
    @RequestMapping(value = "/auth/request/resolve", method = RequestMethod.POST )
    public @ResponseBody ReviewRequest resolveRequest(@RequestBody ReviewRequest reviewRequest,Principal principal) {
        reviewRequest.setResolver(principal.getName());
        reviewRequest.getComments().get(0).setCreatedBy(principal.getName());
        reviewRequest.getComments().get(0).setCreatedAt(LocalDateTime.now());
        
        ReviewRequest updatedRequest = requestsMgr.updateReview(reviewRequest);
        
        return updatedRequest;
    }
    
    @RequestMapping(value = "/auth/request/reopen", method = RequestMethod.POST )
    public @ResponseBody ReviewRequest reopenRequest(@RequestBody ReviewRequest reviewRequest,Principal principal) {
        reviewRequest.getComments().get(0).setCreatedBy(principal.getName());
        reviewRequest.getComments().get(0).setCreatedAt(LocalDateTime.now());
        reviewRequest.setRequester(principal.getName());
        
        ReviewRequest updatedRequest = requestsMgr.updateReview(reviewRequest);
        
        return updatedRequest;
    }
    
    @RequestMapping(value = "/auth/request/{conceptId}/all", method = RequestMethod.GET)
    public @ResponseBody List<ReviewRequest> getAllReviews(@PathVariable String conceptId, Principal principal){
        List<ReviewRequest> listOfReviews = requestsMgr.getAllReviews(conceptId);  
        return listOfReviews;
    }
}
