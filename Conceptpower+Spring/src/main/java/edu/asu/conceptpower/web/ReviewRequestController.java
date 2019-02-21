package edu.asu.conceptpower.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.app.core.impl.CommentsManager;

import edu.asu.conceptpower.core.CommentStatus;
import edu.asu.conceptpower.core.ReviewJsonResponse;

@Controller
public class ReviewRequestController {
 

    @Autowired
    private CommentsManager commentsObj;
        
    
    @RequestMapping(value = "/auth/addComment", method = RequestMethod.POST , produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ReviewJsonResponse addNewComment(@Validated @ModelAttribute("conceptSearchBean") ConceptSearchBean conceptSearchBean,
            @RequestParam("comment") String comment, Principal principal , 
            @RequestParam("conceptId") String conceptId) {
   
        
        ReviewJsonResponse response = new ReviewJsonResponse();
        response.setComment(comment);
        response.setConceptId(conceptId);
        response.setRequestor(principal.getName());
        
        commentsObj.addComment(conceptId,comment,CommentStatus.OPENED,principal.getName());
   
      return response;
    }
    
    @RequestMapping(value = "/auth/addComment", method = RequestMethod.GET )
    public String show() {
        return "conceptSearchBean";
    }
}
