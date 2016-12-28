package edu.asu.conceptpower.web;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.core.IIndexService;
import edu.asu.conceptpower.app.db4o.IConceptDBManager;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.app.wrapper.impl.ConceptEntryWrapperCreator;
import edu.asu.conceptpower.core.ConceptEntry;

/**
 * This class provides all the required methods for deleting a concept
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptDeleteController {

    @Autowired
    private IConceptManager conceptManager;

    @Autowired
    private ConceptEntryWrapperCreator wrapperCreator;

    @Autowired
    private IIndexService indexService;

    @Value("#{messages['INDEXER_RUNNING']}")
    private String indexerRunning;

    /**
     * This method provides details of a concept to be deleted for concept
     * delete page
     * 
     * @param conceptid
     *            ID of a concept to be deleted
     * @param model
     *            A generic model holder for Servlet
     * @return String value to redirect user to concept delete page
     * @throws IndexerRunningException
     */
    @RequestMapping(value = "auth/conceptlist/deleteconcept/{conceptid}", method = RequestMethod.GET)
    public String prepareDeleteConcept(@PathVariable("conceptid") String conceptid, ModelMap model,
            @RequestParam(value = "fromHomeScreenDelete", required = false) String fromHomeScreenDelete)
                    throws LuceneException, IndexerRunningException {
        ConceptEntry concept = conceptManager.getConceptEntry(conceptid);
        model.addAttribute("word", concept.getWord());
        model.addAttribute("description", concept.getDescription());
        model.addAttribute("conceptId", concept.getId());
        model.addAttribute("wordnetId", concept.getWordnetId());
        model.addAttribute("pos", concept.getPos());
        model.addAttribute("conceptList", concept.getConceptList());
        model.addAttribute("type", concept.getTypeId());
        model.addAttribute("equal", concept.getEqualTo());
        model.addAttribute("similar", concept.getSimilarTo());
        model.addAttribute("user", concept.getModified());
        model.addAttribute("modified", concept.getModified());
        model.addAttribute("synonyms", concept.getSynonymIds());

        if (fromHomeScreenDelete != null) {
            model.addAttribute("fromHomeScreenDelete", fromHomeScreenDelete);
        } else {
            model.addAttribute("fromHomeScreenDelete", false);
        }
        return "/auth/conceptlist/deleteconcept";
    }

    /**
     * This method returns user to a particular concept list page after the user
     * cancels concept delete operation
     * 
     * @param conceptList
     *            concept list name where user has to redirected
     * @param model
     *            A generic model holder for Servlet
     * @return String value to redirect user to a particular concept list
     */
    @RequestMapping(value = "auth/concepts/canceldelete/{conceptList}", method = RequestMethod.GET)
    public String cancelDelete(@PathVariable("conceptList") String conceptList, ModelMap model) throws LuceneException {
        List<ConceptEntryWrapper> foundConcepts = null;
        List<ConceptEntry> founds = conceptManager.getConceptListEntries(conceptList, 1, -1, "id",
                IConceptDBManager.DESCENDING);
        foundConcepts = wrapperCreator
                .createWrappers(founds != null ? founds.toArray(new ConceptEntry[founds.size()]) : new ConceptEntry[0]);

        model.addAttribute("result", foundConcepts);
        return "/auth/conceptlist/concepts";
    }

    /**
     * This method deletes a concept
     * 
     * @param id
     *            Concept ID to be deleted
     * @param model
     *            A generic model holder for Servlet
     * @return String value to redirect user to a particular concept list page
     * @throws IndexerRunningException
     */
    @RequestMapping(value = "auth/conceptlist/deleteconceptconfirm/{id}", method = RequestMethod.POST)
    public ModelAndView confirmDelete(@PathVariable("id") String id,
            @RequestParam(value = "fromHomeScreenDelete") String fromHomeScreenDelete,
            @RequestParam(value = "listName") String listName, Principal principal,
            RedirectAttributes redirectAttributes) throws LuceneException, IndexerRunningException {
        ModelAndView model = new ModelAndView();
        // Check if indexer is running
        if (indexService.isIndexerRunning()) {
            model.addObject("show_error_alert", true);
            model.addObject("error_alert_msg", indexerRunning);
            // Need to include command Object
            model.setViewName("/auth/conceptlist/deleteconcept");
            return model;
        }

        conceptManager.deleteConcept(id, principal.getName());
        if (fromHomeScreenDelete.equalsIgnoreCase("true")) {
            model.setViewName("redirect:/login");
            return model;
        }
        model.setViewName("redirect:/auth/" + listName + "/concepts");
        return model;
    }
}
