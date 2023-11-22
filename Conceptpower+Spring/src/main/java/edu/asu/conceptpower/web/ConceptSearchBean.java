package edu.asu.conceptpower.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import edu.asu.conceptpower.app.core.POS;
import edu.asu.conceptpower.app.wrapper.ConceptEntryWrapper;

@Component
public class ConceptSearchBean {

    private String word;
    private String pos;
    private Map<String, String> posMap;
    // TODO Set this variable equal to the list of conceptLists
    private List<String> conceptListMap;
    private List<ConceptEntryWrapper> foundConcepts;
    private String luceneError;
    private boolean searchOnDescription;
    private List<String> conceptLists;
    private List<String> posList;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<ConceptEntryWrapper> getFoundConcepts() {
        return foundConcepts;
    }

    public void setFoundConcepts(List<ConceptEntryWrapper> foundConcepts) {
        this.foundConcepts = foundConcepts;
    }


    public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
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

    public String getLuceneError() {
        return luceneError;
    }

    public void setLuceneError(String luceneError) {
        this.luceneError = luceneError;
    }

    public boolean isSearchOnDescription() {
        return searchOnDescription;
    }

    public void setSearchOnDescription(boolean searchOnDescription) {
        this.searchOnDescription = searchOnDescription;
    }

    public List<String> getConceptList() {
        return conceptLists;
    }

    public void setConceptList(List<String> conceptList) {
        this.conceptLists = conceptList;
    }

    public List<String> getPosList() {
        return posList;
    }

    public void setPosList(List<String> posList) {
        this.posList = posList;
    }

    public List<String> getConceptListMap() {
        return conceptListMap;
    }

    public void setConceptListMap(List<String> conceptListMap) {
        this.conceptListMap = conceptListMap;
    }

}
