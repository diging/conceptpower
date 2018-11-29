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
    
    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public ResponseEntity<String> addNewComment(@RequestParam("comment") String comment, @RequestParam("wordNetId") String wordNetId, Principal principal) {
        // store new comment here
        
      //  String requester="";
        String resolver="";
        String status="Open";
        String conceptId="";
        System.out.println("###wordNetId--"+wordNetId);

      //  wordNetId="WID-05744010-N-02-rabbit_test";
        
        wordNetId = wordNetId.substring(wordNetId.indexOf(">") + 1);
        wordNetId = wordNetId.substring(0, wordNetId.indexOf("<"));
        
        System.out.println("$$$wordNetId--"+wordNetId);
        
        String requester = usrManager.findUser("admin").getUsername();
        System.out.println("###requester"+requester);

        try {
            conceptId = conceptMgr.getWordnetConceptEntry(wordNetId).getId();
        } catch (LuceneException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println("###conceptId"+conceptId);
        commentsObj.addComment(comment, conceptId, requester, resolver, status);
        
        
       return new ResponseEntity<String>(HttpStatus.OK);
    }
  
  
}
