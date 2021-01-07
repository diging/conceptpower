package edu.asu.conceptpower.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.app.db.TypeDatabaseClient;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.manager.IConceptDBManager;
import edu.asu.conceptpower.app.manager.IConceptListManager;
import edu.asu.conceptpower.app.manager.IConceptManager;
import edu.asu.conceptpower.app.model.ConceptList;
import edu.asu.conceptpower.app.util.URIHelper;
import edu.asu.conceptpower.app.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.app.wrapper.IConceptWrapperCreator;
import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.app.model.ConceptType;

@Controller
public class ConceptListController {
    
    @Autowired
    private IConceptManager conceptManager;
    
    @Autowired
    private IConceptListManager conceptListService;

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

        List<ConceptList> conceptLists = conceptListService.getAllConceptLists();
        model.addAttribute("result", conceptLists);
        return "/layouts/concepts/conceptlist";
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
    @GetMapping(value = "auth/{listid}/concepts")
    public String getConceptsOfConceptList(@PathVariable("listid") String list, ModelMap model,
            @RequestParam(defaultValue = "1") String page,
            @RequestParam(defaultValue = IConceptDBManager.DESCENDING + "") String sortDir) throws LuceneException {

        int pageInt = Integer.parseInt(page);
        int sortDirInt = Integer.parseInt(sortDir);
        int pageCount = conceptManager.getPageCount(list);
        
        List<ConceptEntry> founds = conceptManager.getConceptListEntries(list, pageInt, -1, "id", sortDirInt);

        List<ConceptEntryWrapper> foundConcepts = wrapperCreator
                .createWrappers(founds != null ? founds
                        .toArray(new ConceptEntry[founds.size()])
                        : new ConceptEntry[0]);

        if (pageInt < 1) {
            pageInt = 1;
        }
        if (pageInt > pageCount) {
            pageInt = pageCount;
        }
        model.addAttribute("page", pageInt);
        model.addAttribute("result", foundConcepts);
        model.addAttribute("count", pageCount);
        model.addAttribute("listid", list);
        return "/layouts/concepts/conceptlistreferences";
    }

    /**
     * This method provides details of a concept for given concept ID
     * 
     * @param conceptid
     *            ID of a concept
     * @return Map containing concept details
     * @throws LuceneException 
     */
    @GetMapping(value = "conceptDetail", produces = "application/json")
    public @ResponseBody ResponseEntity<String> getConceptDetails(
            @RequestParam("conceptid") String conceptid) throws LuceneException {
        ConceptEntry entry = conceptManager.getConceptEntry(conceptid);
        List<ConceptEntryWrapper> wrappers = wrapperCreator.createWrappers(new ConceptEntry[] { entry } );
        ConceptEntryWrapper wrapper = null;
        if (wrappers.size() > 0) {
            wrapper = wrappers.get(0);
        }
        else {
            return new ResponseEntity<>("No entry for the provided id.", HttpStatus.BAD_REQUEST);
        }
        
        Map<String, String> details = new HashMap<>();

        details.put("name", wrapper.getEntry().getWord());
        details.put("id", wrapper.getEntry().getId());
        details.put("uri", URICreator.getURI(wrapper.getEntry()));
        //This condition has been included to make sure null values are not displayed in the details dialog box
        details.put("wordnetid", wrapper.getEntry().getWordnetId()==null?"":wrapper.getEntry().getWordnetId());
        details.put("pos", wrapper.getEntry().getPos());
        details.put("conceptlist", wrapper.getEntry().getConceptList());

        ConceptType type = wrapper.getEntry().getTypeId() == null ? null
                : typeDatabaseClient.getType(
                        wrapper.getEntry().getTypeId());
        
        details.put("description", wrapper.getDescription());
        
        details.put(
                "type",
                type == null ? "" : type.getTypeName());
        details.put("equalto",
                wrapper.getEntry().getEqualTo() == null ? ""
                        : wrapper.getEntry().getEqualTo());
        details.put("similarto",
                wrapper.getEntry().getSimilarTo() == null ? ""
                        : wrapper.getEntry().getSimilarTo());
        details.put("creator",
                wrapper.getEntry().getCreatorId() == null ? ""
                        : wrapper.getEntry().getCreatorId());
        details.put("mergedIds", wrapper.getEntry().getMergedIds() == null ? "" : wrapper.getEntry().getMergedIds());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/html; charset=utf-8");
        return new ResponseEntity<>(new JSONObject(details).toString(), responseHeaders, HttpStatus.OK);
    }
}
