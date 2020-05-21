//package edu.asu.conceptpower.app.core.model.impl;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import javax.persistence.Entity;
//import javax.persistence.Id;
//
//import edu.asu.conceptpower.app.core.model.IConceptEntry;
//import edu.asu.conceptpower.servlet.core.ChangeEvent;
//import edu.asu.conceptpower.servlet.core.ChangeEvent.ChangeEventTypes;
//
///**
// * 
// * @author Keerthivasan
// * 
// */
//@Entity
//public class ConceptEntry implements IConceptEntry, Serializable{
//
//    /**
//     * 
//     */
//    private static final long serialVersionUID = -2996311266459722841L;
//
//    @Id
//    private String id;
//
//    private String wordnetId;
//
//    private String word;
//
//    private String description;
//
//    private String pos;
//
//    private String conceptList;
//
//    private String typeId;
//
//    private String equalTo;
//
//    private String similarTo;
//
//    private String synonymIds;
//
//    private String synsetIds;
//
//    private String narrows;
//
//    private String broadens;
//
//    private String creatorId;
//
//    private String modified;
//
//    private String mergedIds;
//
//    private boolean isDeleted;
//    
//    private String modifiedUser;
//
//    private List<ChangeEvent> changeEvents = new ArrayList<ChangeEvent>();
//
//    private Set<String> alternativeIds = new HashSet<>();
//
//    public ConceptEntry() {
//    }
//
//    public ConceptEntry(String id, String word, String description) {
//        this.id = id;
//        this.word = word;
//        this.description = description;
//    }
//
//    /**
//     * A string containing the id of the user who created an entry.
//     * 
//     * First check if creator is in change event list. If so fetch it Else fetch
//     * it from old db
//     * 
//     * @return the id of the user who created an entry
//     */
//    
//    @Override
//    public String getCreatorId() {
//        // This check has been introduced to make sure the existing concepts
//        // work fine. For existing concepts changeevents will be null in D/B. So
//        // if changevent is null in DB fetch creatorId directly
//        if (this.changeEvents != null && this.changeEvents.size() > 0) {
//            Collections.sort(this.changeEvents);
//            // Since the list is sorted, the first event will be a creation
//            // event. If the first event is not creation event, then it means
//            // some existing concept has been modified with this new change. So
//            // changevents will contain only the modified user id. In that case
//            // fetch from the creatorId itself as per the old design
//            if (ChangeEventTypes.CREATION == changeEvents.get(0).getType()) {
//                return changeEvents.get(0).getUserName();
//            }
//        }
//        return creatorId;
//
//    }
//
//    @Override
//    public void setCreatorId(String creatorId) {
//        this.creatorId = creatorId;
//    }
//
//    /**
//     * A string containing the ids of other conceptpower entries that are
//     * synonyms for an entry. The synonym ids are speparated by
//     * {@link edu.asu.conceptpower.app.core.Constants.SYNONYM_SEPARATOR}.
//     */
//    
//    @Override
//    public String getSynonymIds() {
//        return synonymIds;
//    }
//
//    @Override
//    public void setSynonymIds(String synonymIds) {
//        this.synonymIds = synonymIds;
//    }
//
//    /**
//     * A string containing the ids of other conceptpower entries that narrow
//     * this entry. This field is currently not used.
//     */
//    
//    @Override
//    public String getNarrows() {
//        return narrows;
//    }
//    
//    @Override
//    public void setNarrows(String narrows) {
//        this.narrows = narrows;
//    }
//
//    /**
//     * A string containing the ids of other conceptpower entries that broadens
//     * this entry. This field is currently not used.
//     */
//    
//    @Override
//    public String getBroadens() {
//        return broadens;
//    }
//
//    @Override
//    public void setBroadens(String broadens) {
//        this.broadens = broadens;
//    }
//
//    /**
//     * Id of the {@link Type} a concept has.
//     */
//    
//    @Override
//    public String getTypeId() {
//        return typeId;
//    }
//
//    @Override
//    public void setTypeId(String typeId) {
//        this.typeId = typeId;
//    }
//
//    /**
//     * A string containing URIs of authority file records or control vocabulary
//     * entries that are equal to an entry.
//     * 
//     */
//    
//    @Override
//    public String getEqualTo() {
//        return equalTo;
//    }
//
//    /**
//     * If a slash "/" is present at the end of the equal, this method removes
//     * the slash from end of equal and assigns the value to equalTo. For example
//     * if equal="http://viaf.org/viaf/110275452/", then this method assigns
//     * "http://viaf.org/viaf/110275452" to equalTo
//     * 
//     * @param equalTo
//     */
//    
//    @Override
//    public void setEqualTo(String equal) {
//        if (equal != null) {
//            List<String> equalsTo = Arrays.asList(equal.split(","));
//            this.equalTo = equalsTo.stream().map(i -> removeTrailingBackSlashAndTrim(i))
//                    .collect(Collectors.joining(","));
//        } else {
//            this.equalTo = equal;
//        }
//    }
//
//    /**
//     * Id of an entry.
//     */
//    
//    @Override
//    public String getId() {
//        return id;
//    }
//
//    @Override
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    /**
//     * If an entry represents an entry that exists in wordnet, this field
//     * contains the id an entry has in Wordnet. If an entry represents several
//     * entries in Wordnet this field contains a list of Wordnet ids separated by
//     * {@link Constants.CONCEPT_SEPARATOR}.
//     */
//    
//    @Override
//    public String getWordnetId() {
//        return wordnetId;
//    }
//
//    @Override
//    public void setWordnetId(String wordnetId) {
//        this.wordnetId = wordnetId;
//    }
//
//    /**
//     * A term describing the concept (e.g. horse).
//     */
//    
//    @Override
//    public String getWord() {
//        return word;
//    }
//
//    @Override
//    public void setWord(String word) {
//        this.word = word;
//    }
//
//    /**
//     * A description giving a broad idea what concept is referred to. This
//     * description is meant to be very broad (e.g. a mammal belonging to one of
//     * two extant subspecies of Equus ferus).
//     */
//    
//    @Override
//    public String getDescription() {
//        return description;
//    }
//
//    @Override
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    /**
//     * Part of speech of (noun, verb, adjective, adverb, other)
//     */
//    
//    @Override
//    public String getPos() {
//        return pos;
//    }
//
//    @Override
//    public void setPos(String pos) {
//        this.pos = pos;
//    }
//
//    /**
//     * A list the concept belongs to.
//     */
//    
//    @Override
//    public String getConceptList() {
//        return conceptList;
//    }
//
//    @Override
//    public void setConceptList(String conceptList) {
//        this.conceptList = conceptList;
//    }
//
//    /**
//     * A string containing URIs of authority file records or control vocabulary
//     * entries that are similar to an entry.
//     * 
//     */
//    
//    @Override
//    public String getSimilarTo() {
//        return similarTo;
//    }
//
//    /**
//     * If a slash "/" is present at the end of the similar, this method removes
//     * the slash from end of similar and assigns the value to similarTo. For
//     * example if similar="http://viaf.org/viaf/110275452/", then this method
//     * assigns similarTo to "http://viaf.org/viaf/110275452"
//     * 
//     * @param similarTo
//     */
//    
//    @Override
//    public void setSimilarTo(String similar) {
//        if (similar != null) {
//            List<String> similarToList = Arrays.asList(similar.split(","));
//            this.similarTo = similarToList.stream().map(i -> removeTrailingBackSlashAndTrim(i))
//                    .collect(Collectors.joining(","));
//        } else {
//            this.similarTo = similar;
//        }
//    }
//
//    /**
//     * A string containing the ids of the synsets an entry belongs to. This
//     * field is currently not used.
//     */
//    
//    @Override
//    public String getSynsetIds() {
//        return synsetIds;
//    }
//    
//    @Override
//    public void setSynsetIds(String synsetIds) {
//        this.synsetIds = synsetIds;
//    }
//
//    @Override
//    public void setModified(String modified) {
//        this.modified = modified;
//    }
//
//    /**
//     * A string containing a string describing who modified a concept and when.
//     */
//    @Override
//    public String getModified() {
//        if (this.changeEvents != null && this.changeEvents.size() > 0) {
//            Collections.sort(this.changeEvents);
//            if (ChangeEventTypes.CREATION == changeEvents.get(changeEvents.size() - 1).getType()) {
//                return changeEvents.get(0).getUserName();
//            }
//        }
//        return modified;
//    }
//
//    /**
//     * This function return true if the entry was deleted by a user and false if
//     * it was not deleted.
//     */
//    @Override
//    public boolean isDeleted() {
//        return isDeleted;
//    }
//
//    @Override
//    public void setDeleted(boolean isDeleted) {
//        this.isDeleted = isDeleted;
//    }
//
//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + ((conceptList == null) ? 0 : conceptList.hashCode());
//        result = prime * result + ((creatorId == null) ? 0 : creatorId.hashCode());
//        result = prime * result + ((id == null) ? 0 : id.hashCode());
//        result = prime * result + ((pos == null) ? 0 : pos.hashCode());
//        result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
//        result = prime * result + ((word == null) ? 0 : word.hashCode());
//        result = prime * result + ((wordnetId == null) ? 0 : wordnetId.hashCode());
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj)
//            return true;
//        if (obj == null)
//            return false;
//        if (getClass() != obj.getClass())
//            return false;
//        ConceptEntry other = (ConceptEntry) obj;
//        if (conceptList == null) {
//            if (other.conceptList != null)
//                return false;
//        } else if (!conceptList.equals(other.conceptList))
//            return false;
//        if (creatorId == null) {
//            if (other.creatorId != null)
//                return false;
//        } else if (!creatorId.equals(other.creatorId))
//            return false;
//        if (id == null) {
//            if (other.id != null)
//                return false;
//        } else if (!id.equals(other.id))
//            return false;
//        if (pos == null) {
//            if (other.pos != null)
//                return false;
//        } else if (!pos.equals(other.pos))
//            return false;
//        if (typeId == null) {
//            if (other.typeId != null)
//                return false;
//        } else if (!typeId.equals(other.typeId))
//            return false;
//        if (word == null) {
//            if (other.word != null)
//                return false;
//        } else if (!word.equals(other.word))
//            return false;
//        if (wordnetId == null) {
//            if (other.wordnetId != null)
//                return false;
//        } else if (!wordnetId.equals(other.wordnetId))
//            return false;
//        return true;
//    }
//
//    @Override
//    public List<ChangeEvent> getChangeEvents() {
//        // Creates a copy of changeevent
//        if (this.changeEvents != null) {
//            return new ArrayList<>(this.changeEvents);
//        }
//        return null;
//    }
//
//    /**
//     * This method add the changeevent to the end of the list. The first element
//     * will always be a creator
//     * 
//     * @param event
//     */
//    @Override
//    public void addNewChangeEvent(ChangeEvent event) {
//
//        if (changeEvents == null) {
//            this.changeEvents = new ArrayList<ChangeEvent>();
//        }
//        // Appends to the end of the list
//        this.changeEvents.add(event);
//    }
//
//    @Override
//    public String getModifiedUser() {
//        return modifiedUser;
//    }
//
//    @Override
//    public void setModifiedUser(String modifiedUser) {
//        this.modifiedUser = modifiedUser;
//    }
//
//    @Override
//    public Set<String> getAlternativeIds() {
//        if (this.alternativeIds == null) {
//            this.alternativeIds = new HashSet<>();
//        }
//        return alternativeIds;
//    }
//
//    @Override
//    public void setAlternativeIds(Set<String> alternativeIds) {
//        this.alternativeIds = alternativeIds;
//    }
//
//    @Override
//    public String getMergedIds() {
//        return mergedIds;
//    }
//    
//    @Override
//    public void setMergedIds(String mergedIds) {
//        this.mergedIds = mergedIds;
//    }
//
//    private String removeTrailingBackSlashAndTrim(String val) {
//        if (val.endsWith("/")) {
//            return val.substring(0, val.length() - 1).trim();
//        }
//        return val.trim();
//    }
//    
//}