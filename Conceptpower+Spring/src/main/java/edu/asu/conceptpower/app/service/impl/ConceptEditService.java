package edu.asu.conceptpower.app.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.bean.ConceptEditBean;
import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.service.IConceptEditService;
import edu.asu.conceptpower.core.ConceptEntry;

@Service
public class ConceptEditService implements IConceptEditService {

    @Autowired
    private IConceptManager conceptManager;

    /**
     * This method adds all the wordnet concepts to the database and lucene
     * index that are removed from wordnet id field of wrapper. The next step is
     * to remove all the wordnet concepts from database and lucene index that
     * are added to the wordnet id field of the wrapper. Finally the concept is
     * updated in the lucene index and database.
     * 
     * 
     * If conceptEntry or conceptEditBean is null, this method just returns
     * without editing the concept. Else the concept entry is updated in the
     * database and lucene index.
     * 
     * Concept Edit bean is used for fetching the wordnet ids that are added and
     * removed from the concept.
     * 
     */
    @Override
    public void editConcepts(ConceptEntry conceptEntry, ConceptEditBean conceptEditBean, String userName)
            throws IllegalAccessException, LuceneException, IndexerRunningException {

        if (conceptEntry == null || conceptEditBean == null) {
            return;
        }

        Set<String> oldWordnetIds = new HashSet<String>(
                Arrays.asList(conceptEditBean.getExistingWordnetIds().split(",")));
        Set<String> updatedWordnetIds = new HashSet<String>(Arrays.asList(conceptEditBean.getWordnetIds().split(",")));
        oldWordnetIds.removeAll(updatedWordnetIds);
        addWordNetConcepts(oldWordnetIds, userName);
        deleteWordNetConcepts(conceptEntry.getWordnetId(), userName);
        conceptManager.storeModifiedConcept(conceptEntry, userName);
    }

    private void addWordNetConcepts(Set<String> oldWordnetIds, String userName)
            throws LuceneException, IllegalAccessException, IndexerRunningException {
        for (String wordnetId : oldWordnetIds) {
            if (!wordnetId.trim().equalsIgnoreCase("")) {
                ConceptEntry entry = conceptManager.getWordnetConceptEntry(wordnetId);
                conceptManager.addWordnetConceptEntry(entry, userName);
            }
        }
    }

    private void deleteWordNetConcepts(String wordnetIds, String userName)
            throws LuceneException, IndexerRunningException {
        if (wordnetIds != null) {
            String[] wordnetIdArray = wordnetIds.split(",");
            for (String wordnetId : wordnetIdArray) {
                if (!wordnetId.trim().equalsIgnoreCase("")) {
                    conceptManager.deleteConceptById(wordnetId, userName);
                }
            }

        }
    }

}
