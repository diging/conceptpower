package edu.asu.conceptpower.lucene;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
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
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.exceptions.LuceneException;
import edu.asu.conceptpower.wordnet.Constants;
import edu.asu.conceptpower.wordnet.WordNetConfiguration;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

@SuppressWarnings("deprecation")
@Component
public class LuceneUtility implements ILuceneUtility {

    @Autowired
    private WhitespaceAnalyzer whiteSpaceAnalyzer;

    @Autowired
    private StandardAnalyzer standradAnalyzer;

    @Autowired
    private WordNetConfiguration configuration;

    @Value("${lucenePath}")
    private String lucenePath;

    private IndexWriter writer = null;

    public void deleteById(String id) throws LuceneException {
        try {
            Query q = new QueryParser("id", whiteSpaceAnalyzer).parse("id:" + id);
            Path relativePath = FileSystems.getDefault().getPath(lucenePath, "index");
            Directory index = FSDirectory.open(relativePath);
            IndexWriterConfig configWhiteSpace = new IndexWriterConfig(whiteSpaceAnalyzer);
            writer = new IndexWriter(index, configWhiteSpace);
            writer.deleteDocuments(q);
        } catch (Exception ex) {
            throw new LuceneException("Issues in deletion. Please retry");
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
                throw new LuceneException("Problems in closing the writer");
            }
        }

    }

    @SuppressWarnings("deprecation")
    public void insertConcept(ConceptEntry entry) throws LuceneException {
        try {
            Path relativePath = FileSystems.getDefault().getPath(lucenePath, "index");
            Directory index = FSDirectory.open(relativePath);
            IndexWriterConfig config = new IndexWriterConfig(standradAnalyzer);
            writer = new IndexWriter(index, config);
            Document doc = new Document();
            doc.add(new TextField("word", entry.getWord().replace(" ", ""), Field.Store.YES));

            doc.add(new StringField("pos", entry.getPos().toString(), Field.Store.YES));

            doc.add(new StringField("description", entry.getDescription() != null ? entry.getDescription() : "",
                    Field.Store.YES));
            doc.add(new StringField("id", entry.getId(), Field.Store.YES));

            doc.add(new StringField("listName", entry.getConceptList() != null ? entry.getConceptList() : "",
                    Field.Store.YES));

            doc.add(new Field("synonymId", entry.getSynonymIds() != null ? entry.getSynonymIds() : "", Field.Store.YES,
                    Field.Index.NO));
            doc.add(new Field("equalTo", entry.getEqualTo() != null ? entry.getEqualTo() : "", Field.Store.YES,
                    Field.Index.NO));
            doc.add(new Field("similar", entry.getSimilarTo() != null ? entry.getSimilarTo() : "", Field.Store.YES,
                    Field.Index.NO));
            doc.add(new Field("types", entry.getTypeId() != null ? entry.getTypeId() : "", Field.Store.YES,
                    Field.Index.NO));
            doc.add(new Field("creatorId", entry.getCreatorId() != null ? entry.getCreatorId() : "", Field.Store.YES,
                    Field.Index.NO));
            doc.add(new Field("modifiedId", entry.getModified() != null ? entry.getModified() : "", Field.Store.YES,
                    Field.Index.NO));
            doc.add(new StringField("conceptType", "UserConcept", Field.Store.YES));
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
            doc.add(new Field("modifiedTime", formatter.format(cal.getTime()), Field.Store.YES, Field.Index.NO));

            writer.addDocument(doc);
            writer.commit();
        } catch (Exception ex) {
            throw new LuceneException("Cannot insert concept in lucene. Please retry");
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
                throw new LuceneException("Problems in closing the writer");
            }
        }

    }

    public ConceptEntry[] queryLuceneIndex(String word, String pos, String listName, String id, String conceptType)
            throws LuceneException {
        IndexReader reader = null;
        Analyzer analyzer = null;
        List<ConceptEntry> concepts = new ArrayList<ConceptEntry>();
        try {
            Query q = null;

            StringBuffer queryString = new StringBuffer();
            String defaultQuery = null;

            if (word != null) {
                queryString.append("word:" + word);
                analyzer = standradAnalyzer;
                defaultQuery = "word";
            }
            if (pos != null) {
                if (queryString.length() != 0)
                    queryString.append(" AND pos:" + pos);
                else
                    queryString.append("pos:" + pos);
                defaultQuery = "pos";
            }

            if (listName != null) {
                if (queryString.length() != 0) {
                    queryString.append(" AND listName:" + listName);
                } else {
                    queryString.append("listName:" + listName);
                }
                analyzer = whiteSpaceAnalyzer;
                defaultQuery = "listName";
            }

            if (id != null) {
                if (queryString.length() != 0) {
                    queryString.append(" AND id:" + id);
                } else {
                    queryString.append("id:" + id);
                }
                analyzer = whiteSpaceAnalyzer;
                defaultQuery = "id";
            }

            if (conceptType != null) {
                if (queryString.length() != 0) {
                    queryString.append(" AND conceptType:" + conceptType);
                } else {
                    queryString.append("conceptType:" + conceptType);
                }
            }
            q = new QueryParser(defaultQuery, analyzer).parse(queryString.toString());
            Path relativePath = FileSystems.getDefault().getPath(lucenePath, "index");
            Directory index = FSDirectory.open(relativePath);

            int hitsPerPage = 10;

            reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(q, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;

            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                ConceptEntry entry = getConceptFromDocument(d);
                concepts.add(entry);
            }
        } catch (Exception ex) {
            throw new LuceneException("Issues in querying lucene index. Please retry");
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
                throw new LuceneException("Problems in closing the writer");
            }
        }
        return concepts.toArray(new ConceptEntry[concepts.size()]);
    }

    private ConceptEntry getConceptFromDocument(Document d) {
        ConceptEntry entry = new ConceptEntry();
        entry.setId(d.get("id"));
        entry.setWord(d.get("word"));
        entry.setPos(d.get("pos"));
        entry.setConceptList(Constants.WORDNET_DICTIONARY);
        entry.setDescription(d.get("description"));
        entry.setWordnetId(d.get("id"));
        entry.setSynonymIds(d.get("synonymId"));
        entry.setConceptList(d.get("listName"));
        entry.setTypeId(d.get("types"));
        entry.setEqualTo(d.get("equalTo"));
        entry.setSimilarTo(d.get("similar"));
        entry.setModified(d.get("modifiedId"));
        entry.setSynonymIds(d.get("synonymId"));
        entry.setCreatorId(d.get("creatorId"));
        return entry;
    }

    @Override
    public void deleteIndexes() throws LuceneException {

        Path relativePath = FileSystems.getDefault().getPath(lucenePath, "index");

        try {
            Directory index = FSDirectory.open(relativePath);
            Query q = new QueryParser("conceptType", whiteSpaceAnalyzer).parse("*:*");
            IndexWriterConfig config = new IndexWriterConfig(whiteSpaceAnalyzer);
            writer = new IndexWriter(index, config);
            writer.deleteDocuments(q);
            writer.commit();
        } catch (Exception ex) {
            throw new LuceneException("Issues in deleting wordnet concepts. Please retry");
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
                throw new LuceneException("Problems in closing the writer");
            }
        }

    }

    @SuppressWarnings("deprecation")
    protected void createDocuments(Iterator<IIndexWord> iterator, IDictionary dict, IndexWriter writer)
            throws IOException {
        for (; iterator.hasNext();) {
            IIndexWord indexWord = iterator.next();
            List<IWordID> wordIdds = indexWord.getWordIDs();

            for (IWordID wordId : wordIdds) {

                Document doc = new Document();
                doc.add(new Field("word", wordId.getLemma(), Store.YES, Index.ANALYZED));
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
                // Adding this new data to delete only wordnet concepts while
                // adding all wordnet concepts from jwi.
                doc.add(new StringField("conceptType", "wordnetconcept", Field.Store.YES));

                System.out.println(wordId.getPOS() + " " + wordId.getLemma());
                writer.addDocument(doc);
            }

        }
    }

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
