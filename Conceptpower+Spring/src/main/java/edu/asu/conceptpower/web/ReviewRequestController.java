package edu.asu.conceptpower.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.app.core.impl.RequestsManager;
import edu.asu.conceptpower.app.db.repository.ReviewRequestRepository;
import edu.asu.conceptpower.core.ReviewRequest;
import edu.asu.conceptpower.core.ReviewStatus;


@Controller
public class ReviewRequestController {
 
    @Autowired
    private RequestsManager requestsMgr;
    
    @Autowired
    private ReviewRequestRepository reviewRequestRepository;
           
    //@RequestMapping(value = "/auth/request/add", method = RequestMethod.POST )
    public @ResponseBody ReviewRequest addNewReviewRequest( @ModelAttribute(value="reviewRequest") ReviewRequest reviewRequest,Principal principal) {
  
        reviewRequest.setStatus(ReviewStatus.OPENED);
        reviewRequest.setRequester(principal.getName());
        
        requestsMgr.addReviewRequest(reviewRequest);
        
        return reviewRequest;
    }
    
    
    @RequestMapping(value = "/auth/request/add", method = RequestMethod.POST )
    public @ResponseBody edu.asu.conceptpower.app.model.ReviewRequest createNewReviewRequest( @ModelAttribute(value="reviewRequest") edu.asu.conceptpower.app.model.ReviewRequest reviewRequest,Principal principal) {
  
        reviewRequest.setStatus(edu.asu.conceptpower.app.model.ReviewStatus.OPENED);
        reviewRequest.setRequester(principal.getName());
        
        //requestsMgr.addReviewRequest(reviewRequest);
        reviewRequestRepository.save(reviewRequest);
        
        return reviewRequest;
    }
}