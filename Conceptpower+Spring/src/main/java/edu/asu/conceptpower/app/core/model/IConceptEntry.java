package edu.asu.conceptpower.app.core.model;

import java.util.List;
import java.util.Set;
import edu.asu.conceptpower.servlet.core.ChangeEvent;

public interface IConceptEntry{
    public abstract String getCreatorId();
    
    public abstract void setCreatorId(String creatorId);
    /**
     * A string containing the ids of other conceptpower entries that are
     * synonyms for an entry. The synonym ids are speparated by
     * {@link edu.asu.conceptpower.app.core.Constants.SYNONYM_SEPARATOR}.
     */
    public abstract String getSynonymIds();

    public abstract void setSynonymIds(String synonymIds);

    /**
     * A string containing the ids of other conceptpower entries that narrow
     * this entry. This field is currently not used.
     */
    public abstract String getNarrows();

    public abstract void setNarrows(String narrows);

    /**
     * A string containing the ids of other conceptpower entries that broadens
     * this entry. This field is currently not used.
     */
    public abstract String getBroadens();

    public abstract void setBroadens(String broadens);

    /**
     * Id of the {@link Type} a concept has.
     */
    public abstract String getTypeId();

    public abstract void setTypeId(String typeId);

    /**
     * A string containing URIs of authority file records or control vocabulary
     * entries that are equal to an entry.
     * 
     */
    public abstract String getEqualTo();

    /**
     * If a slash "/" is present at the end of the equal, this method removes
     * the slash from end of equal and assigns the value to equalTo. For example
     * if equal="http://viaf.org/viaf/110275452/", then this method assigns
     * "http://viaf.org/viaf/110275452" to equalTo
     * 
     * @param equalTo
     */
    public abstract void setEqualTo(String equal);

    /**
     * Id of an entry.
     */
    public abstract String getId();

    public abstract void setId(String id);

    /**
     * If an entry represents an entry that exists in wordnet, this field
     * contains the id an entry has in Wordnet. If an entry represents several
     * entries in Wordnet this field contains a list of Wordnet ids separated by
     * {@link Constants.CONCEPT_SEPARATOR}.
     */
    public abstract String getWordnetId();

    public abstract void setWordnetId(String wordnetId);

    /**
     * A term describing the concept (e.g. horse).
     */
    public abstract String getWord();

    public abstract void setWord(String word);

    /**
     * A description giving a broad idea what concept is referred to. This
     * description is meant to be very broad (e.g. a mammal belonging to one of
     * two extant subspecies of Equus ferus).
     */
    public abstract String getDescription();

    public abstract void setDescription(String description);

    /**
     * Part of speech of (noun, verb, adjective, adverb, other)
     */
    public abstract String getPos();

    public abstract void setPos(String pos);

    /**
     * A list the concept belongs to.
     */
    public abstract String getConceptList();

    public abstract void setConceptList(String conceptList);

    /**
     * A string containing URIs of authority file records or control vocabulary
     * entries that are similar to an entry.
     * 
     */
    public abstract String getSimilarTo();

    /**
     * If a slash "/" is present at the end of the similar, this method removes
     * the slash from end of similar and assigns the value to similarTo. For
     * example if similar="http://viaf.org/viaf/110275452/", then this method
     * assigns similarTo to "http://viaf.org/viaf/110275452"
     * 
     * @param similarTo
     */
    public abstract void setSimilarTo(String similar);

    /**
     * A string containing the ids of the synsets an entry belongs to. This
     * field is currently not used.
     */
    public abstract String getSynsetIds();

    public abstract void setSynsetIds(String synsetIds);

    public abstract void setModified(String modified);

    /**
     * A string containing a string describing who modified a concept and when.
     */
    public abstract String getModified();

    /**
     * This function return true if the entry was deleted by a user and false if
     * it was not deleted.
     */
    public abstract boolean isDeleted();
    
    public abstract void setDeleted(boolean isDeleted);

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

    public abstract List<ChangeEvent> getChangeEvents();

    /**
     * This method add the changeevent to the end of the list. The first element
     * will always be a creator
     * 
     * @param event
     */
    public abstract void addNewChangeEvent(ChangeEvent event);

    public abstract String getModifiedUser();

    public abstract void setModifiedUser(String modifiedUser);

    public abstract Set<String> getAlternativeIds();

    public abstract void setAlternativeIds(Set<String> alternativeIds);

    public abstract String getMergedIds();

    public abstract void setMergedIds(String mergedIds);
}