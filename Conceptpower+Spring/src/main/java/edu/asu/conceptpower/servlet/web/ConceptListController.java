package edu.asu.conceptpower.servlet.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.root.TypeDatabaseClient;
import edu.asu.conceptpower.root.URIHelper;
import edu.asu.conceptpower.servlet.core.ConceptEntry;
import edu.asu.conceptpower.servlet.core.ConceptList;
import edu.asu.conceptpower.servlet.core.ConceptType;
import edu.asu.conceptpower.servlet.core.IConceptListManager;
import edu.asu.conceptpower.servlet.core.IConceptManager;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.servlet.wrapper.IConceptWrapperCreator;

@Controller
public class ConceptListController {

    @Autowired
    private IConceptManager conceptManager;

    @Autowired
    private IConceptListManager conceptListManager;

    @Autowired
    private TypeDatabaseClient typeDatabaseClient;

    @Autowired
    private URIHelper URICreator;

    @Autowired
    private IConceptWrapperCreator wrapperCreator;

    /**
     * This method provides information of all the existing concept lists for
     * showing concept list page
     * 
     * @param model
     *            A generic model holder for Servlet
     * @return String value to redirect user to all concpet list page
     */
    @RequestMapping(value = "auth/conceptlist")
    public String prepareShowConceptList(ModelMap model) {

        List<ConceptList> conceptLists = conceptListManager.getAllConceptLists();
        model.addAttribute("result", conceptLists);

        return "/auth/conceptlist";
    }

    /**
     * This method provides infomaiton of concepts for a given concept list
     * 
     * @param list
     * @param model
     *            A generic model holder for Servlet
     * @return Return sting value to redirect user to a particular concept list
     *         page
     */
    @RequestMapping(value = "auth/{listid}/concepts", method = RequestMethod.GET)
    public String getConceptsOfConceptList(@PathVariable("listid") String list, ModelMap model) throws LuceneException {

        List<ConceptEntry> founds = conceptManager.getConceptListEntries(list);

        List<ConceptEntryWrapper> foundConcepts = wrapperCreator
                .createWrappers(founds != null ? founds.toArray(new ConceptEntry[founds.size()]) : new ConceptEntry[0]);

        model.addAttribute("result", foundConcepts);
        return "/auth/conceptlist/concepts";
    }

    /**
     * This method provides details of a concept for given concept ID
     * 
     * @param conceptid
     *            ID of a concept
     * @return Map containing concept details
     */
    @RequestMapping(method = RequestMethod.GET, value = "conceptDetail", produces = "application/json")
    public @ResponseBody ResponseEntity<String> getConceptDetails(@RequestParam("conceptid") String conceptid)
            throws LuceneException {
        ConceptEntryWrapper conceptEntry = null;
        conceptEntry = new ConceptEntryWrapper(conceptManager.getConceptEntry(conceptid));
        Map<String, String> details = new HashMap<String, String>();

        details.put("name", conceptEntry.getEntry().getWord());
        details.put("id", conceptEntry.getEntry().getId());
        details.put("uri", URICreator.getURI(conceptEntry.getEntry()));
        // This condition has been included to make sure null values are not
        // displayed in the details dialog box
        details.put("wordnetid",
                conceptEntry.getEntry().getWordnetId() == null ? "" : conceptEntry.getEntry().getWordnetId());
        details.put("pos", conceptEntry.getEntry().getPos());
        details.put("conceptlist", conceptEntry.getEntry().getConceptList());

        ConceptType type = conceptEntry.getEntry().getTypeId() == null ? null
                : typeDatabaseClient.getType(conceptEntry.getEntry().getTypeId());

        details.put("type", type == null ? "" : type.getTypeName());
        details.put("equalto",
                conceptEntry.getEntry().getEqualTo() == null ? "" : conceptEntry.getEntry().getEqualTo());
        details.put("similarto",
                conceptEntry.getEntry().getSimilarTo() == null ? "" : conceptEntry.getEntry().getSimilarTo());
        details.put("creator",
                conceptEntry.getEntry().getCreatorId() == null ? "" : conceptEntry.getEntry().getCreatorId());

        return new ResponseEntity<String>(new JSONObject(details).toString(), HttpStatus.OK);
    }
}
