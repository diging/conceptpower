package edu.asu.conceptpower.app.bean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import edu.asu.conceptpower.app.core.POS;
import edu.asu.conceptpower.app.core.model.impl.ConceptType;

@Component
public class ConceptEditBean {

    private Map<String, String> posMap = new LinkedHashMap<String, String>();
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
    private List<ConceptType> types;
    private String conceptId;
    private List conceptEntryList;
    private boolean fromHomeScreen;
    private String luceneError;
    private String wordnetIds;
    private String existingWordnetIds;

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

    public List<ConceptType> getTypes() {
        return types;
    }

    public void setTypes(List<ConceptType> types) {
        this.types = types;
    }

    public String getSynonymsids() {
        return synonymsids;
    }

    public void setSynonymsids(String synonymsids) {
        this.synonymsids = synonymsids;
    }

    public List getConceptEntryList() {
        return conceptEntryList;
    }

    public void setConceptEntryList(List conceptEntryList) {
        this.conceptEntryList = conceptEntryList;
    }

    public boolean isFromHomeScreen() {
        return fromHomeScreen;
    }

    public void setFromHomeScreen(boolean fromHomeScreen) {
        this.fromHomeScreen = fromHomeScreen;
    }

    public Map<String, String> getPosMap() {
        posMap.put(POS.NOUN, "Noun");
        posMap.put(POS.VERB, "Verb");
        posMap.put(POS.ADVERB, "Adverb");
        posMap.put(POS.ADJECTIVE, "Adjective");
        return posMap;
    }

    public void setPosMap(Map<String, String> posMap) {
        this.posMap = posMap;
    }

    public String getLuceneError() {
        return luceneError;
    }

    public void setLuceneError(String luceneError) {
        this.luceneError = luceneError;
    }

    public String getWordnetIds() {
        return wordnetIds;
    }

    public void setWordnetIds(String wordnetIds) {
        this.wordnetIds = wordnetIds;
    }

    public String getExistingWordnetIds() {
        return existingWordnetIds;
    }

    public void setExistingWordnetIds(String existingWordnetIds) {
        this.existingWordnetIds = existingWordnetIds;
    }
}
