package edu.asu.conceptpower.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.app.core.impl.RequestsManager;
import edu.asu.conceptpower.core.ReviewStatus;
import edu.asu.conceptpower.core.RequestJsonResponse;
import edu.asu.conceptpower.core.ReviewRequest;

@Controller
public class ReviewRequestController {
 
    @Autowired
    private RequestsManager requestsMgr;
           
    @RequestMapping(value = "/auth/addRequest", method = RequestMethod.POST )
    public @ResponseBody RequestJsonResponse addNewReviewRequest( @ModelAttribute(value="reviewRequest") ReviewRequest reviewRequest, BindingResult result,Principal principal) {
        reviewRequest.setStatus(ReviewStatus.OPENED);
        reviewRequest.setRequester(principal.getName());
        
        RequestJsonResponse res = new RequestJsonResponse();
        res.setResult(reviewRequest);
        requestsMgr.addReviewRequest(reviewRequest);

        return res;
    }
}
