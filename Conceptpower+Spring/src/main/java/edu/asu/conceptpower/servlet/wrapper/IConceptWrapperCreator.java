package edu.asu.conceptpower.servlet.wrapper;

import java.util.List;

import edu.asu.conceptpower.servlet.core.ConceptEntry;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;

public interface IConceptWrapperCreator {

	public abstract List<ConceptEntryWrapper> createWrappers(ConceptEntry[] entries) throws LuceneException;

}