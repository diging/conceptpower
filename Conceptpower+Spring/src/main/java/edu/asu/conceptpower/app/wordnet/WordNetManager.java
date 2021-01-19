package edu.asu.conceptpower.app.wordnet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.WordID;

@Component
public class WordNetManager {

    private static final Logger logger = LoggerFactory.getLogger(WordNetManager.class);
    
    @Autowired
    private WordNetConfiguration configuration;

    private IDictionary dict;

    private Map<String, POS> posMap;

    @PostConstruct
	public void init() throws IOException, org.apache.lucene.queryparser.classic.ParseException {

		String wnhome = configuration.getWordnetPath();
		String path = wnhome + File.separator + configuration.getDictFolder();

		URL url = null;

		url = new URL("file", null, path);

		dict = new Dictionary(url);
		dict.open();

		posMap = new HashMap<String, POS>();
		posMap.put(edu.asu.conceptpower.app.core.POS.NOUN, POS.NOUN);
		posMap.put(edu.asu.conceptpower.app.core.POS.VERB, POS.VERB);
		posMap.put(edu.asu.conceptpower.app.core.POS.ADVERB, POS.ADVERB);
		posMap.put(edu.asu.conceptpower.app.core.POS.ADJECTIVE, POS.ADJECTIVE);

	}
    
    public ConceptEntry getConcept(String id) {
        
        if (id == null || id.equals("null")) {
            return null;
        }

        IWordID wordId = null;
        try {
            wordId = WordID.parseWordID(id);
        } catch (IllegalArgumentException e) {
            logger.warn("Could not find id '" + id + "' in WordNet.", e);
            return null;
        }

        if (wordId != null) {
            return getConceptFromWordId(wordId);
        }

        return null;
    }


    public ConceptEntry[] getSynonyms(String id) {
        IWordID wordId = null;
        try {
            wordId = WordID.parseWordID(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        List<ConceptEntry> entries = new ArrayList<ConceptEntry>();
        if (wordId != null) {
            IWord word = dict.getWord(wordId);
            ISynset synset = word.getSynset();
            if (synset != null) {
                List<IWord> synonyms = synset.getWords();
                for (IWord synonym : synonyms) {
                    entries.add(createConceptEntry(synonym));
                }
            }
        }

        return entries.toArray(new ConceptEntry[entries.size()]);
    }

    protected ConceptEntry createConceptEntry(IWord word) {
        ConceptEntry entry = new ConceptEntry();
        entry.setId(word.getID().toString());
        entry.setWordnetId(word.getID().toString());
        entry.setWord(word.getLemma().replace("_", " "));
        entry.setPos(word.getPOS().name());
        //TODO: Needs Handling
        entry.setConceptListId(Constants.WORDNET_DICTIONARY);

        entry.setDescription(word.getSynset().getGloss());

        ISynset synset = word.getSynset();
        List<IWord> synonyms = synset.getWords();
        StringBuffer sb = new StringBuffer();
        for (IWord syn : synonyms) {
            if (!syn.getID().equals(word.getID()))
                sb.append(syn.getID().toString() + edu.asu.conceptpower.app.core.Constants.SYNONYM_SEPARATOR);
        }
        entry.setSynonymIds(sb.toString());

        return entry;
    }

    protected ConceptEntry getConceptFromWordId(IWordID wordId) {
        IWord word = dict.getWord(wordId);
        ConceptEntry entry = null;
        if (word != null)
            entry = createConceptEntry(word);
        return entry;

    }

    @PreDestroy
    public void close() {
        dict.close();
    }

}
