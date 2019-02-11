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
        
        StringBuffer resolver = new StringBuffer();
        StringBuffer conceptId = new StringBuffer("");
        StringBuffer sb = new StringBuffer();
        String substring;
        Boolean review_flag = true;
        boolean wordNetId_Present =true;

        String requester = principal.getName();

        if(wordNetId != null && wordNetId.length()>0)
        {
            sb = new StringBuffer(wordNetId);
            
            if(wordNetId.equalsIgnoreCase("<font size=\"2\"></font>")) // wordNetId always come in this format even when no value is present.
            {
                sb = new StringBuffer(wordId);  //since wordNetId is blank,will use wordId instead.
                wordNetId_Present = false;
            }
        }
        
        //wordNetId and wordId has value stored in <font>wordNetId_Value<font> / <font>wordId_Value<font>  format.So,extracting the values between font tags.
        if((sb.indexOf(">")!=-1) && (sb.indexOf("<")!= -1)) {
            substring = sb.substring(sb.indexOf(">") + 1);
            sb.setLength(0);
            sb.append(substring);

            substring = sb.substring(0, sb.indexOf("<"));
            sb.setLength(0);
            sb.append(substring);            
        }

       //storing the extracted wordNetId in conceptId
        if(wordNetId_Present && wordNetId != null && wordNetId.length()>0 && sb != null && sb.length() != 0) {
                try {
                    conceptId.append(conceptMgr.getWordnetConceptEntry(sb.toString()).getId());
                } catch (LuceneException e) {
                    logger.warn(e.getMessage());
                }
             wordNetId = conceptId.toString();

        }else
        {
            wordNetId=null; //otherwise wordNetId will be having value as "<font size="2"></font>". It is therefore made null to get rid of this value.
            wordNetId_Present = false;
        }
        
        //Will only enter this if loop if wordNetId is missing.
        if(!wordNetId_Present && wordId != null && wordId.length()>0 && sb != null && sb.length() != 0)
        {
            wordId = sb.toString();
        }
        
        commentsObj.addComment(wordId,comment,wordNetId , requester, resolver.toString(), Status.OPENED , review_flag);
        
      return "redirect:/home/conceptsearch?word="+wordValue+"&pos="+posValue;
    }
}
