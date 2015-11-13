package edu.asu.conceptpower.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.conceptpower.core.IConceptListManager;
import edu.asu.conceptpower.validation.ConceptListAddValidator;

@Controller
public class ConceptListAddController {

	@Autowired
	private IConceptListManager conceptListManager;
	
	@Autowired
	private ConceptListAddValidator validator;
	
	@InitBinder
	protected void initBinder (WebDataBinder validateBinder)
	{
		validateBinder.setValidator(validator);
	}

	/**
	 * This method provides a string value to redirect user to add concept list
	 * page
	 * 
	 * @return string value to redirect user to add concept list page
	 */
	@RequestMapping(value = "auth/conceptlist/addconceptlist")
	public ModelMap listAddView(@ModelAttribute("conceptListAddForm") ConceptListAddForm conceptListAddForm) throws Exception{
		return new ModelMap("/auth/conceptlist/addconceptlist");
	}

	/**
	 * This method creates and stores a new concept list
	 * 
	 * @param req
	 *            Holds HTTP request information
	 * @param model
	 *            A generic model holder for Servlet
	 * @return String value to redirect user to a concept list page
	 */
	@RequestMapping(value = "auth/concepts/createconceptlist")
	public String createConceptList(HttpServletRequest req, ModelMap model,
			@Validated @ModelAttribute("conceptListAddForm") ConceptListAddForm conceptListAddForm,BindingResult result) {
		
		if(result.hasErrors()){
			return "/auth/conceptlist/addconceptlist";
		}
		conceptListManager.addConceptList(conceptListAddForm.getListName(), conceptListAddForm.getDescription());
		return "redirect:/auth/conceptlist";
	}

}
