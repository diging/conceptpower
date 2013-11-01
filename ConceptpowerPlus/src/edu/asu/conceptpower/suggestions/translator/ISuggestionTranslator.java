package edu.asu.conceptpower.suggestions.translator;

import java.util.List;

import edu.asu.conceptpower.external.IMappingManager;
import edu.asu.conceptpower.suggestions.IConceptEntrySuggestion;
import edu.asu.conceptpower.suggestions.IXMLSuggestion;

public interface ISuggestionTranslator {

	public IConceptEntrySuggestion getSuggestion(List<IXMLSuggestion> xmlSuggestions);
	
	public IMappingManager getMappingManager();
}
