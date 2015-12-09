package edu.asu.conceptpower.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import edu.asu.conceptpower.wrapper.ConceptEntryWrapper;
import edu.mit.jwi.item.POS;

@Component
public class ConceptSearchBean {

    private String word;
    private POS pos;
    private Map<POS, POS> posMap;
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

    public Map<POS, POS> getPosMap() {
        posMap = new HashMap<POS, POS>();
        posMap.put(POS.NOUN, POS.NOUN);
        posMap.put(POS.VERB, POS.VERB);
        posMap.put(POS.ADVERB, POS.ADVERB);
        posMap.put(POS.ADJECTIVE, POS.ADJECTIVE);
        return posMap;
    }

    public void setPosMap(Map<POS, POS> posMap) {
        this.posMap = posMap;
    }

    public POS getPos() {
        return pos;
    }

    public void setPos(POS pos) {
        this.pos = pos;
    }


}
