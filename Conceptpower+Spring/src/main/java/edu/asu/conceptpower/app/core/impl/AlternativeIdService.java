package edu.asu.conceptpower.app.core.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.IAlternativeIdService;
import edu.asu.conceptpower.app.core.IConceptTypesService;
import edu.asu.conceptpower.app.core.impl.ConceptTypesService.ConceptTypes;
import edu.asu.conceptpower.core.ConceptEntry;

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
     * This method adds the alternative ids provided concept entry and the id
     * with which the entry is queried is passed as parameter.
     * 
     * Alternative ids comprises of wordnet ids, local concept id, merged id and
     * generic wordnet id(if concept entry is queried with generic wordnet id
     * ??)
     */
    public void addAlternativeIds(String queriedId, ConceptEntry entry) {
        if (conceptTypesService.getConceptTypeByConceptId(queriedId) == ConceptTypes.GENERIC_WORDNET_CONCEPT) {
            entry.getAlternativeIds().add(queriedId);
        }
        // Specific Wordnet id is added irrespective of what is queried for
        if (entry.getWordnetId() != null) {
            String[] wordNetIds = entry.getWordnetId().split(",");
            for (String wordNetId : wordNetIds) {
                entry.getAlternativeIds().add(wordNetId.trim());
            }
        }
        // This has been added to make sure local concept id is added.
        entry.getAlternativeIds().add(entry.getId());

        // Added the merged ids of the concepts to alternative id
        if (entry.getMergedIds() != null) {
            String[] mergedIds = entry.getMergedIds().split(",");
            for (String mergedId : mergedIds) {
                entry.getAlternativeIds().add(mergedId.trim());
            }
        }
    }

    /**
     * This method in turn calls the addAlternativeIds(String queriedId,
     * ConceptEntry entry) to add the alternative id
     */
    public void addAlternativeIds(Collection<ConceptEntry> conceptEntries) {
        for (ConceptEntry entry : conceptEntries) {
            addAlternativeIds(entry.getId(), entry);
        }
    }

    /**
     * This method in turn calls the addAlternativeIds(String queriedId,
     * ConceptEntry entry) to add the alternative id
     */
    public void addAlternativeIds(ConceptEntry[] conceptEntries) {
        for (ConceptEntry entry : conceptEntries) {
            addAlternativeIds(entry.getId(), entry);
        }
    }
}
