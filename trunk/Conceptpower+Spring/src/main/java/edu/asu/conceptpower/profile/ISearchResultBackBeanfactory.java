package edu.asu.conceptpower.profile;

import edu.asu.conceptpower.web.profile.impl.SearchResultBackBean;

/**
 * this interface is used for creating SearchResultBackBean new object
 * 
 * methods:
 * createSearchResultBackBeanObject() : creates object of SearchResultBackBean
 * 
 * @author rohit pendbhaje
 *
 */
public interface ISearchResultBackBeanfactory {
	
	public SearchResultBackBean createSearchResultBackBeanObject();

}
