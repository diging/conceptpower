package edu.asu.conceptpower.wrapper;

import java.util.List;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.exceptions.LuceneException;

public interface IConceptWrapperCreator {

	public abstract List<ConceptEntryWrapper> createWrappers(ConceptEntry[] entries) throws LuceneException;

}