package edu.asu.conceptpower.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.app.core.impl.CommentsManager;
import edu.asu.conceptpower.core.CommentStatus;
import edu.asu.conceptpower.core.ReviewJsonResponse;
import edu.asu.conceptpower.core.ReviewRequest;

@Controller
public class ReviewRequestController {
 
    @Autowired
    private CommentsManager commentsMgr;
           
    @RequestMapping(value = "/auth/addComment", method = RequestMethod.POST )
    public @ResponseBody ReviewJsonResponse addNewComment( @ModelAttribute(value="reviewRequest") ReviewRequest reviewRequest, BindingResult result,Principal principal) {
  
        reviewRequest.setStatus(CommentStatus.OPENED);
        reviewRequest.setRequester(principal.getName());
        
        ReviewJsonResponse res = new ReviewJsonResponse();
        res.setResult(reviewRequest);
        commentsMgr.addReviewRequest(reviewRequest);
        
        return res;
    }
}
