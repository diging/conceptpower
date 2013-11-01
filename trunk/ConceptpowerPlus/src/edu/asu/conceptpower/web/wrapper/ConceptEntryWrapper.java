package edu.asu.conceptpower.web.wrapper;

import java.io.Serializable;
import java.util.List;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.users.User;
import edu.asu.conceptpower.util.URICreator;

public class ConceptEntryWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4261304897583134670L;
	private ConceptEntry entry;
	private List<ConceptEntry> synonyms;
	private ConceptType type;
	private User creator;
	private ConceptEntry wrappedWordnetEntry;
	private String description;
	
	public ConceptEntryWrapper(ConceptEntry entry) {
		this.entry = entry;
	}
	
	public ConceptEntry getEntry() {
		return entry;
	}
	public void setEntry(ConceptEntry entry) {
		this.entry = entry;
	}
	public List<ConceptEntry> getSynonyms() {
		return synonyms;
	}
	public void setSynonyms(List<ConceptEntry> synonyms) {
		this.synonyms = synonyms;
	}
	public ConceptType getType() {
		return type;
	}
	public void setType(ConceptType type) {
		this.type = type;
	}
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}

	public void setWrappedWordnetEntry(ConceptEntry wrappedWordnetEntry) {
		this.wrappedWordnetEntry = wrappedWordnetEntry;
	}

	public ConceptEntry getWrappedWordnetEntry() {
		return wrappedWordnetEntry;
	}

	public String getDescription() {
		if (description == null) {
			StringBuffer sb = new StringBuffer();
			if (entry.getDescription() != null) {
				sb.append(entry.getDescription());
				sb.append("<br/><br/>");
			}
			if (wrappedWordnetEntry != null && wrappedWordnetEntry.getDescription() != null) {
				sb.append("Wordnet description:<br/>");
				sb.append("<i>" + wrappedWordnetEntry.getWord() + "</i><br/>");
				sb.append(wrappedWordnetEntry.getDescription());
			}
			
			return sb.toString();
		}
		return description;
	}

	public void setDescription(String description) {
		this.description = description.replace("\n", "<br/>");
	}

	public String getUri() {
		if (entry == null)
			return "";
		
		return URICreator.getURI(entry);
	}

	
	
}
