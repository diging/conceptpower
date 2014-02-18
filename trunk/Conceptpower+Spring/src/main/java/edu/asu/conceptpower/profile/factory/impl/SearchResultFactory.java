package edu.asu.conceptpower.profile.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.conceptpower.profile.ISearchResult;
import edu.asu.conceptpower.profile.ISearchResultFactory;
import edu.asu.conceptpower.profile.impl.SearchResult;

@Service
public class SearchResultFactory implements ISearchResultFactory {

	@Override
	public ISearchResult getSearchResultObject() {
		return new SearchResult();
	}

}
