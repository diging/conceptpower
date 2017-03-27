package edu.asu.conceptpower.rest.msg.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class is used for serializing the concept entry details. This class will
 * be serialized using Jackson
 * 
 * @author karthikeyanmohan
 *
 */
@JsonInclude(Include.NON_NULL)
public class ConceptEntryMessage {

    private String id;

    @JsonProperty("concept_uri")
    private String conceptUri;

    private String lemma;
    private String pos;
    private String description;
    private String conceptList;

    @JsonProperty("creator_id")
    private String creatorId;

    @JsonProperty("equal_to")
    private String equalTo;

    @JsonProperty("modified_by")
    private String modifiedBy;

    @JsonProperty("similar_to")
    private String similarTo;

    @JsonProperty("synonym_ids")
    private String synonymIds;

    private ConceptTypeMessage type;

    private boolean deleted;

    @JsonProperty("wordnet_id")
    private String wordnetId;

    @JsonProperty("alternativeIds")
    private List<AlternativeId> alternativeIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConceptList() {
        return conceptList;
    }

    public void setConceptList(String conceptList) {
        this.conceptList = conceptList;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getEqualTo() {
        return equalTo;
    }

    public void setEqualTo(String equalTo) {
        this.equalTo = equalTo;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getSimilarTo() {
        return similarTo;
    }

    public void setSimilarTo(String similarTo) {
        this.similarTo = similarTo;
    }

    public String getSynonymIds() {
        return synonymIds;
    }

    public void setSynonymIds(String synonymIds) {
        this.synonymIds = synonymIds;
    }

    public ConceptTypeMessage getType() {
        return type;
    }

    public void setType(ConceptTypeMessage type) {
        this.type = type;
    }

    public String getWordnetId() {
        return wordnetId;
    }

    public void setWordnetId(String wordnetId) {
        this.wordnetId = wordnetId;
    }

    public List<AlternativeId> getAlternativeIds() {
        return alternativeIds;
    }

    public void setAlternativeIds(List<AlternativeId> alternativeIds) {
        this.alternativeIds = alternativeIds;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getConceptUri() {
        return conceptUri;
    }

    public void setConceptUri(String conceptUri) {
        this.conceptUri = conceptUri;
    }

}
