package edu.asu.conceptpower.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.db4o.ObjectContainer;

import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.core.IIndexService;
import edu.asu.conceptpower.app.db.DatabaseManager;
import edu.asu.conceptpower.app.db4o.IConceptDBManager;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.validation.ConceptSearchValidator;
import edu.asu.conceptpower.app.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.app.wrapper.IConceptWrapperCreator;
import edu.asu.conceptpower.core.ConceptEntry;

/**
 * This class provides concept search methods
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptSearchController {

    @Autowired
    @Qualifier("conceptReviewDatabaseManager")
    private DatabaseManager dbManager;
    
    private ObjectContainer client;
    
    @Autowired
    private IConceptManager conceptManager;

    @Autowired
    private IConceptWrapperCreator wrapperCreator;

    @Autowired
    private ConceptSearchValidator validator;
    
    @Autowired
    private IIndexService indexService;
    
    @Value("#{messages['INDEXER_RUNNING']}")
    private String indexerRunning;
    
    @Value("#{messages['INDEXERSTATUS']}")
    private String indexerStatus;

    @Value("${default_page_size}")
    private Integer defaultPageSize;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(this.validator);
    }

    /**
     * This method is searches concepts a specific term, pos based on the
     * pagination details.
     * 
     * @param model
     * @param page
     * @param sortDir
     * @param sortColumn
     * @param conceptSearchBean
     * @param results
     * @return
     * @throws LuceneException
     * @throws IllegalAccessException
     */
    @RequestMapping(value = "/home/conceptsearch", method = RequestMethod.GET)
    public String search(HttpServletRequest req, ModelMap model, @RequestParam(defaultValue = "1") String page,
            @RequestParam(defaultValue = IConceptDBManager.ASCENDING + "") String sortDir,
            @RequestParam(required = false) String sortColumn,
            @RequestParam(required = false) String conceptIdsToMerge,
            @Validated @ModelAttribute("conceptSearchBean") ConceptSearchBean conceptSearchBean, BindingResult results)
                    throws LuceneException, IllegalAccessException {
        if (results.hasErrors()) {
            return "conceptsearch";
        }
        conceptSearchBean.setWord(conceptSearchBean.getWord().trim());
        
        if (conceptIdsToMerge != null) {
            model.addAttribute("conceptIdsToMerge", conceptIdsToMerge);
        }

        List<ConceptEntryWrapper> foundConcepts = null;
        ConceptEntry[] found = null;
       
        Map<String, String> reviewEnabled = new HashMap<String, String>();
        
        if (indexService.isIndexerRunning()) {
            model.addAttribute("show_error_alert", true);
            model.addAttribute("error_alert_msg", indexerRunning);
            // Need to include command Object
            return "conceptsearch";
        }
        int pageInt = new Integer(page);
        int sortDirInt = new Integer(sortDir);
        int pageCount = 0;
        try {
            found = conceptManager.getConceptListEntriesForWordPOS(conceptSearchBean.getWord(),
                    conceptSearchBean.getPos(), null, pageInt, defaultPageSize, sortColumn, sortDirInt);
            pageCount = conceptManager.getPageCountForConceptEntries(conceptSearchBean.getWord(),
                    conceptSearchBean.getPos(), null);
        } catch (IndexerRunningException e) {
            model.addAttribute(indexerStatus, e.getMessage());
            return "conceptsearch";
        }
       
        //searching for the entries,in found[], in conceptReview.db
        if(found!= null) {
        
        for(int i=0;i<found.length;i++) {
            if(dbManager.getEntry(found[i].getWordnetId(),true) != null) {
                reviewEnabled.put(found[i].getWordnetId(),"true");
            }
        }
        }//--
        foundConcepts = wrapperCreator.createWrappers(found);
        conceptSearchBean.setFoundConcepts(foundConcepts);
        conceptSearchBean.setReviewEnabledWord(reviewEnabled); // added for passing the array of reviewEnabled wordNet Ids
        
        if (pageInt < 1) {
            pageInt = 1;
        }

        if (pageInt > pageCount) {
            pageInt = pageCount;
        }

        if (CollectionUtils.isEmpty(foundConcepts)) {
            results.rejectValue("foundConcepts", "no.searchResults");
        }
        model.addAttribute("page", pageInt);
        model.addAttribute("count", pageCount);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortColumn", sortColumn);
        
        return "conceptsearch";
    }

}
