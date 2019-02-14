package edu.asu.conceptpower.web;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.conceptpower.app.core.impl.CommentsManager;
import edu.asu.conceptpower.app.core.impl.ConceptManager;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.core.ReviewRequest.Status;

@Controller
public class ReviewRequestController {
 
    private static final Logger logger = LoggerFactory.getLogger(ReviewRequestController.class);

    @Autowired
    private CommentsManager commentsObj;
    
    @Autowired
    private ConceptManager conceptMgr;
    
    @PreAuthorize("isAuthenticated()")   
    @RequestMapping(value = "/auth/addComment", method = RequestMethod.POST)
    public String addNewComment(@Validated @ModelAttribute("conceptSearchBean") ConceptSearchBean conceptSearchBean,
            @RequestParam("comment") String comment, @RequestParam("wordNetId") String wordNetId, Principal principal , 
            @RequestParam("wordId") String wordId, @RequestParam("wordValue") String wordValue, @RequestParam("posValue") String posValue) {
   
        
        commentsObj.addComment(wordId,comment,wordNetId, Status.OPENED);
        
      return "redirect:/home/conceptsearch?word="+wordValue+"&pos="+posValue;
    }
}
