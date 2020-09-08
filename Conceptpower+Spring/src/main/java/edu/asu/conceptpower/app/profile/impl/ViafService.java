package edu.asu.conceptpower.app.profile.impl;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import edu.asu.conceptpower.app.jaxb.viaf.Item;
import edu.asu.conceptpower.app.profile.ISearchResult;
import edu.asu.conceptpower.app.profile.ISearchResultFactory;
import edu.asu.conceptpower.app.profile.IService;

/**
 * this class contains methods which connects quadriga to viaf authority service
 * and searches the required term in it.
 * 
 * @author rohit pendbhaje
 */

@Service
public class ViafService implements IService {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ISearchResult searchResult;

	@Autowired
	private ISearchResultFactory searchResultFactory;

	private String serviceid;
	private String name;

	@Autowired
	@Qualifier("viafURL")
	private String viafURL;

	@Autowired
	@Qualifier("searchViafURLPath")
	private String searchViafURLPath;

	@Autowired
	@Qualifier("searchViafURLPath1")
	private String searchViafURLPath1;

	@Autowired
	@Qualifier("searchViafURLPath2")
	private String searchViafURLPath2;
	
	@Inject
	@Named("restTemplateViaf")
	private RestTemplate restTemplate;

	@Override
	public void setServiceId(String serviceid) {
		this.serviceid = serviceid;

	}

	@Override
	public String getServiceId() {

		return "edu.asu.viaf";
	}

	@Override
	public void setName(String name) {
		this.name = name;

	}

	@Override
	public String getName() {

		return "Viaf";
	}

	/**
	 * searches results in the viaf authority service and gives back results to
	 * quadriga
	 * 
	 * @param item
	 *            term entered by user
	 * @param startindex
	 *            index of the start of the search result viaf service
	 * 
	 * @return list of searchresults retrieved from viaf service
	 * @author rohit pendbhaje
	 * 
	 */
	@Override
	public List<ISearchResult> search(String word) {

		String startIndex = "1";
        List<ISearchResult> searchResults = new ArrayList<ISearchResult>();
		String fullUrl;

		try {
             fullUrl = viafURL.trim() + searchViafURLPath.trim() + "%20" + URLEncoder.encode(word.trim(), "UTF-8")
             + searchViafURLPath1.trim() + startIndex.trim()+ searchViafURLPath2.trim();
       		} catch (UnsupportedEncodingException e1) {
            	      logger.error("Error in URL Encoding.", e1);
            	      return searchResults;
        	}
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_RSS_XML));

		HttpEntity<ViafReply> entity = new HttpEntity<ViafReply>(headers);
		ResponseEntity<ViafReply> reply ;

	        try {
           	      reply = restTemplate.exchange(new URI(fullUrl),HttpMethod.GET, entity, ViafReply.class);
                } catch (RestClientException | URISyntaxException e) {
                      logger.error("Error during contacting VIAF.", e);
                      return searchResults;
                }
		ViafReply rep = reply.getBody();
		
		List<Item> items = null;
		items = rep.getChannel().getItems();

		if (items != null) {
			for (Item i : items) {
				ISearchResult searchResult = searchResultFactory
						.getSearchResultObject();
				searchResult.setDescription(i.getPubDate());
				searchResult.setId(i.getLink());
				searchResult.setName(i.getTitle());
				searchResults.add(searchResult);
			}
		}
		return searchResults;

	}

}
