package edu.asu.conceptpower.app.manager;

import java.util.Collection;

import edu.asu.conceptpower.app.model.ConceptEntry;

public interface IAlternativeIdService {

    /**
     * This method adds all alternative ids to the given concept entry.
     * 
     * Alternative ids comprise queriedId, if the queriedId is of
     * GENERIC_WORDNET_CONCEPT_ID type, specific wordnet ids from the worndet id
     * field of the concept, the id of the concept itself, if the id is of type
     * LOCAL_CONCEPT_ID type and the merged ids.
     * 
     * @param queriedId
     *            - Id with which the concept entry is queried by the user.
     * @param entry
     *            - ConceptEntry corresponding to that id.
     */
    public void addAlternativeIds(String queriedId, ConceptEntry entry);

    /**
     * This method adds the alternative ids to each of the concept entry in the
     * Collection object which is passed as a method parameter.
     * 
     * @param conceptEntries
     *            - Collection of concept entry.
     */
    public void addAlternativeIds(Collection<ConceptEntry> conceptEntries);

    /**
     * This method adds the alternative ids to each of the concept entry in the
     * Concept entry array which is passed as a method parameter.
     * 
     * @param conceptEntries
     *            - Array of concept entry.
     */
    public void addAlternativeIds(ConceptEntry[] conceptEntries);
}
