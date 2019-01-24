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
import edu.asu.conceptpower.core.ReviewRequest.Status;

@Controller
public class ReviewRequestController {
 
    private static final Logger logger = LoggerFactory.getLogger(ReviewRequestController.class);

    @Autowired
    private CommentsManager commentsObj;
    
    @Autowired
    private ConceptManager conceptMgr;
    
       
    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public ResponseEntity<String> addNewComment(@RequestParam("comment") String comment, @RequestParam("wordNetId") String wordNetId, Principal principal) {
        
        StringBuffer resolver = new StringBuffer();
        StringBuffer conceptId = new StringBuffer();
        StringBuffer wordNet_Id = new StringBuffer(wordNetId);
        String substring;

        //wordNetId has value stored in <font>wordNetId_Value<font> format.So,extracting the values between font tags.
        if((wordNet_Id.indexOf(">")!=-1) && (wordNet_Id.indexOf("<")!= -1)) {
            substring = wordNet_Id.substring(wordNet_Id.indexOf(">") + 1);
            wordNet_Id.setLength(0);
            wordNet_Id.append(substring);

            substring = wordNet_Id.substring(0, wordNet_Id.indexOf("<"));
            wordNet_Id.setLength(0);
            wordNet_Id.append(substring);            
        }
        String requester = principal.getName();
       
        try {
            if(wordNetId != null) {
                conceptId.append(conceptMgr.getWordnetConceptEntry(wordNet_Id.toString()).getId());
            }
        } catch (LuceneException e) {
            logger.error("Lucene exception", e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
       
        commentsObj.addComment(comment, conceptId.toString(), requester, resolver.toString(), Status.OPENED);
        
        
       return new ResponseEntity<String>(HttpStatus.OK);
    }
  
}
