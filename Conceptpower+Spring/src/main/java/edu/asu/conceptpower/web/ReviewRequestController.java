package edu.asu.conceptpower.web;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.conceptpower.app.core.impl.CommentsManager;
import edu.asu.conceptpower.app.core.impl.ConceptManager;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.users.UsersManager;

@Controller
public class ReviewRequestController {
 
 
    @Autowired
    private CommentsManager commentsObj;
    
    @Autowired
    private UsersManager usrManager;
    
    @Autowired
    private ConceptManager conceptMgr;
    
    private static final Logger logger = LoggerFactory.getLogger(ReviewRequestController.class);

    
    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public ResponseEntity<String> addNewComment(@RequestParam("comment") String comment, @RequestParam("wordNetId") String wordNetId, Principal principal) {
        
        String resolver="";
        String status="Open";
        String conceptId="";
        
       //wordNetId has value stored in <font>wordNetId_Value<font> format.So,extracting the values between font tags.
        wordNetId = wordNetId.substring(wordNetId.indexOf(">") + 1);
        wordNetId = wordNetId.substring(0, wordNetId.indexOf("<"));
        
        String requester = principal.getName();
       

        try {
            conceptId = conceptMgr.getWordnetConceptEntry(wordNetId).getId();
        } catch (LuceneException e) {
            logger.error("Lucene exception", e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
       
        commentsObj.addComment(comment, conceptId, requester, resolver, status);
        
        
       return new ResponseEntity<String>(HttpStatus.OK);
    }
  
}
