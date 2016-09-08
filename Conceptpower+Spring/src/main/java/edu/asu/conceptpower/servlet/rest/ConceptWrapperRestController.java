package edu.asu.conceptpower.servlet.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.servlet.core.Constants;
import edu.asu.conceptpower.servlet.core.IConceptManager;
import edu.asu.conceptpower.servlet.core.IIndexService;
import edu.asu.conceptpower.servlet.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.servlet.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.servlet.exceptions.IndexerRunningException;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;

@RestController
public class ConceptWrapperRestController {

    @Autowired
    private IConceptManager conceptManager;

    @Autowired
    private IIndexService indexService;

    @RequestMapping(value = "rest/conceptwrapper/add", method = RequestMethod.POST)
    public ResponseEntity<String> addConceptWrapper(@RequestBody ConceptEntry conceptEntry, Principal principal)
            throws LuceneException, IllegalAccessException, DictionaryDoesNotExistException, DictionaryModifyException,
            IndexerRunningException {

        if (conceptEntry.getConceptList() == null || conceptEntry.getConceptList().trim().isEmpty()) {
            return new ResponseEntity<String>("Concept list cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (conceptEntry.getWordnetId() != null) {
            String[] wrappers = conceptEntry.getWordnetId().split(Constants.CONCEPT_SEPARATOR);
            if (wrappers.length > 0) {
                conceptEntry.setWord(conceptManager.getConceptEntry(wrappers[0]).getWord().replace("_", " "));
                conceptEntry.setPos(conceptManager.getConceptEntry(wrappers[0]).getPos());
                conceptEntry.setCreatorId(principal.getName());

                if (indexService.isIndexerRunning()) {
                    return new ResponseEntity<String>("Indexer running. Try again after some time.",
                            HttpStatus.INTERNAL_SERVER_ERROR);
                }
                conceptManager.addConceptListEntry(conceptEntry, principal.getName());
            }
        }

        return new ResponseEntity<String>("Concept wrapper created successfully", HttpStatus.OK);
    }
}
