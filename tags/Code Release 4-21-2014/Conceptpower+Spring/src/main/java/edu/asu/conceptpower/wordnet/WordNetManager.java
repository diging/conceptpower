package edu.asu.conceptpower.wordnet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.WordID;

@Service
public class WordNetManager {

	@Autowired
	private WordNetConfiguration configuration;

	private IDictionary dict;

	private Map<String, POS> posMap;

	@PostConstruct
	public void init() throws IOException {
		String wnhome = configuration.getWordnetPath();
		String path = wnhome + File.separator + configuration.getDictFolder();
		URL url = null;

		url = new URL("file", null, path);

		dict = new Dictionary(url);
		dict.open();

		posMap = new HashMap<String, POS>();
		posMap.put(edu.asu.conceptpower.core.POS.NOUN, POS.NOUN);
		posMap.put(edu.asu.conceptpower.core.POS.VERB, POS.VERB);
		posMap.put(edu.asu.conceptpower.core.POS.ADVERB, POS.ADVERB);
		posMap.put(edu.asu.conceptpower.core.POS.ADJECTIVE, POS.ADJECTIVE);
	}

	public ConceptEntry getConcept(String id) {

		IWordID wordId = null;
		try {
			wordId = WordID.parseWordID(id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if (wordId != null) {
			ConceptEntry entry = getConceptFromWordId(wordId);

			return entry;
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
		entry.setWord(word.getLemma());
		entry.setPos(word.getPOS().name());
		entry.setConceptList(Constants.WORDNET_DICTIONARY);

		entry.setDescription(word.getSynset().getGloss());

		ISynset synset = word.getSynset();
		List<IWord> synonyms = synset.getWords();
		StringBuffer sb = new StringBuffer();
		for (IWord syn : synonyms) {
			if (!syn.getID().equals(word.getID()))
				sb.append(syn.getID().toString()
						+ edu.asu.conceptpower.core.Constants.SYNONYM_SEPARATOR);
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

	public ConceptEntry[] getEntriesForWord(String word) {

		if (word == null || word.trim().isEmpty())
			return new ConceptEntry[0];

		POS pPOS = POS.NOUN;

		List<ConceptEntry> concepts = new ArrayList<ConceptEntry>();
		// get nouns
		IIndexWord indexWord = dict.getIndexWord(word, pPOS);
		if (indexWord != null) {
			List<IWordID> wordIDs = indexWord.getWordIDs();
			for (IWordID wid : wordIDs) {
				ConceptEntry entry = getConceptFromWordId(wid);
				concepts.add(entry);
			}
		}

		// get verbs
		pPOS = POS.VERB;
		indexWord = dict.getIndexWord(word, pPOS);
		if (indexWord != null) {
			List<IWordID> wordIDs = indexWord.getWordIDs();
			for (IWordID wid : wordIDs) {
				ConceptEntry entry = getConceptFromWordId(wid);
				concepts.add(entry);
			}
		}

		// get adverbs
		pPOS = POS.ADVERB;
		indexWord = dict.getIndexWord(word, pPOS);
		if (indexWord != null) {
			List<IWordID> wordIDs = indexWord.getWordIDs();
			for (IWordID wid : wordIDs) {
				ConceptEntry entry = getConceptFromWordId(wid);
				concepts.add(entry);
			}
		}

		// get adjectives
		pPOS = POS.ADJECTIVE;
		indexWord = dict.getIndexWord(word, pPOS);
		if (indexWord != null) {
			List<IWordID> wordIDs = indexWord.getWordIDs();
			for (IWordID wid : wordIDs) {
				ConceptEntry entry = getConceptFromWordId(wid);
				concepts.add(entry);
			}
		}

		return concepts.toArray(new ConceptEntry[concepts.size()]);
	}

	public ConceptEntry[] getEntriesForWord(String word, String pos) {
		POS pPOS = posMap.get(pos);
		if (pPOS == null)
			return null;

		List<ConceptEntry> concepts = new ArrayList<ConceptEntry>();
		IIndexWord indexWord = dict.getIndexWord(word, pPOS);
		if (indexWord != null) {
			List<IWordID> wordIDs = indexWord.getWordIDs();
			for (IWordID wid : wordIDs) {
				ConceptEntry entry = getConceptFromWordId(wid);
				concepts.add(entry);
			}
			return concepts.toArray(new ConceptEntry[concepts.size()]);
		}

		return null;
	}

	@PreDestroy
	public void close() {
		dict.close();
	}
}
