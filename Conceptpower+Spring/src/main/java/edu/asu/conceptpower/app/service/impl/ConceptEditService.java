package edu.asu.conceptpower.app.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.service.IConceptEditService;
import edu.asu.conceptpower.app.wordnet.Constants;
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
     * If conceptEntry is null, this method just returns without editing the
     * concept. Else the concept entry is updated in the database and lucene
     * index.
     * 
     */
    @Override
    public void editConcepts(ConceptEntry conceptEntry, String oldWordnetIds, String updatedWordnetIds, String userName)
            throws IllegalAccessException, LuceneException, IndexerRunningException {

        if (conceptEntry == null) {
            return;
        }

        Set<String> oldWordnetIdsSet = new HashSet<String>(Arrays.asList(oldWordnetIds.split(",")));
        Set<String> updatedWordnetIdsSet = new HashSet<String>(Arrays.asList(updatedWordnetIds.split(",")));
        oldWordnetIdsSet.removeAll(updatedWordnetIdsSet);
        updateWordnetConcepts(oldWordnetIdsSet, userName);
        deleteWordNetConcepts(conceptEntry.getWordnetId(), userName);
        conceptManager.storeModifiedConcept(conceptEntry, userName);
    }

    private void updateWordnetConcepts(Set<String> oldWordnetIds, String userName)
            throws LuceneException, IllegalAccessException, IndexerRunningException {
        for (String wordnetId : oldWordnetIds) {
            if (!wordnetId.trim().equalsIgnoreCase("")) {
                ConceptEntry entry = conceptManager.getWordnetConceptEntry(wordnetId);
                entry.setConceptList(Constants.WORDNET_DICTIONARY);
                conceptManager.updateIndex(entry, userName);
            }
        }
    }

    private void deleteWordNetConcepts(String wordnetIds, String userName)
            throws LuceneException, IndexerRunningException {
        if (wordnetIds != null) {
            String[] wordnetIdArray = wordnetIds.split(",");
            for (String wordnetId : wordnetIdArray) {
                if (!wordnetId.trim().equalsIgnoreCase("")) {
                    conceptManager.deleteFromIndex(wordnetId, userName);
                }
            }
        }
    }

}
