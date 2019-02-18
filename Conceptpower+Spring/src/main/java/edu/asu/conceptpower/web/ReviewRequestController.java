package edu.asu.conceptpower.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
        
    @PreAuthorize("isAuthenticated()")   
    @RequestMapping(value = "/auth/addComment", method = RequestMethod.POST)
    public String addNewComment(@Validated @ModelAttribute("conceptSearchBean") ConceptSearchBean conceptSearchBean,
            @RequestParam("comment") String comment, Principal principal , 
            @RequestParam("conceptId") String conceptId, @RequestParam("wordValue") String wordValue, @RequestParam("posValue") String posValue,@RequestParam("url") String url) {
   
        commentsObj.addComment(conceptId,comment,CommentStatus.OPENED);
        
      return "redirect:/home/conceptsearch?"+url.trim();
    }
}
