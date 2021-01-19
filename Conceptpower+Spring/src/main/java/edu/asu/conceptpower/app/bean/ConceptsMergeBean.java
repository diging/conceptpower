package edu.asu.conceptpower.app.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.asu.conceptpower.app.error.CPError;

public class ConceptsMergeBean {

    private String selectedPosValue;
    private String selectedListId;
    private String selectedTypeId;
    private String selectedTypeName;
    private String selectedConceptId;
    private List<String> descriptions = new ArrayList<>();
    private Set<String> synonymsids = new HashSet<>();
    private Set<String> equalsValues = new HashSet<>();
    private Set<String> similarValues = new HashSet<>();
    private Set<String> mergeIds = new HashSet<>();
    private String word;
    private List<CPError> errorMessages = new ArrayList<>();
    private Set<String> alternativeIds = new HashSet<>();

    public String getSelectedPosValue() {
        return selectedPosValue;
    }

    public void setSelectedPosValue(String selectedPosValue) {
        this.selectedPosValue = selectedPosValue;
    }

    public String getSelectedListId() {
        return selectedListId;
    }

    public void setSelectedListId(String selectedListId) {
        this.selectedListId = selectedListId;
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

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void clear() {
        this.getSynonymsids().clear();
        this.getEqualsValues().clear();
        this.getSimilarValues().clear();
        this.mergeIds.clear();
        this.errorMessages.clear();
        this.alternativeIds.clear();
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<String> descriptions) {
        this.descriptions = descriptions;
    }

    public Set<String> getMergeIds() {
        return mergeIds;
    }

    public void setMergeIds(Set<String> mergeIds) {
        this.mergeIds = mergeIds;
    }

    public List<CPError> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<CPError> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public Set<String> getAlternativeIds() {
        return alternativeIds;
    }

    public void setAlternativeIds(Set<String> alternativeIds) {
        this.alternativeIds = alternativeIds;
    }

}
