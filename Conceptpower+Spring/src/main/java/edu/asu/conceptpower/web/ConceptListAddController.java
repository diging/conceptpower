package edu.asu.conceptpower.web;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

import edu.asu.conceptpower.app.core.IConceptListManager;
import edu.asu.conceptpower.app.db.repository.ConceptListsRepository;
import edu.asu.conceptpower.app.validation.ConceptListAddValidator;
import edu.asu.conceptpower.core.ConceptList;

@Controller
public class ConceptListAddController {

    @Autowired
    private IConceptListManager conceptListManager;

    @Autowired
    private ConceptListAddValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder validateBinder) {
        validateBinder.setValidator(validator);
    }
    
    @Autowired
    private ConceptListsRepository conceptListsRepository;

    protected final String LIST_PREFIX = "LIST";
    
    /**
     * page
     * 
     * @return string value to redirect user to add concept list paage
     */
    @RequestMapping(value = "auth/conceptlist/addconceptlist")
    public String listAddView(@ModelAttribute("conceptListAddForm") ConceptListAddForm conceptListAddForm)
            throws Exception {
        return "/auth/conceptlist/addconceptlist";
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
            return "/auth/conceptlist/addconceptlist";
        }
        conceptListManager.addConceptList(conceptListAddForm.getListName(), conceptListAddForm.getDescription());
        return "redirect:/auth/conceptlist";
    }
    
    /**
     * This method creates and stores a new concept list in mysql database.
     * 
     * @param req
     *            Holds HTTP request information
     * @param model
     *            A generic model holder for Servlet
     * @return String value to redirect user to a concept list page
     */
    @RequestMapping(value = "auth/concepts/createconceptlist")
    public String addNewConceptList(HttpServletRequest req, ModelMap model,
            @Validated @ModelAttribute("conceptListAddForm") ConceptListAddForm conceptListAddForm,
            BindingResult result) {

        if (result.hasErrors()) {
            return "/auth/conceptlist/addconceptlist";
        }
        edu.asu.conceptpower.app.model.ConceptList newConceptList = new edu.asu.conceptpower.app.model.ConceptList();
        newConceptList.setConceptListName(conceptListAddForm.getListName());
        newConceptList.setDescription(conceptListAddForm.getDescription());
        newConceptList.setId(generateId(LIST_PREFIX));
        
        conceptListsRepository.save(newConceptList);
        return "redirect:/auth/conceptlist";
    }
    
    /*
     * =================================================================
     * Protected/Private methods
     * =================================================================
     */
    protected String generateId(String prefix) {
        String id = prefix + UUID.randomUUID().toString();

        while (true) {
            Optional<edu.asu.conceptpower.app.model.ConceptList> example = null;
      
            // if there doesn't exist an object with this id return id
            example = conceptListsRepository.findById(Integer.parseInt(id));
            //List<Object> results = client.queryByExample(example);
            if (example == null)
                return id;

            // try other id
            id = prefix + UUID.randomUUID().toString();
        }
    }

}
