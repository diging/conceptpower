package edu.asu.conceptpower.servlet.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.servlet.core.ErrorConstants;
import edu.asu.conceptpower.servlet.core.IIndexService;
import edu.asu.conceptpower.servlet.core.IndexingEvent;
import edu.asu.conceptpower.servlet.exceptions.IndexerRunningException;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.lucene.ILuceneDAO;

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

    @Autowired
    @Qualifier("luceneDAO")
    private ILuceneDAO dao;

    /**
     * Retrives latest indexing time and number of concepts indexed so far
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = "auth/luceneIndex", method = RequestMethod.GET)
    public String showLuceneIndex(ModelMap model) {
        IndexingEvent bean = dao.getTotalNumberOfWordsIndexed();
        model.addAttribute("bean", bean);
        return "/auth/luceneIndex";
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
    @RequestMapping(value = "auth/indexLuceneWordNet", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody IndexingEvent indexConcepts(HttpServletRequest req, Principal principal, ModelMap model)
            throws IndexerRunningException {
        IndexingEvent bean = null;
        try {
            if (manager.checkIndexerStatus()) {
                bean = dao.getTotalNumberOfWordsIndexed();
                bean.setMessage(ErrorConstants.INDEXER_RUNNING);
                return bean;
            }
            manager.deleteIndexes();
            manager.indexConcepts();
            bean = dao.getTotalNumberOfWordsIndexed();
            bean.setMessage("Indexed successfully");
            return bean;
        } catch (LuceneException | IllegalArgumentException | IllegalAccessException ex) {
            bean.setMessage(ex.getMessage());
            return bean;
        }
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
    @RequestMapping(value = "auth/deleteIndex", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody IndexingEvent deleteConcepts(HttpServletRequest req, Principal principal, ModelMap model)
            throws IndexerRunningException {
        IndexingEvent bean = new IndexingEvent();
        try {
            if (manager.checkIndexerStatus()) {
                bean = dao.getTotalNumberOfWordsIndexed();
                bean.setMessage(ErrorConstants.INDEXER_RUNNING);
                return bean;
            }
            manager.deleteIndexes();
            bean = dao.getTotalNumberOfWordsIndexed();
            bean.setMessage("Concepts deleted from index successfully");
            return bean;
        } catch (LuceneException e) {
            bean.setMessage(e.getMessage());
            return bean;
        }
    }
}
