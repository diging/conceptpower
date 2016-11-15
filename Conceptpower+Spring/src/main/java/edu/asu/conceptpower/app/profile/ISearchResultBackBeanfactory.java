package edu.asu.conceptpower.app.profile;

import edu.asu.conceptpower.web.backing.SearchResultBackBean;

/**
 * this interface has method which is used for creating new object of
 * SearchResultBackBean class.
 * 
 * @author rohit pendbhaje
 * 
 */
public interface ISearchResultBackBeanfactory {

	/**
	 * this method creates object of SearchresultBackBean class
	 * 
	 * @return object of SearchResultBackBean class
	 */
	public SearchResultBackBean createSearchResultBackBeanObject();

}
