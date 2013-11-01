package edu.asu.conceptpower.core;

import java.io.Serializable;

import edu.asu.conceptpower.reflect.SearchField;
import edu.asu.conceptpower.rest.SearchFieldNames;

/**
 * Search fields
 * @author Julia Damerow
 *
 */
public class ConceptEntry implements Serializable {

	private static final long serialVersionUID = 4569090620671054560L;
	
	private String id;
	private String wordnetId;
	
	@SearchField(fieldName = SearchFieldNames.WORD)
	private String word;
	
	@SearchField(fieldName = SearchFieldNames.DESCRIPTION)
	private String description;
	
	@SearchField(fieldName = SearchFieldNames.POS)
	private String pos;
	
	@SearchField(fieldName = SearchFieldNames.CONCEPT_LIST)
	private String conceptList;
	
	@SearchField(fieldName = SearchFieldNames.TYPE_ID)
	private String typeId;
	
	@SearchField(fieldName = SearchFieldNames.EQUALS_TO)
	private String equalTo;
	
	@SearchField(fieldName = SearchFieldNames.SIMILAR_TO)
	private String similarTo;
	
	private String synonymIds;
	private String synsetIds;
	private String narrows;
	private String broadens;
	
	@SearchField(fieldName = SearchFieldNames.CREATOR)
	private String creatorId;
	
	@SearchField(fieldName = SearchFieldNames.MODIFIED)
	private String modified;
	
	private boolean isDeleted;
	
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public String getSynonymIds() {
		return synonymIds;
	}
	public void setSynonymIds(String synonymIds) {
		this.synonymIds = synonymIds;
	}
	public String getNarrows() {
		return narrows;
	}
	public void setNarrows(String narrows) {
		this.narrows = narrows;
	}
	public String getBroadens() {
		return broadens;
	}
	public void setBroadens(String broadens) {
		this.broadens = broadens;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getEqualTo() {
		return equalTo;
	}
	public void setEqualTo(String equalTo) {
		this.equalTo = equalTo;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWordnetId() {
		return wordnetId;
	}
	public void setWordnetId(String wordnetId) {
		this.wordnetId = wordnetId;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public String getConceptList() {
		return conceptList;
	}
	public void setConceptList(String conceptList) {
		this.conceptList = conceptList;
	}
	
	public String getSimilarTo() {
		return similarTo;
	}
	public void setSimilarTo(String similarTo) {
		this.similarTo = similarTo;
	}
	public String getSynsetIds() {
		return synsetIds;
	}
	public void setSynsetIds(String synsetIds) {
		this.synsetIds = synsetIds;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	public String getModified() {
		return modified;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
}
