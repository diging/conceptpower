package edu.asu.conceptpower.app.wrapper;

import java.io.Serializable;
import java.util.List;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ReviewRequest;
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

    // This field denotes if there is an error in fetching the concept. If it is
    // set true then error msg is displayed on the screen
    private boolean error;
    private String errorMsg;

    private String creatorId;
    private String lastModifiedEvent;

    private ReviewRequest reviewRequest;

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

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
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

    public ReviewRequest getReviewRequest() {
        return reviewRequest;
    }

    public void setReviewRequest(ReviewRequest reviewRequest) {
        this.reviewRequest = reviewRequest;
    }

    
}
