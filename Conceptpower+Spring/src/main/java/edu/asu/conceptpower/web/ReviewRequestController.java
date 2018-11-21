package edu.asu.conceptpower.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.conceptpower.app.core.impl.CommentsManager;

@Controller
public class ReviewRequestController {
 
 
    @Autowired
    private CommentsManager commentsObj;
    
    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public ResponseEntity<String> addNewComment(@RequestParam("comment") String comment, @RequestParam("conceptId") String conceptId, Principal principal) {
        // store new comment here
        
        String requester="";
        String resolver="";
        String status="";

        commentsObj.addComment(comment, conceptId, requester, resolver, status);
        
       return new ResponseEntity<String>(HttpStatus.OK);
    }
  
  
}
