package edu.asu.conceptpower.web;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.core.ReviewRequest;

@Controller
public class ReviewRequestController {

    @RequestMapping(value = "/addComment", method = RequestMethod.GET)
    public String showCommentsPosted(Model model) {
        model.addAttribute("reviewrequest", new ReviewRequest());

        return "conceptsearch";
    }
 
 
  @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public String addComment(@Valid @ModelAttribute("reviewrequest")ReviewRequest reviewrequest, 
            BindingResult result,ModelMap model,@ModelAttribute("conceptSearchBean") ConceptSearchBean conceptSearchBean) {
      model.addAttribute("reviewrequest",reviewrequest);
      model.addAttribute("comment", reviewrequest.getComment());
       model.addAttribute("conceptLink", reviewrequest.getConceptLink());
       model.addAttribute("requester", reviewrequest.getRequester());
       model.addAttribute("resolver", reviewrequest.getResolver());
       model.addAttribute("resolver", reviewrequest.getResolver());
       //,@ModelAttribute("conceptSearchBean") ConceptSearchBean conceptSearchBean
       //modelAttribute="reviewRequest"
       return "conceptsearch";
    }
}
