package edu.asu.conceptpower.web;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This class handles requests for the application home page.
 */
@Controller
public class HomeController {
    
    @Autowired
    private Environment env;
	/**
	 * This method simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model,@ModelAttribute("conceptSearchBean")ConceptSearchBean conceptSearchBean) {
	    model.addAttribute("build",env.getProperty("buildNum"));
	    model.addAttribute("pullrequest", env.getProperty("pullReq"));
		return "home";
	}
	
	@RequestMapping(value = "/forbidden", method = RequestMethod.GET)
	public String forbidden(Model model) {
		return "forbidden";
	}

}
