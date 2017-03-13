package edu.asu.conceptpower.app.core;

import java.util.Collection;

import edu.asu.conceptpower.core.ConceptEntry;

public interface IAlternativeIdService {

    public void addAlternativeIds(String queriedId, ConceptEntry entry);

    public void addAlternativeIds(Collection<ConceptEntry> conceptEntries);

    public void addAlternativeIds(ConceptEntry[] conceptEntries);
}
