package edu.asu.conceptpower.app.bean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConceptsMergeBean {

    private String selectedPosValue;
    private String selectedListName;
    private String selectedTypeId;
    private String selectedTypeName;
    private String selectedConceptId;
    private String descriptions;
    private Set<String> synonymsids = new HashSet<>();
    private Set<String> equalsValues = new HashSet<>();
    private Set<String> similarValues = new HashSet<>();
    private Set<String> wordnetIds;
    private List<String> conceptIds;
    private String word;
    private String errorMessages;

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

    public String getSelectedConceptId() {
        if (selectedConceptId == null) {
            return "";
        }
        return selectedConceptId;
    }

    public void setSelectedConceptId(String selectedConceptId) {
        this.selectedConceptId = selectedConceptId;
    }

    public List<String> getConceptIds() {
        return conceptIds;
    }

    public void setConceptIds(List<String> conceptIds) {
        this.conceptIds = conceptIds;
    }

    public Set<String> getSynonymsids() {
        return synonymsids;
    }

    public void setSynonymsids(Set<String> synonymsids) {
        this.synonymsids = synonymsids;
    }


    public Set<String> getEqualsValues() {
        return equalsValues;
    }

    public void setEqualsValues(Set<String> equalsValues) {
        this.equalsValues = equalsValues;
    }

    public Set<String> getSimilarValues() {
        return similarValues;
    }

    public void setSimilarValues(Set<String> similarValues) {
        this.similarValues = similarValues;
    }

    public Set<String> getWordnetIds() {
        return wordnetIds;
    }

    public void setWordnetIds(Set<String> wordnetIds) {
        this.wordnetIds = wordnetIds;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(String errorMessages) {
        this.errorMessages = errorMessages;
    }

}
