package edu.asu.conceptpower.app.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.asu.conceptpower.app.error.CPError;

public class ConceptsMergeBean {

    private String selectedPosValue;
    private String selectedListName;
    private String selectedTypeId;
    private String selectedTypeName;
    private String selectedConceptId;
    private List<String> descriptions = new ArrayList<>();
    private Set<String> synonymsids = new HashSet<>();
    private Set<String> equalsValues = new HashSet<>();
    private Set<String> similarValues = new HashSet<>();
    /**
     * Any wordnet id that belongs to a concept will be added to this set.
     */
    private Set<String> mergedIds = new HashSet<>();
    private List<String> conceptIds;
    private List<String> conceptNamesWithIds = new ArrayList<>();
    private String word;
    private List<CPError> errorMessages = new ArrayList<>();
    /**
     * This variable contains all the wordnet ids that are gettting merged.
     */
    private Set<String> conceptWordnetIds = new HashSet<>();

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

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<String> getConceptNamesWithIds() {
        return conceptNamesWithIds;
    }

    public void setConceptNamesWithIds(List<String> conceptNamesWithIds) {
        this.conceptNamesWithIds = conceptNamesWithIds;
    }

    public void clear() {
        this.getConceptNamesWithIds().clear();
        this.getSynonymsids().clear();
        this.getEqualsValues().clear();
        this.getSimilarValues().clear();
        this.mergedIds.clear();
        this.errorMessages.clear();
    }

    public Set<String> getConceptWordnetIds() {
        return conceptWordnetIds;
    }

    public void setConceptWordnetIds(Set<String> conceptWordnetIds) {
        this.conceptWordnetIds = conceptWordnetIds;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<String> descriptions) {
        this.descriptions = descriptions;
    }

    public Set<String> getMergedIds() {
        return mergedIds;
    }

    public void setMergedIds(Set<String> mergedIds) {
        this.mergedIds = mergedIds;
    }

    public List<CPError> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<CPError> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
