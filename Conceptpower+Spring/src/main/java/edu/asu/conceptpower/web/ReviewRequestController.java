package edu.asu.conceptpower.web;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReviewRequestController {
 
 
    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public ResponseEntity<String> addNewComment(@RequestParam("comment") String comment, @RequestParam("conceptId") String conceptId, Principal principal) {
        // store new comment here
       return new ResponseEntity<String>(HttpStatus.OK);
    }
  
  
}
