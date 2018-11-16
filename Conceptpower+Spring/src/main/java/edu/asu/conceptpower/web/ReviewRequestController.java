package edu.asu.conceptpower.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import edu.asu.conceptpower.app.bean.ConceptsMergeBean;
import edu.asu.conceptpower.core.ReviewRequest;

@Controller
public class ReviewRequestController {

    @RequestMapping(value = "/home/comment", method = RequestMethod.GET)
    public ModelAndView conceptReview() {
       return new ModelAndView("commenstPosted", "model", new ReviewRequest());
    }
 
  @RequestMapping(value = "/home/addComment", method = RequestMethod.POST)
 // @ResponseStatus(value = HttpStatus.OK)
    public String addEmployee(@ModelAttribute("SpringWeb")ReviewRequest comment, 
    ModelMap model,@ModelAttribute("conceptSearchBean") ConceptSearchBean conceptSearchBean) {
       model.addAttribute("comment", comment.getComment());
       model.addAttribute("conceptLink", comment.getConceptLink());
       model.addAttribute("requester", comment.getRequester());
       model.addAttribute("resolver", comment.getResolver());
       model.addAttribute("resolver", comment.getResolver());
       
       return "conceptsearch";
    }
}
