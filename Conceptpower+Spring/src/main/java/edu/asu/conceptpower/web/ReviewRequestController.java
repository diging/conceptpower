package edu.asu.conceptpower.web;

import java.security.Principal;
import java.time.LocalDateTime;
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
        reviewRequest.setCreatedAt(LocalDateTime.now());
        requestsMgr.addReviewRequest(reviewRequest);
        
        return reviewRequest;
    }
    
    @RequestMapping(value = "/auth/request/resolve", method = RequestMethod.POST )
    public @ResponseBody ResponseEntity<ReviewRequest> resolveRequest(@RequestBody ReviewRequest reviewRequest,Principal principal) {
        if(reviewRequest.getStatus() != ReviewStatus.RESOLVED || reviewRequest.getComments().size() != 1) {
            return new ResponseEntity<ReviewRequest>(HttpStatus.BAD_REQUEST);
        }
        reviewRequest.setResolver(principal.getName());
        reviewRequest.getComments().get(0).setCreatedBy(principal.getName());
        reviewRequest.getComments().get(0).setCreatedAt(LocalDateTime.now());
        
        return new ResponseEntity<ReviewRequest>(requestsMgr.updateReview(reviewRequest), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/auth/request/reopen", method = RequestMethod.POST )
    public @ResponseBody ResponseEntity<ReviewRequest> reopenRequest(@RequestBody ReviewRequest reviewRequest,Principal principal) {
        if(reviewRequest.getStatus() != ReviewStatus.OPENED || reviewRequest.getComments().size() != 1) {
            return new ResponseEntity<ReviewRequest>(HttpStatus.BAD_REQUEST);
        }
        reviewRequest.setRequester(principal.getName());
        reviewRequest.getComments().get(0).setCreatedBy(principal.getName());
        reviewRequest.getComments().get(0).setCreatedAt(LocalDateTime.now());
        
        return new ResponseEntity<ReviewRequest>(requestsMgr.updateReview(reviewRequest), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/auth/request/{conceptId}/all", method = RequestMethod.GET)
    public @ResponseBody List<ReviewRequest> getAllReviews(@PathVariable String conceptId, Principal principal){
        return requestsMgr.getAllReviews(conceptId);  
    }
}
