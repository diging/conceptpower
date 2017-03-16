package edu.asu.conceptpower.app.core;

import java.util.Collection;

import edu.asu.conceptpower.core.ConceptEntry;

public interface IAlternativeIdService {

    /**
     * This method adds the alternative ids to the concept entry which is passed
     * as a method parameter.
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
