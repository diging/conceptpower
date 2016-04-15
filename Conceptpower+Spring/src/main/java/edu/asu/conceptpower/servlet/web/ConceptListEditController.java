package edu.asu.conceptpower.servlet.web;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.servlet.core.ConceptEntry;
import edu.asu.conceptpower.servlet.core.ConceptList;
import edu.asu.conceptpower.servlet.core.IConceptListManager;
import edu.asu.conceptpower.servlet.core.IConceptManager;
import edu.asu.conceptpower.servlet.core.IIndexService;
import edu.asu.conceptpower.servlet.exceptions.IndexerRunningException;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.validation.ConceptListAddValidator;

/**
 * This class provides all the methods required for editing a concept list
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptListEditController {

    @Autowired
    private IConceptManager conceptManager;

    @Autowired
    private IConceptListManager conceptListManager;

    @Autowired
    private ConceptListAddValidator validator;
    
    @Autowired
    private IIndexService indexService;
    
    @Value("#{messages['INDEXER_RUNNING']}")
    private String indexerRunning;
    
    @Value("#{messages['INDEXERSTATUS']}")
    private String indexerStatus;

    @InitBinder
    protected void initBinder(WebDataBinder validateBinder) {
        validateBinder.setValidator(validator);
    }

    /**
     * This method provides list name and description of a list to be edited
     * 
     * @param listName
     *            Represents list which has to be edited
     * @param model
     *            Generic model holder for servlet
     * @return Returns a string value to redirect user to edit list page
     */
    @RequestMapping(value = "auth/conceptlist/editlist/{listname}", method = RequestMethod.GET)
    public String prepareEditList(@PathVariable("listname") String listName, ModelMap model,
            @ModelAttribute("conceptListAddForm") ConceptListAddForm conceptListAddForm) {

        ConceptList list = conceptListManager.getConceptList(listName);
        conceptListAddForm.setListName(list.getConceptListName());
        conceptListAddForm.setDescription(list.getDescription());
        conceptListAddForm.setOldListName(listName);
        return "/auth/conceptlist/editlist";
    }

    /**
     * This method updates edited list information
     * 
     * @param listName
     *            Represents list whose data has to be updated
     * @param req
     *            holds HTTP request information
     * @return Returns a string value to redirect user to concept list page
     * @throws IllegalAccessException 
     * @throws IndexerRunningException 
     */
    @RequestMapping(value = "auth/conceptlist/storeeditlist", method = RequestMethod.POST)
    public String editList(HttpServletRequest req,
            @Validated @ModelAttribute("conceptListAddForm") ConceptListAddForm conceptListAddForm,
			BindingResult result, ModelMap model)
                    throws LuceneException, IllegalAccessException, IndexerRunningException {
        if (result.hasErrors()) {
            return "/auth/conceptlist/editlist";
        }

        ConceptList list = conceptListManager.getConceptList(conceptListAddForm.getOldListName());
        list.setConceptListName(conceptListAddForm.getListName());
        list.setDescription(conceptListAddForm.getDescription());

        if (indexService.isIndexerRunning()) {
            model.addAttribute("show_error_alert", true);
            model.addAttribute("error_alert_msg", indexerRunning);
            // Need to include command Object
            return "/auth/conceptlist/editlist";
        }

        conceptListManager.storeModifiedConceptList(list, conceptListAddForm.getOldListName());

        // modify the name for all the existing concepts under this concept
        // list
        List<ConceptEntry> entries = conceptManager.getConceptListEntries(conceptListAddForm.getOldListName());
        Iterator<ConceptEntry> entriesIterator = entries.iterator();

        while (entriesIterator.hasNext()) {
            ConceptEntry conceptEntry = (ConceptEntry) entriesIterator.next();
            conceptEntry.setConceptList(list.getConceptListName());
            conceptManager.storeModifiedConcept(conceptEntry);
            model.addAttribute(indexerStatus, indexerRunning);
        }
        return "redirect:/auth/conceptlist";
    }

    /**
     * Returns a string value to redirect user to concept list page when the
     * user cancels list edit operation
     * 
     * @return Returns a string value to redirect user to concept list page
     */
    @RequestMapping(value = "auth/conceptlist/editlist/canceledit", method = RequestMethod.GET)
    public String cancelEdit() {
        return "redirect:/auth/conceptlist";
    }

}
