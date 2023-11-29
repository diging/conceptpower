package edu.asu.conceptpower.web;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.app.core.IConceptListManager;
import edu.asu.conceptpower.core.ConceptList;

/**
 * This class handles requests for the application home page.
 */
@Controller
public class HomeController {
	/**
	 * This method simply selects the home view to render by returning its name.
	 */
    
    @Autowired
    private IConceptListManager conceptListManager;
    
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model,@ModelAttribute("conceptSearchBean")ConceptSearchBean conceptSearchBean) {
        List<ConceptList> conceptLists = conceptListManager.getAllConceptLists();
        List<String> conceptListNames = conceptLists.stream().map(ConceptList::getConceptListName).toList();
        conceptSearchBean.setConceptList(conceptListNames);
		return "home";
	}
	
	@RequestMapping(value = "/forbidden", method = RequestMethod.GET)
	public String forbidden(Model model) {
		return "forbidden";
	}

}
