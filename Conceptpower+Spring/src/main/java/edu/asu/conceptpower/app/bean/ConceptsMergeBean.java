package edu.asu.conceptpower.app.bean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.asu.conceptpower.app.core.POS;
import edu.asu.conceptpower.core.ConceptType;

public class ConceptsMergeBean {

    private List<String> conceptIds;
    private Map<String, String> posMap = new LinkedHashMap<String, String>();
    private Set<String> words;
    private String selectedPosValue;
    private String selectedListName;
    private Set<String> conceptListValues;
    private Set<String> descriptions;
    private Set<String> synonymsids;
    private Set<String> conceptTypeIdList;
    private Set<String> equalsValues;
    private Set<String> similarValues;
    private String selectedTypeId;
    private String selectedTypeName;
    private ConceptType[] types;
    private String selectedConceptId;
    private Set<String> wordnetIds;

    public Map<String, String> getPosMap() {
        posMap = new LinkedHashMap<String, String>();
        posMap.put(POS.NOUN, "Noun");
        posMap.put(POS.VERB, "Verb");
        posMap.put(POS.ADVERB, "Adverb");
        posMap.put(POS.ADJECTIVE, "Adjective");
        return posMap;
    }

    public void setPosMap(Map<String, String> posMap) {
        this.posMap = posMap;
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

    public Set<String> getWords() {
        return words;
    }

    public void setWords(Set<String> words) {
        this.words = words;
    }

    public Set<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(Set<String> descriptions) {
        this.descriptions = descriptions;
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

    public Set<String> getConceptTypeIdList() {
        return conceptTypeIdList;
    }

    public void setConceptTypeIdList(Set<String> conceptTypeIdList) {
        this.conceptTypeIdList = conceptTypeIdList;
    }

    public Set<String> getConceptListValues() {
        return conceptListValues;
    }

    public void setConceptListValues(Set<String> conceptListValues) {
        this.conceptListValues = conceptListValues;
    }

    public Set<String> getWordnetIds() {
        return wordnetIds;
    }

    public void setWordnetIds(Set<String> wordnetIds) {
        this.wordnetIds = wordnetIds;
    }

}
