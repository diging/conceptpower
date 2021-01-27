package edu.asu.conceptpower.app.manager.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.manager.IAlternativeIdService;
import edu.asu.conceptpower.app.manager.IConceptTypesService;
import edu.asu.conceptpower.app.manager.IConceptTypesService.IdType;
import edu.asu.conceptpower.app.model.ConceptEntry;

/**
 * This helper class is used for adding alternative ids to the concept entry.
 * 
 * @author karthikeyanmohan
 *
 */
@Service
public class AlternativeIdService implements IAlternativeIdService {

    @Autowired
    private IConceptTypesService conceptTypesService;

    /**
     * If id or concept entry passed to the method is null, then method just
     * returns and no changes are made.
     * 
     * @param queriedId
     * @param entry
     */
    public void addAlternativeIds(String queriedId, ConceptEntry entry) {

        if (entry == null) {
            return;
        }

        if (conceptTypesService.getConceptTypeByConceptId(queriedId) == IdType.GENERIC_WORDNET_CONCEPT_ID) {
            entry.setAlternativeIds(queriedId);
        }
        // Specific Wordnet id is added irrespective of what is queried for
        if (entry.getWordnetId() != null) {
            String[] wordNetIds = entry.getWordnetId().split(",");
            for (String wordNetId : wordNetIds) {
                entry.setAlternativeIds(wordNetId.trim());
            }
        }
        
        // This has been added to make sure local concept id is added.
        entry.setAlternativeIds(entry.getId());

        // Added the merged ids of the concepts to alternative id
        if (entry.getMergedIds() != null) {
            entry.setAlternativeIds(entry.getMergedIds());
        }
    }

    /**
     * This method adds the alternative ids to each of the concept entry in the
     * Collection<ConceptEntry> which is passed as a parameter.
     * 
     * @param conceptEntries
     */
    public void addAlternativeIds(Collection<ConceptEntry> conceptEntries) {
        for (ConceptEntry entry : conceptEntries) {
            addAlternativeIds(entry.getId(), entry);
        }
    }

    /**
     * This method adds the alternative ids to each of the concept entry in the
     * ConceptEntry[] which is passed as a parameter.
     * 
     * @param conceptEntries
     */
    public void addAlternativeIds(ConceptEntry[] conceptEntries) {
        for (ConceptEntry entry : conceptEntries) {
            addAlternativeIds(entry.getId(), entry);
        }
    }
}
