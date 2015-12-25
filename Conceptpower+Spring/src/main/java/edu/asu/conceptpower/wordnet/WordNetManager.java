package edu.asu.conceptpower.wordnet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
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
	
	@Autowired
	private StandardAnalyzer analyzer;
	
	private Directory index = new RAMDirectory();
	
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
        int j = 0;
        

        for (; iterator.hasNext();) {
            j++;
            IIndexWord indexWord = iterator.next();
            List<IWordID> wordIdds = indexWord.getWordIDs();

            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter w = new IndexWriter(index, config);
            for (IWordID wordId : wordIdds) {
                Document doc = new Document();
                doc.add(new TextField("word", wordId.getLemma(), Field.Store.YES));
                doc.add(new StringField("pos", wordId.getPOS().toString(), Field.Store.YES));
                doc.add(new StringField("id", wordId.getSynsetID().toString(), Field.Store.YES));
                IWord word = dict.getWord(wordId);
                doc.add(new StringField("decription", word.getSynset().getGloss(), Field.Store.YES));
                doc.add(new StringField("wordNetId", word.getID().toString(), Field.Store.YES));
                w.addDocument(doc);
                
                System.out.println(wordId.getPOS() + " " + wordId.getLemma());
            }

            if (j == 10000)
                break;

            w.close();
        }
        // System.out.println(j);
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
        ConceptEntry entry = new ConceptEntry();
        try {
            String word = wordId.getLemma();
            String pos = wordId.getPOS().toString();

            Query q = new QueryParser("title", analyzer).parse("word:" + word + " AND pos:" + pos);

            int hitsPerPage = 10;
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(q, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;

            System.out.println("Found " + hits.length + " hits.");
            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);

                entry.setId(d.get("id"));
                entry.setWord(d.get("word"));
                entry.setPos(d.get("pos"));
                entry.setConceptList(Constants.WORDNET_DICTIONARY);
                entry.setDescription(d.get("Description"));

            }

            IWord iWord = null;
            /*
             * ConceptEntry entry = null; if (word != null) entry =
             * createConceptEntry();
             */

        } catch (Exception ex) {
            ex.printStackTrace();
        }

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
