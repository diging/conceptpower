package edu.asu.conceptpower.web;

import java.security.Principal;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
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
        
    
    @RequestMapping(value = "/auth/addComment", method = RequestMethod.POST , consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, 
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseBody
    public void addNewComment(/*@Validated @ModelAttribute("conceptSearchBean") ConceptSearchBean conceptSearchBean,
            @RequestParam("comment") String comment, Principal principal , 
            @RequestParam("conceptId") String conceptId,*/ ReviewJsonResponse response,Principal principal) {
   
        
       // ReviewJsonResponse responseView = new ReviewJsonResponse();
//        response.setComment(comment);
//        response.setConceptId(conceptId);
//        response.setRequestor(principal.getName());
        System.out.println("@@"+response.getComment());
        
       /* if(result.hasErrors()){
            
            //Get error message
            Map<String, String> errors = result.getFieldErrors().stream()
                  .collect(
                        Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)
                    );
            
            responseView.setValidated(false);
            responseView.setErrorMessages(errors);
         }else{
            
             commentsObj.addComment(response.getConceptId(),response.getComment(),CommentStatus.OPENED,principal.getName());

             responseView.setValidated(true);
             responseView.setComment(response.getComment());
             responseView.setConceptId(response.getConceptId());

         }*/
        commentsObj.addComment(response.getConceptId(),response.getComment(),CommentStatus.OPENED,principal.getName());

        System.out.println("##"+response.getComment());

     // return responseView;
    }
    
    
}
