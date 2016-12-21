package edu.asu.conceptpower.app.bean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.asu.conceptpower.app.core.POS;
import edu.asu.conceptpower.core.ConceptType;

public class ConceptsMergeBean {

    public List<String> conceptIds;
    private Map<String, String> posMap = new LinkedHashMap<String, String>();
    private Set<String> word;
    private String selectedPosValue;
    private String selectedListName;
    private Set<String> conceptList;
    private Set<String> description;
    private Set<String> synonymsids;
    private Set<String> conceptTypeIdList;
    private Set<String> equals;
    private Set<String> similar;
    private String selectedTypeId;
    private String selectedTypeName;
    private ConceptType[] types;
    private String selectedConceptId;

    public Set<String> getWord() {
        return word;
    }

    public void setWord(Set<String> word) {
        this.word = word;
    }

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

    public Set<String> getDescription() {
        return description;
    }

    public void setDescription(Set<String> description) {
        this.description = description;
    }

    public Set<String> getConceptList() {
        return conceptList;
    }

    public void setConceptList(Set<String> conceptList) {
        this.conceptList = conceptList;
    }

    public Set<String> getConceptTypeIdList() {
        return conceptTypeIdList;
    }

    public void setConceptTypeIdList(Set<String> conceptTypeIdList) {
        this.conceptTypeIdList = conceptTypeIdList;
    }

    public Set<String> getEquals() {
        return equals;
    }

    public void setEquals(Set<String> equals) {
        this.equals = equals;
    }

    public Set<String> getSimilar() {
        return similar;
    }

    public void setSimilar(Set<String> similar) {
        this.similar = similar;
    }

    public Set<String> getSynonymsids() {
        return synonymsids;
    }

    public void setSynonymsids(Set<String> synonymsids) {
        this.synonymsids = synonymsids;
    }

}
