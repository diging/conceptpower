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
    private CommentsManager commentsObj;
        
    
    @RequestMapping(value = "/auth/addComment", method = RequestMethod.POST )
    public @ResponseBody ReviewJsonResponse addNewComment( @ModelAttribute(value="reviewRequest") ReviewRequest reviewRequest, BindingResult result,Principal principal) {
  
        ReviewJsonResponse res = new ReviewJsonResponse();
        res.setResult(reviewRequest);
        commentsObj.addComment(reviewRequest.getConceptId(),reviewRequest.getComment(),CommentStatus.OPENED,principal.getName());

     
     return res;

    }
    
    
}
