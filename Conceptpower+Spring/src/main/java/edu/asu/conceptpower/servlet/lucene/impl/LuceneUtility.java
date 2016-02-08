package edu.asu.conceptpower.servlet.lucene.impl;

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
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.servlet.core.ConceptEntry;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.lucene.ILuceneUtility;
import edu.asu.conceptpower.servlet.reflect.LuceneField;
import edu.asu.conceptpower.servlet.reflect.SearchField;
import edu.asu.conceptpower.servlet.rest.LuceneFieldNames;
import edu.asu.conceptpower.servlet.wordnet.WordNetConfiguration;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

@Component
@PropertySource("classpath:lucene.properties")
public class LuceneUtility implements ILuceneUtility {

    @Autowired
    private WhitespaceAnalyzer whiteSpaceAnalyzer;

    @Autowired
    private WordNetConfiguration configuration;

    @Value("${lucenePath}")
    private String lucenePath;

    private IndexWriter writer = null;
    private IndexReader reader = null;

    @PostConstruct
    public void init() throws LuceneException {
        try {
            Path relativePath = FileSystems.getDefault().getPath(lucenePath, "index");
            Directory index = FSDirectory.open(relativePath);
            IndexWriterConfig configWhiteSpace = new IndexWriterConfig(whiteSpaceAnalyzer);
            writer = new IndexWriter(index, configWhiteSpace);
            reader = DirectoryReader.open(writer, true);
        } catch (IOException ex) {
            throw new LuceneException("Restart the application", ex);
        }
    }

    public void deleteById(String id) throws LuceneException {
        try {
            Query q = new QueryParser("id", whiteSpaceAnalyzer).parse("id:" + id);
            writer.deleteDocuments(q);
            writer.commit();
        } catch (IOException ex) {
            throw new LuceneException("Issues in deletion. Please retry", ex);
        } catch (ParseException e) {
            throw new LuceneException("Issues in framing queries", e);
        }

    }

    public void insertConcept(ConceptEntry entry) throws LuceneException {
        Document doc = new Document();
        doc.add(new StringField(LuceneFieldNames.WORD, entry.getWord().replace(" ", ""), Field.Store.YES));

        doc.add(new StringField(LuceneFieldNames.POS, entry.getPos().toString(), Field.Store.YES));

        doc.add(new StringField(LuceneFieldNames.DESCRIPTION,
                entry.getDescription() != null ? entry.getDescription() : "", Field.Store.YES));

        doc.add(new StringField(LuceneFieldNames.CONCEPT_LIST,
                entry.getConceptList() != null ? entry.getConceptList() : "", Field.Store.YES));

        doc.add(new StoredField(LuceneFieldNames.SYNONYMID,
                entry.getSynonymIds() != null ? entry.getSynonymIds() : ""));
        doc.add(new StoredField(LuceneFieldNames.EQUALS_TO, entry.getEqualTo() != null ? entry.getEqualTo() : ""));
        doc.add(new StoredField(LuceneFieldNames.SIMILAR_TO, entry.getSimilarTo() != null ? entry.getSimilarTo() : ""));
        doc.add(new StoredField(LuceneFieldNames.TYPE_ID, entry.getTypeId() != null ? entry.getTypeId() : ""));
        doc.add(new StoredField(LuceneFieldNames.CREATOR, entry.getCreatorId() != null ? entry.getCreatorId() : ""));
        doc.add(new StoredField(LuceneFieldNames.MODIFIED, entry.getModified() != null ? entry.getModified() : ""));
        doc.add(new StoredField(LuceneFieldNames.TYPE_ID, "UserConcept"));
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        doc.add(new StoredField(LuceneFieldNames.MODIFIED_TIME, formatter.format(cal.getTime())));
        doc.add(new StoredField(LuceneFieldNames.WORDNETID, entry.getWordnetId() != null ? entry.getWordnetId() : ""));
        doc.add(new StoredField(LuceneFieldNames.ID, entry.getId() != null ? entry.getId() : ""));

        try {
            writer.addDocument(doc);
            writer.commit();
        } catch (IOException ex) {
            throw new LuceneException("Cannot insert concept in lucene. Please retry", ex);
        }

    }

    private ConceptEntry getConceptFromDocument(Document d) {
        ConceptEntry entry = new ConceptEntry();
        entry.setId(d.get(LuceneFieldNames.ID));
        entry.setWord(d.get(LuceneFieldNames.WORD));
        entry.setPos(d.get(LuceneFieldNames.POS));
        entry.setConceptList(d.get(LuceneFieldNames.CONCEPT_LIST));
        entry.setDescription(d.get(LuceneFieldNames.DESCRIPTION));
        entry.setWordnetId(d.get(LuceneFieldNames.WORDNETID));
        entry.setSynonymIds(d.get(LuceneFieldNames.SYNONYMID));
        entry.setTypeId(d.get(d.get(LuceneFieldNames.TYPE_ID)));
        entry.setEqualTo(d.get(LuceneFieldNames.EQUALS_TO));
        entry.setSimilarTo(d.get(LuceneFieldNames.SIMILAR_TO));
        entry.setModified(d.get(LuceneFieldNames.MODIFIED));
        entry.setSynonymIds(d.get(LuceneFieldNames.SYNONYMID));
        entry.setCreatorId(d.get(LuceneFieldNames.CREATOR));
        return entry;
    }

    @Override
    public void deleteIndexes() throws LuceneException {
        try {
            Query q = new QueryParser("conceptType", whiteSpaceAnalyzer).parse("*:*");
            writer.deleteDocuments(q);
            writer.commit();
        } catch (IOException ex) {
            throw new LuceneException("Issues in deleting wordnet concepts. Please retry", ex);
        } catch (ParseException e) {
            throw new LuceneException("Issues in framing the query", e);
        }
    }

    protected void createDocuments(Iterator<IIndexWord> iterator, IDictionary dict, IndexWriter writer)
            throws IOException {
        for (; iterator.hasNext();) {
            IIndexWord indexWord = iterator.next();
            List<IWordID> wordIdds = indexWord.getWordIDs();

            for (IWordID wordId : wordIdds) {

                Document doc = new Document();
                doc.add(new StringField(LuceneFieldNames.WORD, wordId.getLemma(), Field.Store.YES));
                doc.add(new StringField(LuceneFieldNames.POS, wordId.getPOS().toString(), Field.Store.YES));

                IWord word = dict.getWord(wordId);
                doc.add(new StringField(LuceneFieldNames.DESCRIPTION, word.getSynset().getGloss(), Field.Store.YES));
                doc.add(new StringField(LuceneFieldNames.ID, word.getID().toString(), Field.Store.YES));
                doc.add(new StringField(LuceneFieldNames.WORDNETID, word.getID().toString(), Field.Store.YES));

                ISynset synset = word.getSynset();
                List<IWord> synonyms = synset.getWords();
                StringBuffer sb = new StringBuffer();
                for (IWord syn : synonyms) {
                    if (!syn.getID().equals(word.getID()))
                        sb.append(
                                syn.getID().toString() + edu.asu.conceptpower.servlet.core.Constants.SYNONYM_SEPARATOR);
                }
                doc.add(new StringField(LuceneFieldNames.SYNONYMID, sb.toString(), Field.Store.YES));
                // Adding this new data to delete only wordnet concepts while
                // adding all wordnet concepts from jwi.
                doc.add(new StringField(LuceneFieldNames.CONCEPTTYPE, "wordnetconcept", Field.Store.YES));
                writer.addDocument(doc);
            }

        }
    }

    @Override
    public void indexConcepts() throws LuceneException {

        try {
            String wnhome = configuration.getWordnetPath();
            String path = wnhome + File.separator + configuration.getDictFolder();

            URL url = null;

            url = new URL("file", null, path);

            IDictionary dict = new Dictionary(url);
            dict.open();

            // 2. Adding data into
            Iterator<IIndexWord> iterator = dict.getIndexWordIterator(POS.NOUN);
            createDocuments(iterator, dict, writer);

            iterator = dict.getIndexWordIterator(POS.ADVERB);
            createDocuments(iterator, dict, writer);

            iterator = dict.getIndexWordIterator(POS.ADJECTIVE);
            createDocuments(iterator, dict, writer);

            iterator = dict.getIndexWordIterator(POS.VERB);
            createDocuments(iterator, dict, writer);

            writer.commit();

        } catch (IOException ex) {
            throw new LuceneException("Problem in Creating Index. Please retry", ex);
        }
    }

    public ConceptEntry[] queryIndex(Map<String, String> fieldMap, String operator) throws LuceneException {

        if (operator == null) {
            operator = "AND";
        }

        StringBuffer queryString = new StringBuffer();
        int firstEntry = 1;
        for (String fieldName : fieldMap.keySet()) {
            String searchString = fieldMap.get(fieldName);
            if (searchString != null) {
                ConceptEntry con = new ConceptEntry();
                java.lang.reflect.Field[] fields = con.getClass().getDeclaredFields();
                for (java.lang.reflect.Field field : fields) {
                    SearchField search = field.getAnnotation(SearchField.class);
                    LuceneField luceneFieldAnnotation = field.getAnnotation(LuceneField.class);
                    if (search != null) {
                        if (search.fieldName().equals(fieldName)) {
                            if (firstEntry != 1)
                                queryString.append(" " + operator + " ");
                            firstEntry++;
                            queryString.append(luceneFieldAnnotation.lucenefieldName() + ":");
                            queryString.append(searchString);
                        }
                    }
                }
            }
        }

        List<ConceptEntry> concepts = new ArrayList<ConceptEntry>();
        int hitsPerPage = 10;
        IndexSearcher searcher = null;
        try {
            Query q = new QueryParser("", whiteSpaceAnalyzer).parse(queryString.toString());
            reader = DirectoryReader.open(writer, true);
            searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(q, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;
            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                ConceptEntry entry = getConceptFromDocument(d);
                concepts.add(entry);
            }
        }

        catch (IOException ex) {
            throw new LuceneException("Issues in querying lucene index. Please retry", ex);
        } catch (ParseException e) {
            throw new LuceneException("Issues in framing the query", e);
        }
        return concepts.toArray(new ConceptEntry[concepts.size()]);

    }

    @PreDestroy
    public void destroy() throws LuceneException {
        try {
            reader.close();
            writer.close();
        } catch (IOException ex) {
            throw new LuceneException(ex.getMessage(), ex);
        }
    }
}
