package edu.asu.conceptpower.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.profile.ISearchResult;
import edu.asu.conceptpower.service.impl.AuthorityFileSearch;
import edu.asu.conceptpower.web.back.SearchResultBackBean;

/**
 * This controller answer requests to other authority files
 * external to Conceptpower (e.g. VIAF).
 * 
 * @author Julia Damerow
 *
 */
@Controller
public class ConceptSearchJsonController {

	@Autowired
	private AuthorityFileSearch authorityFileSearch;

	
	/**
	 * This method searches a specific service for given term and returns
	 * the results in JSON format.
	 * 
	 * @param serviceterm
	 *            holds a string value for which we need to service search
	 * @param serviceid
	 *            holds service ID string which represents a specific service
	 *            selected by user
	 * @return Returns array of SearchResultBackBean objects which represent
	 *         service search results for specific servicterm and serviceid
	 *        
	 * @author Chetan Ambi
	 */
	@RequestMapping(method = RequestMethod.GET, value = "serviceSearch")
	public @ResponseBody
	SearchResultBackBean[] serviceSearchForConcept(
			@RequestParam("serviceterm") String serviceterm,
			@RequestParam("serviceid") String serviceid) {

		List<ISearchResult> searchResults = authorityFileSearch
				.getSearchResultBackBeanList(serviceid, serviceterm);
		
		List<SearchResultBackBean> searchResultBackBeans = new ArrayList<SearchResultBackBean>();
		
		for (ISearchResult searchResult : searchResults) {

			SearchResultBackBean searchResultBackBean = new SearchResultBackBean();
			searchResultBackBean.setWord(searchResult.getName());
			searchResultBackBean.setId(searchResult.getId());
			searchResultBackBean.setDescription(searchResult.getDescription());
			searchResultBackBeans.add(searchResultBackBean);
		}
		
		return searchResultBackBeans.toArray(new SearchResultBackBean[searchResultBackBeans.size()]);
	}

}
