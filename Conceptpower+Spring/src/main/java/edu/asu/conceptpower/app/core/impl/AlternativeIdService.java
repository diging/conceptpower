package edu.asu.conceptpower.app.core.impl;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.ConceptTypesService;
import edu.asu.conceptpower.app.core.ConceptTypesService.ConceptTypes;
import edu.asu.conceptpower.core.ConceptEntry;

@Service
public class AlternativeIdService {

    @Autowired
    private ConceptTypesService conceptTypesService;

    public void addAlternativeIds(String queriedId, ConceptEntry entry) {
        if (entry.getAlternativeIds() == null) {
            entry.setAlternativeIds(new HashSet<String>());
        }
        if (conceptTypesService.getConceptTypeByConceptId(queriedId) == ConceptTypes.GENERIC_WORDNET_CONCEPT) {
            entry.getAlternativeIds().add(queriedId);
        }
        if (conceptTypesService.getConceptTypeByConceptId(queriedId) != ConceptTypes.SPECIFIC_WORDNET_CONCEPT) {
            // Added for generic wordnet and specific local concept
            entry.getAlternativeIds().add(entry.getId());
        }
        // Specific Wordnet id is added irrespective of what is queried for
        if (entry.getWordnetId() != null) {
            String[] wordNetIds = entry.getWordnetId().split(",");
            for (String wordNetId : wordNetIds) {
                entry.getAlternativeIds().add(wordNetId);
            }
        }
    }

    public void addAlternativeIds(Collection<ConceptEntry> conceptEntries) {
        for (ConceptEntry entry : conceptEntries) {
            addAlternativeIds(entry.getId(), entry);
        }
    }
    
    public void addAlternativeIds(ConceptEntry[] conceptEntries) {
        for (ConceptEntry entry : conceptEntries) {
            addAlternativeIds(entry.getId(), entry);
        }
    }
}
