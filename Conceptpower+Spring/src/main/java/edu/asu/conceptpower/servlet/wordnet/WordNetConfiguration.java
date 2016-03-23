package edu.asu.conceptpower.servlet.wordnet;

import java.io.Serializable;



public class WordNetConfiguration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1738852278788455406L;
	
	private String wordnetPath;
	private String dictFolder;
	
	public String getWordnetPath() {
		return wordnetPath;
	}
	public void setWordnetPath(String wordnetPath) {
		this.wordnetPath = wordnetPath;
	}
	public String getDictFolder() {
		return dictFolder;
	}
	public void setDictFolder(String dictFolder) {
		this.dictFolder = dictFolder;
	}
	
	
}
