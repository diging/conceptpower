package edu.asu.conceptpower.servlet.wrapper;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.servlet.core.ChangeEvent;
import edu.asu.conceptpower.servlet.core.ChangeEventConstants;
import edu.asu.conceptpower.servlet.core.ConceptEntry;
import edu.asu.conceptpower.servlet.core.ConceptType;
import edu.asu.conceptpower.servlet.users.User;

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
    private String creatorId;
    private String lastModifiedEvent;

    public ConceptEntryWrapper(ConceptEntry entry) {
        this.entry = entry;
        List<ChangeEvent> changeEvents = entry.getChangeEvents();
        Collections.sort(changeEvents);
        // Since the list is sorted first element will be Creation event. If not
        // then concept needs to be created before change events modification.
        // In that case fetch from the existing entry. getCreator()
        if (changeEvents.size() > 0
                && changeEvents.get(0).getType().equalsIgnoreCase(ChangeEventConstants.CREATION)) {
            this.creatorId = changeEvents.get(0).getUserName();
        } else {
            this.creatorId = entry.getCreatorId();
        }
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

    public void setWrappedWordnetEntries(List<ConceptEntry> wrappedWordnetEntries) {
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

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getLastModifiedEvent() {
        return lastModifiedEvent;
    }

    public void setLastModifiedEvent(String lastModifiedEvent) {
        this.lastModifiedEvent = lastModifiedEvent;
    }
}
