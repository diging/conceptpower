package edu.asu.conceptpower.web;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.manager.IConceptDBManager;
import edu.asu.conceptpower.app.manager.IConceptManager;
import edu.asu.conceptpower.app.manager.IIndexService;
import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.app.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.app.wrapper.impl.ConceptEntryWrapperCreator;

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
    public ResponseEntity<String> prepareDeleteConcept(@PathVariable("conceptid") String conceptid,
            @RequestParam(value = "fromHomeScreenDelete", required = false) String fromHomeScreenDelete,
            @RequestParam(value = "searchword", required = false) String searchword)
                    throws LuceneException, IndexerRunningException {
        ConceptEntry concept = conceptManager.getConceptEntry(conceptid);
        Map<String, String> details = new HashMap<String, String>();
        
        details.put("word", concept.getWord());
        details.put("description", concept.getDescription());
        details.put("conceptId", concept.getId());
        details.put("wordnetId", concept.getWordnetId());
        details.put("pos", concept.getPos());
        details.put("conceptList", concept.getConceptList());
        details.put("type", concept.getTypeId());
        details.put("equal", concept.getEqualTo());
        details.put("similar", concept.getSimilarTo());
        details.put("user", concept.getModified());
        details.put("modified", concept.getModified());
        details.put("synonyms", concept.getSynonymIds());
        details.put("searchword", searchword);
        
        if (fromHomeScreenDelete != null) {
            details.put("fromHomeScreenDelete", fromHomeScreenDelete);
        } else {
            details.put("fromHomeScreenDelete", "false");
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/html; charset=utf-8");
        return new ResponseEntity<String>(new JSONObject(details).toString(), responseHeaders, HttpStatus.OK);
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
        model.addAttribute("listid", conceptList);
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
            @RequestParam(value = "searchword") String searchword,
            @RequestParam(value = "listName") String listName, Principal principal,
            RedirectAttributes redirectAttributes) throws LuceneException, IndexerRunningException {
        ModelAndView model = new ModelAndView();
        ConceptEntry concept = conceptManager.getConceptEntry(id);
        // Check if indexer is running
        if (indexService.isIndexerRunning()) {
            model.addObject("show_error_alert", true);
            model.addObject("error_alert_msg", indexerRunning);
            // Need to include command Object
            model.setViewName("/auth/conceptlist/deleteconcept");
            return model;
        }

        conceptManager.deleteConcept(conceptManager.getConceptEntry(id), principal.getName());
        if (fromHomeScreenDelete.equalsIgnoreCase("true")) {
            model.setViewName("redirect:/home/conceptsearch?word="+searchword+"&pos="+concept.getPos());
            return model;
        }
        model.setViewName("redirect:/auth/" + listName + "/concepts");
        return model;
    }
}
