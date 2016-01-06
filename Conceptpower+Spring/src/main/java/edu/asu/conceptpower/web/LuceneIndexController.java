package edu.asu.conceptpower.web;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.Principal;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.wordnet.WordNetConfiguration;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

@Controller
public class LuceneIndexController {

    @Autowired
    private WordNetConfiguration configuration;

    @Autowired
    private StandardAnalyzer standradAnalyzer;

    @RequestMapping(value = "auth/luceneIndex", method = RequestMethod.GET)
    public String showLuceneIndex(ModelMap model) {
        return "/auth/luceneIndex";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "auth/indexLuceneWordNet", method = RequestMethod.POST)
    public String addConcept(HttpServletRequest req, Principal principal, ModelMap model) {

        
        //Include condition to delete data from lucene 
        
        String lucenePath = System.getProperty("lucenePath");
        IndexWriter w = null;
        IndexWriterConfig config = new IndexWriterConfig(standradAnalyzer);

        try {
            Path relativePath = FileSystems.getDefault().getPath(lucenePath, "index");
            Directory index = FSDirectory.open(relativePath);
            w = new IndexWriter(index, config);

            String wnhome = configuration.getWordnetPath();
            String path = wnhome + File.separator + configuration.getDictFolder();

            URL url = null;

            url = new URL("file", null, path);

            IDictionary dict = new Dictionary(url);
            dict.open();

            Iterator<IIndexWord> iterator = dict.getIndexWordIterator(POS.NOUN);
            createDocuments(iterator, dict, w);

            iterator = dict.getIndexWordIterator(POS.ADVERB);
            createDocuments(iterator, dict, w);

            iterator = dict.getIndexWordIterator(POS.ADJECTIVE);
            createDocuments(iterator, dict, w);

            iterator = dict.getIndexWordIterator(POS.VERB);
            createDocuments(iterator, dict, w);

            model.addAttribute("message", "Indexing Completed Sucessfully");
        } catch (Exception ex) {
            model.addAttribute("message", "Problem in indexing");
        } finally {
            try {
                w.close();
            } catch (Exception ex) {
                // TODO
            }
        }

        return "/auth/luceneIndex";
    }

    public void createDocuments(Iterator<IIndexWord> iterator, IDictionary dict, IndexWriter writer)
            throws IOException {
        for (; iterator.hasNext();) {
            IIndexWord indexWord = iterator.next();
            List<IWordID> wordIdds = indexWord.getWordIDs();

            for (IWordID wordId : wordIdds) {

                Document doc = new Document();
                doc.add(new TextField("word", wordId.getLemma(), Field.Store.YES));
                doc.add(new StringField("pos", wordId.getPOS().toString(), Field.Store.YES));

                IWord word = dict.getWord(wordId);
                doc.add(new StringField("description", word.getSynset().getGloss(), Field.Store.YES));
                doc.add(new StringField("id", word.getID().toString(), Field.Store.YES));

                ISynset synset = word.getSynset();
                List<IWord> synonyms = synset.getWords();
                StringBuffer sb = new StringBuffer();
                for (IWord syn : synonyms) {
                    if (!syn.getID().equals(word.getID()))
                        sb.append(syn.getID().toString() + edu.asu.conceptpower.core.Constants.SYNONYM_SEPARATOR);
                }
                doc.add(new StringField("synonymId", sb.toString(), Field.Store.YES));
                //Adding this new data to delete only wordnet concepts while adding all wordnet concepts from jwi.
                doc.add(new StringField("conceptType","WordNetConcept", Field.Store.YES));

                System.out.println(wordId.getPOS() + " " + wordId.getLemma());
                writer.addDocument(doc);
            }

        }
    }

}
