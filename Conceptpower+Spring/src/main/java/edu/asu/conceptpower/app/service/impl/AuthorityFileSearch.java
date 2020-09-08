package edu.asu.conceptpower.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.profile.ISearchResult;
import edu.asu.conceptpower.app.profile.IService;
import edu.asu.conceptpower.app.profile.IServiceRegistry;
import edu.asu.conceptpower.app.service.IAuthorityFileSearch;

/**
 * This class query a specified service and returns the results.
 * 
 * @author rohit pendbhaje
 * 
 */

@Service
public class AuthorityFileSearch implements
		IAuthorityFileSearch {

	@Autowired
	private IServiceRegistry serviceRegistry;

	
	/**
	 * this method takes results from viafservice method and copy it in the
	 * SearchResultBackBean
	 * 
	 * 
	 * @param serviceId
	 *            id of the service selected by the user
	 * @param term
	 *            term entered by the user
	 * @return list of copied searchresults of SearchResultBackBean class
	 *         returned from viaf service
	 */
	@Override
	public List<ISearchResult> getSearchResultBackBeanList(
			String serviceId, String term) {

		IService serviceObj = serviceRegistry.getServiceObject(serviceId);
		List<ISearchResult> searchResults = serviceObj.search(term);
		
		return searchResults;

	}

}
