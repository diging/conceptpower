package edu.asu.conceptpower.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import edu.asu.conceptpower.wrapper.ConceptEntryWrapper;
import edu.mit.jwi.item.POS;

@Component
public class ConceptSearchBean {

    private String word;
    private POS pos;
    private Map<POS, String> posMap;
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

    public POS getPos() {
        return pos;
    }

    public void setPos(POS pos) {
        this.pos = pos;
    }

    public Map<POS, String> getPosMap() {
        posMap = new LinkedHashMap<POS, String>();
        posMap.put(POS.NOUN, "Noun");
        posMap.put(POS.VERB, "Verb");
        posMap.put(POS.ADVERB, "Adverb");
        posMap.put(POS.ADJECTIVE, "Adjective");
        return posMap;
    }

    public void setPosMap(Map<POS, String> posMap) {
        this.posMap = posMap;
    }

}
