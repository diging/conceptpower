package edu.asu.conceptpower.servlet.wrapper;

import java.io.Serializable;
import java.util.List;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.users.User;

/**
 * This class describes the concept entry wrapper in concept power. It provides
 * properties which are not available for the concept entries to be wrapped
 * 
 * @author Julia Damerow
 * 
 */
public class ConceptEntryWrapper implements Serializable {

	private static final long serialVersionUID = -4261304897583134670L;
	private ConceptEntry entry;
	private List<ConceptEntry> synonyms;
	private ConceptType type;
	private User creator;
	private List<ConceptEntry> wrappedWordnetEntries;
	private String description;
	private String uri;
	
	
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

	public void setWrappedWordnetEntries(
			List<ConceptEntry> wrappedWordnetEntries) {
		this.wrappedWordnetEntries = wrappedWordnetEntries;
	}

	public List<ConceptEntry> getWrappedWordnetEntries() {
		return wrappedWordnetEntries;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description.replace("\n", "<br/>");
	}

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
