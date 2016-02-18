package edu.asu.conceptpower.servlet.lucene.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.root.DatabaseClient;
import edu.asu.conceptpower.root.DatabaseManager;
import edu.asu.conceptpower.servlet.core.ConceptEntry;
import edu.asu.conceptpower.servlet.db4o.IConceptDBManager;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.lucene.ILuceneUtility;
import edu.asu.conceptpower.servlet.reflect.LuceneField;
import edu.asu.conceptpower.servlet.reflect.SearchField;
import edu.asu.conceptpower.servlet.rest.LuceneFieldNames;
import edu.asu.conceptpower.servlet.wordnet.Constants;
import edu.asu.conceptpower.servlet.wordnet.WordNetConfiguration;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

@Component
@PropertySource("classpath:config.properties")
public class LuceneUtility implements ILuceneUtility {

    @Autowired
    private WhitespaceAnalyzer whiteSpaceAnalyzer;

    @Autowired
    private WordNetConfiguration configuration;

    @Autowired
    private DatabaseClient databaseClient;
    
    @Autowired
    @Qualifier("luceneDatabaseManager")
    private DatabaseManager luceneManager;

    
    @Autowired
    private IConceptDBManager client;
    
    @Value("${lucenePath}")
    private String lucenePath;

    @Value("${hits}")
    private int hitsPerPage;

    private IndexWriter writer = null;
    private DirectoryReader reader = null;
    private IndexWriterConfig configWhiteSpace = null;
    private Directory index;
    private Path relativePath = null;
    private IndexSearcher searcher = null;

    @PostConstruct
    public void init() throws LuceneException {
        try {
            relativePath = FileSystems.getDefault().getPath(lucenePath, "index");
            index = FSDirectory.open(relativePath);
            configWhiteSpace = new IndexWriterConfig(whiteSpaceAnalyzer);
            writer = new IndexWriter(index, configWhiteSpace);
            reader = DirectoryReader.open(writer, true);
            searcher = new IndexSearcher(reader);
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

    public void insertConcept(ConceptEntry entry) throws LuceneException, IllegalAccessException {
        Document doc = new Document();

        java.lang.reflect.Field[] fields = entry.getClass().getDeclaredFields();
        for (java.lang.reflect.Field field : fields) {

            LuceneField searchFieldAnnotation = field.getAnnotation(LuceneField.class);
            field.setAccessible(true);
            if (searchFieldAnnotation != null) {
                Object contentOfField = field.get(entry);
                if (contentOfField != null) {

                    if (searchFieldAnnotation.isIndexable()) {
                        doc.add(new StringField(searchFieldAnnotation.lucenefieldName(),
                                String.valueOf(contentOfField).replace(" ", ""), Field.Store.YES));
                    } else {
                        doc.add(new StoredField(searchFieldAnnotation.lucenefieldName(),
                                String.valueOf(contentOfField).replace(" ", "")));
                    }
                }
            }
        }

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        doc.add(new StoredField(LuceneFieldNames.MODIFIED_TIME, formatter.format(cal.getTime())));
        doc.add(new StoredField(LuceneFieldNames.ID, entry.getId() != null ? entry.getId() : ""));
        try {
            writer.addDocument(doc);
            writer.commit();
        } catch (IOException ex) {
            throw new LuceneException("Cannot insert concept in lucene. Please retry", ex);
        }

    }

    private ConceptEntry getConceptFromDocument(Document d) throws IllegalAccessException {

        java.lang.reflect.Field[] fields = ConceptEntry.class.getDeclaredFields();
        ConceptEntry con = new ConceptEntry();
        for (java.lang.reflect.Field field : fields) {
            LuceneField luceneFieldAnnotation = field.getAnnotation(LuceneField.class);
            field.setAccessible(true);
            if (luceneFieldAnnotation != null && d.get(luceneFieldAnnotation.lucenefieldName()) != null)
                field.set(con, d.get(luceneFieldAnnotation.lucenefieldName()));
        }
        con.setId(d.get("id"));
        return con;
    }

    @Override
    public void deleteIndexes() throws LuceneException {
        try {
            writer.deleteAll();
            writer.commit();
        } catch (IOException e) {
            throw new LuceneException("Problem in deleting indexes. Please retry", e);
        }
    }

    protected int createDocuments(Iterator<IIndexWord> iterator, IDictionary dict, IndexWriter writer) {
        int numberOfUnIndexedWords = 0;
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
                doc.add(new StringField(LuceneFieldNames.CONCEPT_LIST, Constants.WORDNET_DICTIONARY, Field.Store.YES));
                try {
                    writer.addDocument(doc);
                } catch (IOException e) {
                    numberOfUnIndexedWords++;
                }
            }
        }
        return numberOfUnIndexedWords;
    }

    protected int createDocumentsFromConceptEntries(List<ConceptEntry> conceptEntryList, IndexWriter writer)
            throws IllegalArgumentException, IllegalAccessException {
        int numberOfUnindexConcepts = 0;
        for (ConceptEntry entry : conceptEntryList) {
            Document doc = new Document();

            java.lang.reflect.Field[] fields = entry.getClass().getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {

                LuceneField searchFieldAnnotation = field.getAnnotation(LuceneField.class);
                field.setAccessible(true);
                if (searchFieldAnnotation != null) {
                    Object contentOfField = field.get(entry);
                    if (contentOfField != null) {

                        if (searchFieldAnnotation.isIndexable()) {
                            doc.add(new StringField(searchFieldAnnotation.lucenefieldName(),
                                    String.valueOf(contentOfField).replace(" ", ""), Field.Store.YES));
                        } else {
                            doc.add(new StoredField(searchFieldAnnotation.lucenefieldName(),
                                    String.valueOf(contentOfField).replace(" ", "")));
                        }
                    }
                }
            }
            doc.add(new StoredField(LuceneFieldNames.ID, entry.getId() != null ? entry.getId() : ""));
            try {
                writer.addDocument(doc);
            } catch (IOException ex) {
                numberOfUnindexConcepts++;
            }
        }

        return numberOfUnindexConcepts;
    }

    @Override
    public void indexConcepts() throws LuceneException, IllegalArgumentException, IllegalAccessException {

        String wnhome = configuration.getWordnetPath();
        String path = wnhome + File.separator + configuration.getDictFolder();
        int numberOfUnIndexedWords;

        URL url;
        try {
            url = new URL("file", null, path);
        } catch (MalformedURLException e) {
            throw new LuceneException(e);
        }

        IDictionary dict = new Dictionary(url);
        try {
            dict.open();
        } catch (IOException e) {
            throw new LuceneException("Issues while opening the dictionary", e);
        }

        // 2. Adding data into
        Iterator<IIndexWord> iterator = dict.getIndexWordIterator(POS.NOUN);
        numberOfUnIndexedWords = createDocuments(iterator, dict, writer);

        iterator = dict.getIndexWordIterator(POS.ADVERB);
        numberOfUnIndexedWords += createDocuments(iterator, dict, writer);

        iterator = dict.getIndexWordIterator(POS.ADJECTIVE);
        numberOfUnIndexedWords += createDocuments(iterator, dict, writer);

        iterator = dict.getIndexWordIterator(POS.VERB);
        numberOfUnIndexedWords += createDocuments(iterator, dict, writer);
        try {
            writer.commit();
        } catch (IOException e) {
            throw new LuceneException("Issues in writing document", e);
        }

        // Fetching DB4o Data

        List<ConceptEntry> conceptEntriesList = (List<ConceptEntry>) databaseClient
                .getAllElementsOfType(ConceptEntry.class);

        numberOfUnIndexedWords += createDocumentsFromConceptEntries(conceptEntriesList, writer);

        try {
            writer.commit();
        } catch (IOException e) {
            throw new LuceneException("Issues in writing document", e);
        }

        if (numberOfUnIndexedWords > 0) {
            throw new LuceneException("Indexing not done for " + numberOfUnIndexedWords);
        }
    }

    public ConceptEntry[] queryIndex(Map<String, String> fieldMap, String operator)
            throws LuceneException, IllegalAccessException {

        if (operator == null) {
            operator = "AND";
        }

        StringBuffer queryString = new StringBuffer();
        int firstEntry = 1;

        ConceptEntry con = new ConceptEntry();
        java.lang.reflect.Field[] fields = con.getClass().getDeclaredFields();
        for (java.lang.reflect.Field field : fields) {
            SearchField search = field.getAnnotation(SearchField.class);
            LuceneField luceneFieldAnnotation = field.getAnnotation(LuceneField.class);
            if (search != null) {
                String searchString = fieldMap.get(search.fieldName());
                if (searchString != null) {
                    if (firstEntry != 1)
                        queryString.append(" " + operator + " ");
                    firstEntry++;
                    queryString.append(luceneFieldAnnotation.lucenefieldName() + ":");
                    queryString.append(searchString);
                }
            }
        }

        List<ConceptEntry> concepts = new ArrayList<ConceptEntry>();

        try {
            Query q = new QueryParser("", whiteSpaceAnalyzer).parse(queryString.toString());
            DirectoryReader oldReader = reader;
            reader = DirectoryReader.openIfChanged(oldReader);
            if (reader == null) {
                reader = oldReader;
            } else {
                searcher = new IndexSearcher(reader);
            }
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
