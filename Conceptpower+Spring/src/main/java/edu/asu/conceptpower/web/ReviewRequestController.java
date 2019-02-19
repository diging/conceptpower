package edu.asu.conceptpower.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.conceptpower.app.core.impl.CommentsManager;

import edu.asu.conceptpower.core.CommentStatus;

@Controller
public class ReviewRequestController {
 

    @Autowired
    private CommentsManager commentsObj;
        
    
    @RequestMapping(value = "/auth/addComment", method = RequestMethod.POST)
    public String addNewComment(@Validated @ModelAttribute("conceptSearchBean") ConceptSearchBean conceptSearchBean,
            @RequestParam("comment") String comment, Principal principal , 
            @RequestParam("conceptId") String conceptId,@RequestParam("url") String url) {
   
        commentsObj.addComment(conceptId,comment,CommentStatus.OPENED,principal.getName());
        
      return "redirect:/home/conceptsearch?"+url.trim();
    }
}
