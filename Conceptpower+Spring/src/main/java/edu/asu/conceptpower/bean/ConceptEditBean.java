package edu.asu.conceptpower.bean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import edu.asu.conceptpower.core.ConceptType;

@Component
public class ConceptEditBean {

	private Map<String, String> possMap = new LinkedHashMap<String, String>();
	private String word;
	private String concept;
	private String selectedPosValue;
	private String selectedListName;
	private String conceptListValue;
	private List conceptList;
	private String description;
	private String synonymsids;
	private String conceptType;
	private List conceptTypeList;
	private String equals;
	private String similar;
	private String selectedTypeId;
	private String selectedTypeName;
	private ConceptType[] types;
	private String conceptId;

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public String getConceptListValue() {
		return conceptListValue;
	}

	public void setConceptListValue(String conceptListValue) {
		this.conceptListValue = conceptListValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getConceptType() {
		return conceptType;
	}

	public void setConceptType(String conceptType) {
		this.conceptType = conceptType;
	}

	public List getConceptTypeList() {
		return conceptTypeList;
	}

	public void setConceptTypeList(List conceptTypeList) {
		this.conceptTypeList = conceptTypeList;
	}

	public String getEquals() {
		return equals;
	}

	public void setEquals(String equals) {
		this.equals = equals;
	}

	public String getSimilar() {
		return similar;
	}

	public void setSimilar(String similar) {
		this.similar = similar;
	}

	public String getSelectedPosValue() {
		return selectedPosValue;
	}

	public void setSelectedPosValue(String selectedPosValue) {
		this.selectedPosValue = selectedPosValue;
	}

	public Map<String, String> getPossMap() {
		possMap.put("noun", "Nouns");
		possMap.put("verb", "Verbs");
		possMap.put("adverb", "Adverb");
		possMap.put("adjective", "Adjective");
		possMap.put("other", "Other");
		return possMap;
	}

	public String getSelectedListName() {
		return selectedListName;
	}

	public void setSelectedListName(String selectedListName) {
		this.selectedListName = selectedListName;
	}


	public String getSelectedTypeId() {
		return selectedTypeId;
	}

	public void setSelectedTypeId(String selectedTypeId) {
		this.selectedTypeId = selectedTypeId;
	}

	public String getSelectedTypeName() {
		return selectedTypeName;
	}

	public void setSelectedTypeName(String selectedTypeName) {
		this.selectedTypeName = selectedTypeName;
	}

	public String getConceptId() {
		return conceptId;
	}

	public void setConceptId(String conceptId) {
		this.conceptId = conceptId;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public List getConceptList() {
		return conceptList;
	}

	public void setConceptList(List conceptList) {
		this.conceptList = conceptList;
	}

	public void setPossMap(Map<String, String> possMap) {
		this.possMap = possMap;
	}

	public ConceptType[] getTypes() {
		return types;
	}

	public void setTypes(ConceptType[] types) {
		this.types = types;
	}

	public String getSynonymsids() {
		return synonymsids;
	}

	public void setSynonymsids(String synonymsids) {
		this.synonymsids = synonymsids;
	}

}
