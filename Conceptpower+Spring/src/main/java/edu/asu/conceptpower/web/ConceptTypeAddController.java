package edu.asu.conceptpower.web;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.app.core.IConceptTypeManger;
import edu.asu.conceptpower.app.validation.ConceptTypeAddValidator;
import edu.asu.conceptpower.core.ConceptType;
import jakarta.servlet.http.HttpServletRequest;

/**
 * This class provides required methods for concept type creation
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptTypeAddController {

    @Autowired
    private IConceptTypeManger conceptTypesManager;

    @Autowired
    private ConceptTypeAddValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    /**
     * This method provides types information required for concept type creation
     * 
     * @param model
     *            A generic model holder for Servlet
     * @return String value to redirect use to concept type add page
     */
    @RequestMapping(value = "auth/concepttype/addtype")
    public String prepareTypeAddView(ModelMap model,
            @ModelAttribute("conceptTypeAddForm") ConceptTypeAddForm conceptTypeAddForm) {
        ConceptType[] allTypes = conceptTypesManager.getAllTypes();

        Map<String, String> types = new LinkedHashMap<String, String>();
        for (ConceptType conceptType : allTypes) {
            types.put(conceptType.getTypeId(), conceptType.getTypeName());
        }
        conceptTypeAddForm.setTypes(types);
        return "layouts/concepts/addconcepttype";
    }

    /**
     * This method stores the newly created concept type
     * 
     * @param req
     *            Holds HTTP request information
     * @param principal
     *            Holds information of a logged in user
     * @return String value to redirect user to concept type list page
     */
    @RequestMapping(value = "auth/concepts/createconcepttype", method = RequestMethod.POST)
    public String createConceptType(HttpServletRequest req, Principal principal,
            @Validated @ModelAttribute("conceptTypeAddForm") ConceptTypeAddForm conceptTypeAddForm,
            BindingResult results) {

        if (results.hasErrors()) {
            return "layouts/concepts/addconcepttype";
        }
        ConceptType type = new ConceptType();
        type.setTypeName(conceptTypeAddForm.getTypeName());
        type.setDescription(conceptTypeAddForm.getTypeDescription());
        type.setMatches(conceptTypeAddForm.getMatches());
        type.setSupertypeId(conceptTypeAddForm.getSuperType());
        type.setCreatorId(principal.getName());

        conceptTypesManager.addConceptType(type);
        return "redirect:/auth/concepttype";
    }
}
