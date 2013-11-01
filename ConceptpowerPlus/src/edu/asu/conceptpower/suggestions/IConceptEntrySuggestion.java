package edu.asu.conceptpower.suggestions;

import java.util.List;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.external.IExternalSource;

public interface IConceptEntrySuggestion {

	// SimilarTo: mendeley:http://mendeley.com/xxx
	
	public ConceptEntry getConceptEntry();
	public List<IExternalSource> getExternalSource();
}
