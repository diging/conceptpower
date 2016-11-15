package edu.asu.conceptpower.web.backing;

import java.util.HashMap;
import java.util.Map;

/**
 * this class is used as a backing bean for the search results retrieved through
 * various services like viaf, conceptpower in the user profile page
 * 
 * @author rohit pendbhaje
 * 
 */

public class SearchResultBackBean {

	//@SearchField(fieldName = SearchFieldNames.WORD)
	private String word;

	//@SearchField(fieldName = SearchFieldNames.ID)
	private String id;

	//@SearchField(fieldName = SearchFieldNames.DESCRIPTION)
	private String description;

	//@SearchField(fieldName = SearchFieldNames.ISCHECKED)
	private boolean isChecked = false;
	
	public Map<Object, Object> toMap() {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("word", word);
		map.put("id", id);
		map.put("description", description);
		map.put("isChecked", isChecked);
		return map;
	}

	public boolean getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
