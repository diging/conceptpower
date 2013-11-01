package edu.asu.conceptpower.suggestions.translator.impl;

import java.util.List;

import edu.asu.conceptpower.external.IMappingManager;
import edu.asu.conceptpower.suggestions.IConceptEntrySuggestion;
import edu.asu.conceptpower.suggestions.IXMLSuggestion;
import edu.asu.conceptpower.suggestions.translator.ISuggestionTranslator;

public class SuggestionTranslatorImpl implements ISuggestionTranslator {
	
	private IMappingManager manager;
	
	public SuggestionTranslatorImpl(IMappingManager manager) {
		this.manager = manager;
	}

	public IConceptEntrySuggestion getSuggestion(
			List<IXMLSuggestion> xmlSuggestions) {
		// TODO code goes here
		return null;
	}

	public IMappingManager getMappingManager() {
		// TODO Auto-generated method stub
		return null;
	}

}
