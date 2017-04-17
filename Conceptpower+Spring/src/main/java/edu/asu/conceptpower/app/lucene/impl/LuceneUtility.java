package edu.asu.conceptpower.app.lucene.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.app.constants.LuceneFieldNames;
import edu.asu.conceptpower.app.db4o.IConceptDBManager;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.lucene.ILuceneDAO;
import edu.asu.conceptpower.app.lucene.ILuceneUtility;
import edu.asu.conceptpower.app.lucene.LuceneAction;
import edu.asu.conceptpower.app.reflect.LuceneField;
import edu.asu.conceptpower.app.reflect.SearchField;
import edu.asu.conceptpower.app.wordnet.Constants;
import edu.asu.conceptpower.app.wordnet.WordNetConfiguration;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.IndexingEvent;
import edu.asu.conceptpower.rest.SearchParamters;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

/**
 * This class creates, deletes indexes for all concepts Performs searching on
 * various indexes
 * 
 * @author karthikeyanmohan
 *
 */
@Component
@PropertySource("classpath:/config.properties")
public class LuceneUtility implements ILuceneUtility {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StandardAnalyzer standardAnalyzer;

    @Autowired
    private WordNetConfiguration configuration;

    @Autowired
    private IConceptDBManager databaseClient;

    @Autowired
    private WhitespaceAnalyzer whiteSpaceAnalyzer;

    @Autowired
    private KeywordAnalyzer keywordAnalyzer;

    @Autowired
    private Environment env;

    @Autowired
    @Qualifier("luceneDAO")
    private ILuceneDAO luceneDAO;

    private String lucenePath;

    private int numberOfResults;

    private IndexWriter writer = null;
    private DirectoryReader reader = null;
    private IndexWriterConfig configWhiteSpace = null;
    private Directory index;
    private Path relativePath = null;
    private IndexSearcher searcher = null;

    /**
     * 
     * @throws LuceneException
     */
    @PostConstruct
    public void init() throws LuceneException {
        lucenePath = env.getProperty("lucenePath");
        numberOfResults = Integer.parseInt(env.getProperty("numberOfLuceneResults"));
        try {
            relativePath = FileSystems.getDefault().getPath(lucenePath, "index");
            index = FSDirectory.open(relativePath);
            configWhiteSpace = new IndexWriterConfig(standardAnalyzer);
            writer = new IndexWriter(index, configWhiteSpace);
            reader = DirectoryReader.open(writer, true);
            searcher = new IndexSearcher(reader);
        } catch (IOException ex) {
            throw new LuceneException("Restart the application", ex);
        }
    }

    /**
     * This method deletes the index based on the id
     * 
     * @throws LuceneException
     */
    public void deleteById(String id, String userName) throws LuceneException {
        try {
            writer.deleteDocuments(new Term(LuceneFieldNames.ID, id));
            writer.commit();
            reloadReader();
        } catch (IOException ex) {
            throw new LuceneException("Issues in deletion. Please retry", ex);
        }
        IndexingEvent event = new IndexingEvent(new Date(), -1, LuceneAction.DELETE, userName);
        luceneDAO.storeValues(event);
    }

    /**
     * This method is used for iterating over the concept entry and fetching the
     * values to be stored into lucene index After storing the values in index,
     * index count is increased in tables in lucene database
     */
    public void insertConcept(ConceptEntry entry, String userName) throws LuceneException, IllegalAccessException {
        Document doc = new Document();

        java.lang.reflect.Field[] fields = entry.getClass().getDeclaredFields();
        for (java.lang.reflect.Field field : fields) {

            LuceneField searchFieldAnnotation = field.getAnnotation(LuceneField.class);
            field.setAccessible(true);
            if (searchFieldAnnotation != null) {
                Object contentOfField = field.get(entry);
                if (contentOfField != null) {
                    if (searchFieldAnnotation.isTokenized()) {
                        if (searchFieldAnnotation.isMultiple()) {
                            String[] contents = String.valueOf(contentOfField).split(",");
                            for (String content : contents) {
                                doc.add(new TextField(searchFieldAnnotation.lucenefieldName(), content,
                                        Field.Store.YES));
                            }
                        } else {
                            doc.add(new TextField(searchFieldAnnotation.lucenefieldName(),
                                    String.valueOf(contentOfField), Field.Store.YES));
                        }
                    } else {
                        // Non tokenized
                        if (searchFieldAnnotation.isMultiple()) {
                            String[] contents = String.valueOf(contentOfField).split(",");
                            for (String content : contents) {
                                doc.add(new StringField(searchFieldAnnotation.lucenefieldName(), content,
                                        Field.Store.YES));
                            }
                        } else {
                            doc.add(new StringField(searchFieldAnnotation.lucenefieldName(),
                                    String.valueOf(contentOfField), Field.Store.YES));
                        }

                    }

                    if (searchFieldAnnotation.isShortPhraseSearchable()) {
                        doc.add(new StringField(searchFieldAnnotation.lucenefieldName() + LuceneFieldNames.UNTOKENIZED_SUFFIX,
                                String.valueOf(contentOfField), Field.Store.YES));
                    }
                }
            }
        }

        try {
            writer.addDocument(doc);
            writer.commit();
            reloadReader();
        } catch (IOException ex) {
            throw new LuceneException("Cannot insert concept in lucene. Please retry", ex);
        }
        IndexingEvent bean = new IndexingEvent(new Date(), 1, LuceneAction.INSERT, userName);
        luceneDAO.storeValues(bean);
    }

    /**
     * This method fetches the values from the retrived document from lucene
     * 
     * @param d
     * @return
     * @throws IllegalAccessException
     */
    private ConceptEntry getConceptFromDocument(Document d) throws IllegalAccessException {

        java.lang.reflect.Field[] fields = ConceptEntry.class.getDeclaredFields();
        ConceptEntry con = new ConceptEntry();
        for (java.lang.reflect.Field field : fields) {
            LuceneField luceneFieldAnnotation = field.getAnnotation(LuceneField.class);
            field.setAccessible(true);
            if (luceneFieldAnnotation != null && d.get(luceneFieldAnnotation.lucenefieldName()) != null)
                if (!luceneFieldAnnotation.isMultiple()) {
                    IndexableField[] indexableFields = d.getFields(luceneFieldAnnotation.lucenefieldName());
                    String content = Arrays.asList(indexableFields).stream().filter(iF -> iF.stringValue() != null)
                            .map(iF -> iF.stringValue()).collect(Collectors.joining(","));
                    field.set(con, content);
                    
                } else {
                    field.set(con, d.get(luceneFieldAnnotation.lucenefieldName()));
                }

        }
        return con;
    }

    /**
     * This method deletes all the indexes in the lucene After deleting the
     * index count is decremented in table
     */
    @Override
    public void deleteIndexes(String userName) throws LuceneException {
        try {
            writer.deleteAll();
            writer.commit();
            reloadReader();
        } catch (IOException e) {
            throw new LuceneException("Problem in deleting indexes. Please retry", e);
        }
        IndexingEvent bean = luceneDAO.getTotalNumberOfWordsIndexed();
        IndexingEvent updatedBean = new IndexingEvent(new Date(), -bean.getIndexedWordsCount(), LuceneAction.DELETE,
                userName);
        luceneDAO.storeValues(updatedBean);
    }

    /**
     * This method iterates over the index word and from each index word the
     * concept details are fetched and stored into lucene index
     * 
     * @param iterator
     * @param dict
     * @param writer
     * @return
     */
    protected int[] createDocuments(Iterator<IIndexWord> iterator, IDictionary dict, IndexWriter writer,
            Set<String> wordnetIdsOfWrappers) {
        int numberOfIndexedWords = 0;
        int numberOfUnIndexedWords = 0;
        for (; iterator.hasNext();) {
            IIndexWord indexWord = iterator.next();
            List<IWordID> wordIdds = indexWord.getWordIDs();

            for (IWordID wordId : wordIdds) {
                IWord word = dict.getWord(wordId);
                if (!wordnetIdsOfWrappers.contains(word.getID().toString())) {
                    Document doc = createIndividualDocument(dict, wordId);
                    try {
                        numberOfIndexedWords++;
                        writer.addDocument(doc);
                    } catch (IOException e) {
                        numberOfUnIndexedWords++;
                    }
                }
            }
        }
        int[] returnValue = { numberOfIndexedWords, numberOfUnIndexedWords };
        return returnValue;
    }

    private Document createIndividualDocument(IDictionary dict, IWordID wordId) {
        Document doc = new Document();
        String lemma = wordId.getLemma().replace("_", " ");
        doc.add(new TextField(LuceneFieldNames.WORD, lemma, Field.Store.YES));
        doc.add(new StringField(LuceneFieldNames.WORD + LuceneFieldNames.UNTOKENIZED_SUFFIX, lemma, Field.Store.YES));
        doc.add(new StringField(LuceneFieldNames.POS, wordId.getPOS().toString(), Field.Store.YES));

        IWord word = dict.getWord(wordId);
        doc.add(new TextField(LuceneFieldNames.DESCRIPTION, word.getSynset().getGloss(), Field.Store.YES));
        doc.add(new StringField(LuceneFieldNames.ID, word.getID().toString(), Field.Store.YES));
        doc.add(new StringField(LuceneFieldNames.WORDNETID, word.getID().toString(), Field.Store.YES));

        ISynset synset = word.getSynset();
        List<IWord> synonyms = synset.getWords();
        StringBuffer sb = new StringBuffer();
        for (IWord syn : synonyms) {
            if (!syn.getID().equals(word.getID()))
                sb.append(syn.getID().toString() + edu.asu.conceptpower.app.core.Constants.SYNONYM_SEPARATOR);
        }
        doc.add(new TextField(LuceneFieldNames.SYNONYMID, sb.toString(), Field.Store.YES));
        // Adding this new data to delete only wordnet concepts while
        // adding all wordnet concepts from jwi.
        doc.add(new StringField(LuceneFieldNames.CONCEPT_LIST, Constants.WORDNET_DICTIONARY, Field.Store.YES));
        return doc;
    }

    /**
     * This method will create the index for the concepts created by user and
     * the concepts that are stored in the CCP database
     * 
     * @param conceptEntryList
     * @param writer
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    protected int[] createDocumentsFromConceptEntries(List<ConceptEntry> conceptEntryList, String userName)
            throws IllegalArgumentException, IllegalAccessException {
        int numberOfIndexedConcepts = 0;
        int numberOfUnindexConcepts = 0;
        for (ConceptEntry entry : conceptEntryList) {
            if (!entry.isDeleted()) {
                try {
                    insertConcept(entry, userName);
                    numberOfIndexedConcepts++;
                } catch (LuceneException e) {
                    numberOfUnindexConcepts++;
                }
            }
        }
        int[] returnValue = new int[2];
        returnValue[0] = numberOfIndexedConcepts;
        returnValue[1] = numberOfUnindexConcepts;
        return returnValue;
    }

    /**
     * This method calls createDocument and creates the documents for all
     * different POS and loads the data into lucene index
     */
    @Override
    public void indexConcepts(String userName)
            throws LuceneException, IllegalArgumentException, IllegalAccessException {

        String wnhome = configuration.getWordnetPath();
        String path = wnhome + File.separator + configuration.getDictFolder();
        
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

        // Fetching DB4o Data
        List<ConceptEntry> conceptEntriesList = (List<ConceptEntry>) databaseClient
                .getAllElementsOfType(ConceptEntry.class);

        Set<String> wordnetIdsOfWrappers = getWordNetIdsOfWrappers(conceptEntriesList);

        // 2. Adding data into
        Iterator<IIndexWord> iterator = dict.getIndexWordIterator(POS.NOUN);
        int[] numberOfWord = createDocuments(iterator, dict, writer, wordnetIdsOfWrappers);

        int numberOfIndexedWords = numberOfWord[0];
        int numberOfUnIndexedWords = numberOfWord[1];

        iterator = dict.getIndexWordIterator(POS.ADVERB);
        numberOfWord = createDocuments(iterator, dict, writer, wordnetIdsOfWrappers);

        numberOfIndexedWords += numberOfWord[0];
        numberOfUnIndexedWords += numberOfWord[1];

        iterator = dict.getIndexWordIterator(POS.ADJECTIVE);
        numberOfWord = createDocuments(iterator, dict, writer, wordnetIdsOfWrappers);

        numberOfIndexedWords += numberOfWord[0];
        numberOfUnIndexedWords += numberOfWord[1];

        iterator = dict.getIndexWordIterator(POS.VERB);
        numberOfWord = createDocuments(iterator, dict, writer, wordnetIdsOfWrappers);

        numberOfIndexedWords += numberOfWord[0];
        numberOfUnIndexedWords += numberOfWord[1];

        try {
            writer.commit();
            reloadReader();
        } catch (IOException e) {
            throw new LuceneException("Issues in writing document", e);
        }


        numberOfWord = createDocumentsFromConceptEntries(conceptEntriesList, userName);
        numberOfIndexedWords += numberOfWord[0];
        numberOfUnIndexedWords += numberOfWord[1];

        try {
            writer.commit();
            reloadReader();
        } catch (IOException e) {
            throw new LuceneException("Issues in writing document", e);
        }

        IndexingEvent event = new IndexingEvent(new Date(), numberOfIndexedWords, LuceneAction.REINDEX, userName);
        luceneDAO.storeValues(event);

        if (numberOfUnIndexedWords > 0) {
            throw new LuceneException("Indexing not done for " + numberOfUnIndexedWords);
        }
    }

    /**
     * This method fetches the concept power by iterating the fieldMap. The
     * fieldMap contains the search criteria
     */
    public ConceptEntry[] queryIndex(Map<String, String> fieldMap, String operator, int page,
            int numberOfRecordsPerPage) throws LuceneException, IllegalAccessException {

        Map<String,Analyzer> analyzerPerField = new HashMap<>();
        BooleanClause.Occur occur = BooleanClause.Occur.SHOULD;
        if (operator == null || operator.equalsIgnoreCase(SearchParamters.OP_AND)) {
            occur = BooleanClause.Occur.MUST;
        }

        java.lang.reflect.Field[] fields = ConceptEntry.class.getDeclaredFields();

        for (java.lang.reflect.Field field : fields) {
            LuceneField luceneFieldAnnotation = field.getAnnotation(LuceneField.class);
            if (luceneFieldAnnotation != null) {
                if (!luceneFieldAnnotation.isShortPhraseSearchable() && !luceneFieldAnnotation.isTokenized()) {
                    // If the field is not tokenzied, then the field needs to be
                    // analyzed using a whitespaceanalyzer, rather than standard
                    // analyzer. This is because for non tokenized strings we
                    // need exact matches and not all the nearest matches.
                    analyzerPerField.put(luceneFieldAnnotation.lucenefieldName(), whiteSpaceAnalyzer);
                } if(luceneFieldAnnotation.isShortPhraseSearchable()) {
                    analyzerPerField.put(luceneFieldAnnotation.lucenefieldName() + LuceneFieldNames.UNTOKENIZED_SUFFIX,
                            keywordAnalyzer);
                }
            }

        }

        PerFieldAnalyzerWrapper perFieldAnalyzerWrapper = new PerFieldAnalyzerWrapper(standardAnalyzer,
                analyzerPerField);

        QueryBuilder qBuild = new QueryBuilder(perFieldAnalyzerWrapper);
        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        for (java.lang.reflect.Field field : fields) {
            SearchField search = field.getAnnotation(SearchField.class);
            LuceneField luceneFieldAnnotation = field.getAnnotation(LuceneField.class);
            if (search != null) {
                String searchString = fieldMap.get(search.fieldName());
                if (searchString != null) {

                    if (!luceneFieldAnnotation.isShortPhraseSearchable() && luceneFieldAnnotation.isTokenized()) {
                        Query q = qBuild.createPhraseQuery(luceneFieldAnnotation.lucenefieldName(), searchString);
                        builder.add(q, occur);
                        createWildCardSearchQuery(luceneFieldAnnotation, searchString, builder);
                    } else if (!luceneFieldAnnotation.isShortPhraseSearchable()
                            && !luceneFieldAnnotation.isTokenized()) {
                        Query q = qBuild.createBooleanQuery(luceneFieldAnnotation.lucenefieldName(), searchString);
                        builder.add(q, occur);
                        createWildCardSearchQuery(luceneFieldAnnotation, searchString, builder);
                    } else {
                        QueryBuilder shortTermsQueryBuilder = new QueryBuilder(perFieldAnalyzerWrapper);
                        BooleanQuery.Builder shortTermBooleanQueryBuilder = new BooleanQuery.Builder();

                        String[] searchStrings = searchString.split(" ");
                        for (String searchValue : searchStrings) {
                            Query q1 = shortTermsQueryBuilder.createPhraseQuery(luceneFieldAnnotation.lucenefieldName(),
                                    searchValue);
                            if (q1 != null) {
                                // Q1 can be null when the search string is very
                                // short such as "be", but the search can be
                                // performed using the BooleanQuery q2.
                                shortTermBooleanQueryBuilder.add(q1, BooleanClause.Occur.SHOULD);
                            }
                        }
                        Query q2 = shortTermsQueryBuilder.createBooleanQuery(
                                luceneFieldAnnotation.lucenefieldName() + LuceneFieldNames.UNTOKENIZED_SUFFIX,
                                searchString);
                        shortTermBooleanQueryBuilder.add(q2, BooleanClause.Occur.SHOULD);
                        createWildCardSearchQuery(luceneFieldAnnotation, searchString, shortTermBooleanQueryBuilder);
                        builder.add(shortTermBooleanQueryBuilder.build(), occur);
                    }
                }
            }
        }

        List<ConceptEntry> concepts = new ArrayList<ConceptEntry>();

        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(numberOfResults);
            int startIndex = 0;
            int hitsPerPage = 0;
            if (page > 0) {
                // page number starts with 1.
                startIndex = calculateStartIndex(page, numberOfRecordsPerPage);
                hitsPerPage = numberOfRecordsPerPage;
            } else if (numberOfRecordsPerPage > 0) {
                // This condition will be called when user requests with a
                // particular number of results and we need to return all the
                // page result. Page will be -1
                startIndex = 0;
                hitsPerPage = numberOfRecordsPerPage;
            } else {
                // Fetching results without pagination.
                // Start index 0 to end Index --> 100 (default we fetch top 100
                // records)
                startIndex = 0;
                hitsPerPage = numberOfResults;
            }

            searcher.search(builder.build(), collector);
            // If page number is more than the available results, we just pass
            // empty result.
            TopDocs topDocs = collector.topDocs(startIndex, hitsPerPage);
            ScoreDoc[] hits = topDocs.scoreDocs;
            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                ConceptEntry entry = getConceptFromDocument(d);
                concepts.add(entry);
            }
        }

        catch (IOException ex) {
            throw new LuceneException("Issues in querying lucene index. Please retry", ex);
        }
        logger.debug("Number of concepts retrieved from lucene = " + concepts.size());
        return concepts.toArray(new ConceptEntry[concepts.size()]);

    }

    private void createWildCardSearchQuery(LuceneField luceneFieldAnnotation, String searchString,
            BooleanQuery.Builder queryBuilder) {
        if (luceneFieldAnnotation.isWildCardSearchEnabled()) {
            Term t = new Term(luceneFieldAnnotation.lucenefieldName(), searchString.toLowerCase());
            WildcardQuery wildCardQuery = new WildcardQuery(t);
            wildCardQuery.setRewriteMethod(MultiTermQuery.SCORING_BOOLEAN_REWRITE);
            queryBuilder.add(wildCardQuery, BooleanClause.Occur.SHOULD);
        }
    }

    private int calculateStartIndex(int page, int numberOfRecordsPerPage) {
        return (page - 1) * numberOfRecordsPerPage;
    }

    /**
     * This method reloads the reader after every update to the index
     * 
     * @throws IOException
     */
    private void reloadReader() throws IOException {
        DirectoryReader oldReader = reader;
        reader = DirectoryReader.openIfChanged(oldReader);
        if (reader == null) {
            reader = oldReader;
        } else {
            searcher = new IndexSearcher(reader);
        }
    }

    /**
     * This method fetches the wordnet ids associated with the concept wrappers.
     * These wordnet ids will not be indexed using lucene.
     * 
     * @param conceptEntries
     * @return
     */
    private Set<String> getWordNetIdsOfWrappers(List<ConceptEntry> conceptEntries) {
        Set<String> wordnetIds = new HashSet<>();
        for (ConceptEntry conceptEntry : conceptEntries) {
            if (conceptEntry.getWordnetId() != null && !conceptEntry.getWordnetId().trim().isEmpty()) {
                String[] wordNetIds = conceptEntry.getWordnetId().split(",");
                wordnetIds.addAll(Arrays.asList(wordNetIds));
            }
        }
        return wordnetIds;
    }

    private boolean containsWildCardCharacters(String searchString) {
        return (searchString.contains("?") || searchString.contains("*"));
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
