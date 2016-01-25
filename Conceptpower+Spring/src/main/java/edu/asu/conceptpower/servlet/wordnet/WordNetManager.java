package edu.asu.conceptpower.servlet.wordnet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.servlet.core.ConceptEntry;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.lucene.ILuceneUtility;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.WordID;

@Component
public class WordNetManager {

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

		Iterator<IIndexWord> iterator = dict.getIndexWordIterator(POS.NOUN);
		posMap = new HashMap<String, POS>();
		posMap.put(edu.asu.conceptpower.servlet.core.POS.NOUN, POS.NOUN);
		posMap.put(edu.asu.conceptpower.servlet.core.POS.VERB, POS.VERB);
		posMap.put(edu.asu.conceptpower.servlet.core.POS.ADVERB, POS.ADVERB);
		posMap.put(edu.asu.conceptpower.servlet.core.POS.ADJECTIVE, POS.ADJECTIVE);

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
        entry.setWord(word.getLemma());
        entry.setPos(word.getPOS().name());
        entry.setConceptList(Constants.WORDNET_DICTIONARY);

        entry.setDescription(word.getSynset().getGloss());

        ISynset synset = word.getSynset();
        List<IWord> synonyms = synset.getWords();
        StringBuffer sb = new StringBuffer();
        for (IWord syn : synonyms) {
            if (!syn.getID().equals(word.getID()))
                sb.append(syn.getID().toString() + edu.asu.conceptpower.servlet.core.Constants.SYNONYM_SEPARATOR);
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
