package edu.asu.conceptpower.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.WordUtils;
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
        posMap = new HashMap<POS, String>();
        posMap.put(POS.NOUN, WordUtils.capitalize(POS.NOUN.toString()));
        posMap.put(POS.VERB, WordUtils.capitalize(POS.VERB.toString()));
        posMap.put(POS.ADVERB, WordUtils.capitalize(POS.ADVERB.toString()));
        posMap.put(POS.ADJECTIVE, WordUtils.capitalize(POS.ADJECTIVE.toString()));
        return posMap;
    }

    public void setPosMap(Map<POS, String> posMap) {
        this.posMap = posMap;
    }

}
