package edu.asu.conceptpower.web;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ConceptDetailsController {

    @RequestMapping(value="/conceptdetails", method=RequestMethod.POST)
    public String getConceptDetails(@RequestBody Map<String, String> details, Model model) {
        model.addAttribute("details", details);
        return "layouts/modals/ConceptDetails";
    }

}
