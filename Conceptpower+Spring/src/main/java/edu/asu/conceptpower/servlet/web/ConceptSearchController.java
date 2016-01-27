package edu.asu.conceptpower.servlet.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.servlet.core.ConceptEntry;
import edu.asu.conceptpower.servlet.core.IConceptManager;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.validation.ConceptSearchValidator;
import edu.asu.conceptpower.servlet.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.servlet.wrapper.IConceptWrapperCreator;

/**
 * This class provides concept search methods
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptSearchController {

    @Autowired
    private IConceptManager conceptManager;

    @Autowired
    private IConceptWrapperCreator wrapperCreator;

    @Autowired
    private ConceptSearchValidator validator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    /**
     * This method is searches concepts a specific term and pos
     * 
     * @param req
     *            Holds the HTTP request information
     * @param model
     *            Generic model holder for servlet
     * @return Returns a string value to redirect user to concept search page
     */
    @RequestMapping(value = "/home/conceptsearch", method = RequestMethod.GET)
    public String search(HttpServletRequest req, ModelMap model,
            @Validated @ModelAttribute("conceptSearchBean") ConceptSearchBean conceptSearchBean, BindingResult results)
                    throws LuceneException {
        if (results.hasErrors()) {
            return "conceptsearch";
        }
        List<ConceptEntryWrapper> foundConcepts = null;
        ConceptEntry[] found = conceptManager.getConceptListEntriesForWord(conceptSearchBean.getWord(),
                conceptSearchBean.getPos().toString().toLowerCase().trim(), null);
        foundConcepts = wrapperCreator.createWrappers(found);
        conceptSearchBean.setFoundConcepts(foundConcepts);
        if (CollectionUtils.isEmpty(foundConcepts)) {
            results.rejectValue("foundConcepts", "no.searchResults");
        }

        return "conceptsearch";
    }

}
