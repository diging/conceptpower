package edu.asu.conceptpower.web;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.profile.ISearchResult;
import edu.asu.conceptpower.service.impl.AuthorityFileSearch;
import edu.asu.conceptpower.web.backing.SearchResultBackBean;

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
	public @ResponseBody ResponseEntity<String> serviceSearchForConcept(
			@RequestParam("serviceterm") String serviceterm,
			@RequestParam("serviceid") String serviceid) {

		List<ISearchResult> searchResults = authorityFileSearch
				.getSearchResultBackBeanList(serviceid, serviceterm);
		
		List<SearchResultBackBean> searchResultBackBeans = new ArrayList<SearchResultBackBean>();
		
		JSONArray json = new JSONArray();
		for (ISearchResult searchResult : searchResults) {

			SearchResultBackBean searchResultBackBean = new SearchResultBackBean();
			searchResultBackBean.setWord(searchResult.getName());
			searchResultBackBean.setId(searchResult.getId());
			searchResultBackBean.setDescription(searchResult.getDescription());
			searchResultBackBeans.add(searchResultBackBean);
			json.put(new JSONObject(searchResultBackBean.toMap()));
		}
		
		return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
	}

}
