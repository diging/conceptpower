package edu.asu.conceptpower.servlet.profile.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.conceptpower.servlet.profile.ISearchResult;
import edu.asu.conceptpower.servlet.profile.ISearchResultFactory;
import edu.asu.conceptpower.servlet.profile.impl.SearchResult;

/**
 * this class has method which creates new object of ISearchResult type
 * 
 * @author rohit
 * 
 */
@Service
public class SearchResultFactory implements ISearchResultFactory {

	/**
	 * creates object of class which has ISearchResult reference
	 * 
	 * @return object of SearchResult class
	 * 
	 */
	@Override
	public ISearchResult getSearchResultObject() {
		return new SearchResult();
	}

}
