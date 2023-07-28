package edu.asu.conceptpower.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.manager.IIndexService;
import edu.asu.conceptpower.app.model.IndexingEvent;
import jakarta.servlet.http.HttpServletRequest;

/**
 * This class provides methods for deleting and viewing lucene indexes
 * 
 * @author mkarthik90
 *
 */
@Controller
public class LuceneIndexController {

    @Autowired
    private IIndexService manager;
    
    @Value("${messages['INDEXER_RUNNING']}")
    private String indexerRunning;

    /**
     * Retrives latest indexing time and number of concepts indexed so far
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = "/auth/index", method = RequestMethod.GET)
    public String onLoadLucene(ModelMap model) {
        IndexingEvent bean = manager.getTotalNumberOfWordsIndexed();
        model.addAttribute("bean", bean);
        return "/layouts/lucene/indexconcepts";
    }

    /**
     * This method deletes all the index in Lucene and recreates all the index
     * from Wordnet and from CCP db40 database
     * 
     * @param req
     * @param principal
     * @param model
     * @return
     * @throws IndexerRunningException
     */
    @RequestMapping(value = "/auth/index/start", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity<IndexingEvent> indexConcepts(HttpServletRequest req, Principal principal,
            ModelMap model)
            throws IndexerRunningException {
        IndexingEvent bean = new IndexingEvent();
        try {
            if (manager.isIndexerRunning()) {
                bean.setMessage(indexerRunning);
                return new ResponseEntity<IndexingEvent>(bean, HttpStatus.SERVICE_UNAVAILABLE);
            }

            manager.deleteIndexes(principal.getName());
            manager.indexConcepts(principal.getName());
            bean.setMessage("Indexer Running");
            return new ResponseEntity<IndexingEvent>(bean, HttpStatus.OK);
        } catch (LuceneException | IllegalArgumentException | IllegalAccessException ex) {
            bean.setMessage(ex.getMessage());
            return new ResponseEntity<IndexingEvent>(bean, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/auth/index/status", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity<IndexingEvent> getIndexerStatus() throws IndexerRunningException {

        IndexingEvent bean = new IndexingEvent();
        if (!manager.isIndexerRunning()) {
            bean = manager.getTotalNumberOfWordsIndexed();
            bean.setMessage("Indexed successfully");
            return new ResponseEntity<IndexingEvent>(bean, HttpStatus.OK);
        }
        bean.setMessage("Indexer Running");
        return new ResponseEntity<IndexingEvent>(bean, HttpStatus.ACCEPTED);
    }

    /**
     * This method deletes all the concepts in the lucene
     * 
     * @param req
     * @param principal
     * @param model
     * @return
     * @throws IndexerRunningException
     */
    @RequestMapping(value = "/auth/index/delete", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody IndexingEvent deleteConcepts(HttpServletRequest req, Principal principal, ModelMap model)
            throws IndexerRunningException {
        IndexingEvent bean = new IndexingEvent();
        try {
            if (manager.isIndexerRunning()) {
                bean = manager.getTotalNumberOfWordsIndexed();
                bean.setMessage(indexerRunning);
                return bean;
            }
            manager.deleteIndexes(principal.getName());
            bean = manager.getTotalNumberOfWordsIndexed();
            bean.setMessage("Concepts deleted from index successfully");
            return bean;
        } catch (LuceneException e) {
            bean.setMessage(e.getMessage());
            return bean;
        }
    }
}