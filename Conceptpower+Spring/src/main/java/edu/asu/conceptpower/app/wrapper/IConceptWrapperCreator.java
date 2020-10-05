package edu.asu.conceptpower.app.wrapper;

import java.util.List;

import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.model.ConceptEntry;

public interface IConceptWrapperCreator {

	public abstract List<ConceptEntryWrapper> createWrappers(ConceptEntry[] entries) throws LuceneException;

}