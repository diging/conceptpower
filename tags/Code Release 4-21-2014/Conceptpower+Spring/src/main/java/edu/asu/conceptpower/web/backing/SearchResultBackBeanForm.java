package edu.asu.conceptpower.web.backing;

import java.util.List;

import org.springframework.stereotype.Service;


/**
 * this class holds the list of the searchresultbackbean items and used in the
 * controller as a modelattribute
 * 
 * @author rohit pendbhaje
 * 
 */
@Service
public class SearchResultBackBeanForm {

	private List<SearchResultBackBean> searchResultList;

	public List<SearchResultBackBean> getSearchResultList() {
		return searchResultList;
	}

	public void setSearchResultList(List<SearchResultBackBean> searchResultList) {
		this.searchResultList = searchResultList;
	}
}
