package edu.asu.conceptpower.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import edu.asu.conceptpower.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.core.POS;

@Component
public class ConceptSearchBean {

    private String word;
    private String pos;
    private Map<String, String> posMap;
    private List<ConceptEntryWrapper> foundConcepts;

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

}
