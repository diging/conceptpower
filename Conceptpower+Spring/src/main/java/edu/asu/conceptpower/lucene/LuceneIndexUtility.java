package edu.asu.conceptpower.lucene;

import java.io.File;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Iterator;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.exceptions.LuceneException;
import edu.asu.conceptpower.wordnet.WordNetConfiguration;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.POS;

@Component
public class LuceneIndexUtility extends LuceneUtility implements ILuceneIndexUtility {

    @Autowired
    private StandardAnalyzer standradAnalyzer;

    @Autowired
    private WordNetConfiguration configuration;

    @Value("${lucenePath}")
    private String lucenePath;

    @Override
    public void indexConcepts() throws LuceneException {

        IndexWriter writer = null;
        IndexWriterConfig config = new IndexWriterConfig(standradAnalyzer);

        try {
            Path relativePath = FileSystems.getDefault().getPath(lucenePath, "index");
            Directory index = FSDirectory.open(relativePath);

            String wnhome = configuration.getWordnetPath();
            String path = wnhome + File.separator + configuration.getDictFolder();

            URL url = null;

            url = new URL("file", null, path);

            IDictionary dict = new Dictionary(url);
            dict.open();

            // 2. Adding data into
            writer = new IndexWriter(index, config);
            Iterator<IIndexWord> iterator = dict.getIndexWordIterator(POS.NOUN);
            createDocuments(iterator, dict, writer);

            iterator = dict.getIndexWordIterator(POS.ADVERB);
            createDocuments(iterator, dict, writer);

            iterator = dict.getIndexWordIterator(POS.ADJECTIVE);
            createDocuments(iterator, dict, writer);

            iterator = dict.getIndexWordIterator(POS.VERB);
            createDocuments(iterator, dict, writer);

        } catch (Exception ex) {
            throw new LuceneException("Problem in Creating Index. Please retry");
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
                throw new LuceneException("Problem in Lucene writer. Please retry");
            }
        }
    }
}
