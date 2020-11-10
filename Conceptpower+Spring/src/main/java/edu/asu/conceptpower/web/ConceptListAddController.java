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

import edu.asu.conceptpower.app.manager.IConceptListManager;
import edu.asu.conceptpower.app.validation.ConceptListAddValidator;

@Controller
public class ConceptListAddController {

    @Autowired
    private IConceptListManager conceptListService;

    @Autowired
    private ConceptListAddValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder validateBinder) {
        validateBinder.setValidator(validator);
    }

    /**
     * page
     * 
     * @return string value to redirect user to add concept list paage
     */
    @RequestMapping(value = "auth/conceptlist/addconceptlist")
    public String listAddView(@ModelAttribute("conceptListAddForm") ConceptListAddForm conceptListAddForm)
            throws Exception {
        return "layouts/concepts/addconceptlist";
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
            @Validated @ModelAttribute("conceptListAddForm") ConceptListAddForm conceptListAddForm,
            BindingResult result) {

        if (result.hasErrors()) {
            return "/layouts/concepts/addconceptlist";
        }
        conceptListService.addConceptList(conceptListAddForm.getListName(), conceptListAddForm.getDescription());
        return "redirect:/auth/conceptlist";
    }

}
