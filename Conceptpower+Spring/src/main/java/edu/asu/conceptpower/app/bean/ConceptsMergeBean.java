package edu.asu.conceptpower.app.bean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.asu.conceptpower.core.ConceptType;

public class ConceptsMergeBean {

    public List<String> conceptIds;
    private Map<String, String> posMap = new LinkedHashMap<String, String>();
    private String word;
    private String selectedPosValue;
    private String selectedListName;
    private String conceptListValue;
    private Set conceptList;
    private String description;
    private String synonymsids;
    private String conceptType;
    private Set conceptTypeIdList;
    private String equals;
    private String similar;
    private String selectedTypeId;
    private String selectedTypeName;
    private ConceptType[] types;
    private String selectedConceptId;

    public Map<String, String> getPosMap() {
        return posMap;
    }

    public void setPosMap(Map<String, String> posMap) {
        this.posMap = posMap;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSelectedPosValue() {
        return selectedPosValue;
    }

    public void setSelectedPosValue(String selectedPosValue) {
        this.selectedPosValue = selectedPosValue;
    }

    public String getSelectedListName() {
        return selectedListName;
    }

    public void setSelectedListName(String selectedListName) {
        this.selectedListName = selectedListName;
    }

    public String getConceptListValue() {
        return conceptListValue;
    }

    public void setConceptListValue(String conceptListValue) {
        this.conceptListValue = conceptListValue;
    }

    public Set getConceptList() {
        return conceptList;
    }

    public void setConceptList(Set conceptList) {
        this.conceptList = conceptList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSynonymsids() {
        return synonymsids;
    }

    public void setSynonymsids(String synonymsids) {
        this.synonymsids = synonymsids;
    }

    public String getConceptType() {
        return conceptType;
    }

    public void setConceptType(String conceptType) {
        this.conceptType = conceptType;
    }

    public Set getConceptTypeIdList() {
        return conceptTypeIdList;
    }

    public void setConceptTypeIdList(Set conceptTypeIdList) {
        this.conceptTypeIdList = conceptTypeIdList;
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

    public ConceptType[] getTypes() {
        return types;
    }

    public void setTypes(ConceptType[] types) {
        this.types = types;
    }

    public String getSelectedConceptId() {
        return selectedConceptId;
    }

    public void setSelectedConceptId(String selectedConceptId) {
        this.selectedConceptId = selectedConceptId;
    }

    public String getWordnetIds() {
        return wordnetIds;
    }

    public void setWordnetIds(String wordnetIds) {
        this.wordnetIds = wordnetIds;
    }

    private String wordnetIds;

    public List<String> getConceptIds() {
        return conceptIds;
    }

    public void setConceptIds(List<String> conceptIds) {
        this.conceptIds = conceptIds;
    }

}
