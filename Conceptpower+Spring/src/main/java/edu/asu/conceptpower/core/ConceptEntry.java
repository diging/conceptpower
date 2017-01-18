package edu.asu.conceptpower.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;

import edu.asu.conceptpower.app.constants.LuceneFieldNames;
import edu.asu.conceptpower.app.constants.SearchFieldNames;
import edu.asu.conceptpower.app.reflect.LuceneField;
import edu.asu.conceptpower.app.reflect.SearchField;
import edu.asu.conceptpower.servlet.core.ChangeEvent;
import edu.asu.conceptpower.servlet.core.ChangeEvent.ChangeEventTypes;

/**
 * This class represents one entry in the authority file.
 * 
 * @author Julia Damerow
 * 
 */
@Entity
public class ConceptEntry implements Serializable {

    private static final long serialVersionUID = 4569090620671054560L;

    @Id
    @SearchField(fieldName = SearchFieldNames.ID)
    @LuceneField(lucenefieldName = LuceneFieldNames.ID, isIndexable = true)
    private String id;

    @SearchField(fieldName = SearchFieldNames.WORDNETID)
    @LuceneField(lucenefieldName = LuceneFieldNames.WORDNETID, isIndexable = true)
    private String wordnetId;

    @SearchField(fieldName = SearchFieldNames.WORD)
    @LuceneField(lucenefieldName = LuceneFieldNames.WORD, isIndexable = true)
    private String word;

    @SearchField(fieldName = SearchFieldNames.DESCRIPTION)
    @LuceneField(lucenefieldName = LuceneFieldNames.DESCRIPTION, isIndexable = true)
    private String description;

    @SearchField(fieldName = SearchFieldNames.POS)
    @LuceneField(lucenefieldName = LuceneFieldNames.POS, isIndexable = true)
    private String pos;

    @SearchField(fieldName = SearchFieldNames.CONCEPT_LIST)
    @LuceneField(lucenefieldName = LuceneFieldNames.CONCEPT_LIST, isIndexable = false)
    private String conceptList;

    @SearchField(fieldName = SearchFieldNames.TYPE_ID)
    @LuceneField(lucenefieldName = LuceneFieldNames.TYPE_ID, isIndexable = false)
    private String typeId;

    @SearchField(fieldName = SearchFieldNames.EQUALS_TO)
    @LuceneField(lucenefieldName = LuceneFieldNames.EQUALS_TO, isIndexable = true)
    private String equalTo;

    @SearchField(fieldName = SearchFieldNames.SIMILAR_TO)
    @LuceneField(lucenefieldName = LuceneFieldNames.SIMILAR_TO, isIndexable = true)
    private String similarTo;

    @SearchField(fieldName=SearchFieldNames.SYNONYM_ID)
    @LuceneField(lucenefieldName = LuceneFieldNames.SYNONYMID, isIndexable = true)
    private String synonymIds;

    private String synsetIds;

    private String narrows;

    private String broadens;

    @SearchField(fieldName = SearchFieldNames.CREATOR)
    @LuceneField(lucenefieldName = LuceneFieldNames.CREATOR, isIndexable = false)
    private String creatorId;

    @SearchField(fieldName = SearchFieldNames.MODIFIED)
    @LuceneField(lucenefieldName = LuceneFieldNames.MODIFIED, isIndexable = false)
    private String modified;

    private boolean isDeleted;
    
    private String modifiedUser;

    private List<ChangeEvent> changeEvents = new ArrayList<ChangeEvent>();

    @LuceneField(lucenefieldName = LuceneFieldNames.ALTERNATIVE_IDS, isIndexable = false)
    private Set<String> alternativeIds = new HashSet<>();

    public ConceptEntry() {
    }

    public ConceptEntry(String id, String word, String description) {
        this.id = id;
        this.word = word;
        this.description = description;
    }

    /**
     * A string containing the id of the user who created an entry.
     * 
     * First check if creator is in change event list. If so fetch it Else fetch
     * it from old db
     * 
     * @return the id of the user who created an entry
     */
    public String getCreatorId() {
        // This check has been introduced to make sure the existing concepts
        // work fine. For existing concepts changeevents will be null in D/B. So
        // if changevent is null in DB fetch creatorId directly
        if (this.changeEvents != null && this.changeEvents.size() > 0) {
            Collections.sort(this.changeEvents);
            // Since the list is sorted, the first event will be a creation
            // event. If the first event is not creation event, then it means
            // some existing concept has been modified with this new change. So
            // changevents will contain only the modified user id. In that case
            // fetch from the creatorId itself as per the old design
            if (ChangeEventTypes.CREATION == changeEvents.get(0).getType()) {
                return changeEvents.get(0).getUserName();
            }
        }
        return creatorId;

    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * A string containing the ids of other conceptpower entries that are
     * synonyms for an entry. The synonym ids are speparated by
     * {@link edu.asu.conceptpower.app.core.Constants.SYNONYM_SEPARATOR}.
     */
    public String getSynonymIds() {
        return synonymIds;
    }

    public void setSynonymIds(String synonymIds) {
        this.synonymIds = synonymIds;
    }

    /**
     * A string containing the ids of other conceptpower entries that narrow
     * this entry. This field is currently not used.
     */
    public String getNarrows() {
        return narrows;
    }

    public void setNarrows(String narrows) {
        this.narrows = narrows;
    }

    /**
     * A string containing the ids of other conceptpower entries that broadens
     * this entry. This field is currently not used.
     */
    public String getBroadens() {
        return broadens;
    }

    public void setBroadens(String broadens) {
        this.broadens = broadens;
    }

    /**
     * Id of the {@link Type} a concept has.
     */
    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    /**
     * A string containing URIs of authority file records or control vocabulary
     * entries that are equal to an entry.
     */
    public String getEqualTo() {
        return equalTo;
    }

    public void setEqualTo(String equalTo) {
        this.equalTo = equalTo;
    }

    /**
     * Id of an entry.
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * If an entry represents an entry that exists in wordnet, this field
     * contains the id an entry has in Wordnet. If an entry represents several
     * entries in Wordnet this field contains a list of Wordnet ids separated by
     * {@link Constants.CONCEPT_SEPARATOR}.
     */
    public String getWordnetId() {
        return wordnetId;
    }

    public void setWordnetId(String wordnetId) {
        this.wordnetId = wordnetId;
    }

    /**
     * A term describing the concept (e.g. horse).
     */
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    /**
     * A description giving a broad idea what concept is referred to. This
     * description is meant to be very broad (e.g. a mammal belonging to one of
     * two extant subspecies of Equus ferus).
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Part of speech of (noun, verb, adjective, adverb, other)
     */
    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    /**
     * A list the concept belongs to.
     */
    public String getConceptList() {
        return conceptList;
    }

    public void setConceptList(String conceptList) {
        this.conceptList = conceptList;
    }

    /**
     * A string containing URIs of authority file records or control vocabulary
     * entries that are similar to an entry.
     */
    public String getSimilarTo() {
        return similarTo;
    }

    public void setSimilarTo(String similarTo) {
        this.similarTo = similarTo;
    }

    /**
     * A string containing the ids of the synsets an entry belongs to. This
     * field is currently not used.
     */
    public String getSynsetIds() {
        return synsetIds;
    }

    public void setSynsetIds(String synsetIds) {
        this.synsetIds = synsetIds;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    /**
     * A string containing a string describing who modified a concept and when.
     */
    public String getModified() {
        if (this.changeEvents != null && this.changeEvents.size() > 0) {
            Collections.sort(this.changeEvents);
            if (ChangeEventTypes.CREATION == changeEvents.get(changeEvents.size() - 1).getType()) {
                return changeEvents.get(0).getUserName();
            }
        }
        return modified;
    }

    /**
     * This function return true if the entry was deleted by a user and false if
     * it was not deleted.
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((conceptList == null) ? 0 : conceptList.hashCode());
        result = prime * result + ((creatorId == null) ? 0 : creatorId.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((pos == null) ? 0 : pos.hashCode());
        result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
        result = prime * result + ((word == null) ? 0 : word.hashCode());
        result = prime * result + ((wordnetId == null) ? 0 : wordnetId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ConceptEntry other = (ConceptEntry) obj;
        if (conceptList == null) {
            if (other.conceptList != null)
                return false;
        } else if (!conceptList.equals(other.conceptList))
            return false;
        if (creatorId == null) {
            if (other.creatorId != null)
                return false;
        } else if (!creatorId.equals(other.creatorId))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (pos == null) {
            if (other.pos != null)
                return false;
        } else if (!pos.equals(other.pos))
            return false;
        if (typeId == null) {
            if (other.typeId != null)
                return false;
        } else if (!typeId.equals(other.typeId))
            return false;
        if (word == null) {
            if (other.word != null)
                return false;
        } else if (!word.equals(other.word))
            return false;
        if (wordnetId == null) {
            if (other.wordnetId != null)
                return false;
        } else if (!wordnetId.equals(other.wordnetId))
            return false;
        return true;
    }

    public List<ChangeEvent> getChangeEvents() {
        // Creates a copy of changeevent
        if (this.changeEvents != null) {
            return new ArrayList<>(this.changeEvents);
        }
        return null;
    }

    /**
     * This method add the changeevent to the end of the list. The first element
     * will always be a creator
     * 
     * @param event
     */
    public void addNewChangeEvent(ChangeEvent event) {

        if (changeEvents == null) {
            this.changeEvents = new ArrayList<ChangeEvent>();
        }
        // Appends to the end of the list
        this.changeEvents.add(event);
    }

    public String getModifiedUser() {
        return modifiedUser;
    }

    public void setModifiedUser(String modifiedUser) {
        this.modifiedUser = modifiedUser;
    }

    public Set<String> getAlternativeIds() {
        return alternativeIds;
    }

    public void setAlternativeIds(Set<String> alternativeIds) {
        this.alternativeIds = alternativeIds;
    }
}