package edu.asu.conceptpower.app.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.core.IIndexService;
import edu.asu.conceptpower.app.db4o.DBNames;
import edu.asu.conceptpower.app.db4o.IConceptDBManager;
import edu.asu.conceptpower.app.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.app.exceptions.DictionaryEntryExistsException;
import edu.asu.conceptpower.app.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.wordnet.Constants;
import edu.asu.conceptpower.app.wordnet.WordNetManager;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.rest.LuceneFieldNames;
import edu.asu.conceptpower.rest.SearchFieldNames;
import edu.asu.conceptpower.servlet.core.ChangeEvent;
import edu.asu.conceptpower.servlet.core.ChangeEvent.ChangeEventTypes;

/**
 * This class handles concepts in general. It uses a DB4O client that contains
 * all added concepts and concept wrappers, and a WordNet client that queries
 * WordNet.
 * 
 * @author Julia Damerow
 * 
 */
@Service
@PropertySource("classpath:config.properties")
public class ConceptManager implements IConceptManager {

    @Autowired
    private IConceptDBManager client;

    @Autowired
    private IIndexService indexService;

    @Autowired
    private WordNetManager wordnetManager;
    
    protected final String CONCEPT_PREFIX = "CON";

    @Value("${default_page_size}")
    private Integer defaultPageSize;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.conceptpower.core.IConceptManager#getConceptEntry(java.lang.
     * String)
     */
    @Override
    public ConceptEntry getConceptEntry(String id) {
        ConceptEntry entry = client.getEntry(id);
        if (entry != null) {
            fillConceptEntry(entry);
            return entry;
        }

        entry = wordnetManager.getConcept(id);
        if (entry != null) {
            // client.store(entry, DBNames.WORDNET_CACHE);
            return entry;
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.core.IConceptManager#getWordnetConceptEntry(java.
     * lang.String)
     */
    @Override
    public ConceptEntry getWordnetConceptEntry(String wordnetId) throws LuceneException {
        ConceptEntry entry = wordnetManager.getConcept(wordnetId);
        return entry;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.core.IConceptManager#getConceptListEntriesForWord(
     * java.lang.String, java.lang.String)
     */
    @Override
    public ConceptEntry[] getConceptListEntriesForWord(String word, String pos, String conceptList)
            throws LuceneException, IllegalAccessException, IndexerRunningException {
        if (pos == null)
            return null;
        Map<String,String> fieldMap = new HashMap<String,String>();
        fieldMap.put(SearchFieldNames.WORD, word);
        fieldMap.put(SearchFieldNames.POS, pos);
        fieldMap.put(LuceneFieldNames.CONCEPT_LIST, conceptList);
        return indexService.searchForConcepts(fieldMap,null);
    }

    /**
     * If a concept entry does not wrap a concept from Wordnet then this method
     * does nothing. If a concept does wrap concepts from Wordnet (id and
     * wordnet id are not the same) then this method copies the synonym ids of
     * the wrapped wordnet concepts into the synonym ids field of the wrapping
     * concept entry.
     * 
     * @param entry
     *            The concept entry that should be filled with the synonym ids
     *            of wrapped wordnet concepts.
     */
    protected void fillConceptEntry(ConceptEntry entry) {
        if (entry.getId() != null && entry.getWordnetId() != null && !entry.getId().equals(entry.getWordnetId())) {
            // generate the synonym ids
            StringBuffer sb = new StringBuffer();

            if (entry.getWordnetId() != null) {
                String wordnetIds = entry.getWordnetId();
                String[] ids = wordnetIds.trim().split(Constants.CONCEPT_SEPARATOR);
                if (ids != null) {
                    for (String id : ids) {
                        if (id != null && !id.trim().isEmpty()) {
                            ConceptEntry wordnetEntry = wordnetManager.getConcept(id);
                            if (wordnetEntry != null) {
                                sb.append(wordnetEntry.getSynonymIds());
                            }
                        }
                    }
                }
            }

            entry.setSynonymIds((entry.getSynonymIds() != null ? entry.getSynonymIds() : "")
                    + Constants.CONCEPT_SEPARATOR + sb.toString());

            // Since synonymId contains duplicates placing a check to remove
            // duplicates from synonymId
            String synonymIds = entry.getSynonymIds();
            String[] synonyms = synonymIds.split(Constants.CONCEPT_SEPARATOR);
            StringBuilder uniqueSynonym = new StringBuilder("");
            for (String synonym : synonyms) {
                if (!uniqueSynonym.toString().contains(synonym)) {
                    uniqueSynonym.append(synonym);
                    uniqueSynonym.append(Constants.CONCEPT_SEPARATOR);
                }
            }
            entry.setSynonymIds(uniqueSynonym.toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.core.IConceptManager#searchForConceptsConnectedByOr(
     * java.util.Map)
     */
    @Override
    public ConceptEntry[] searchForConceptsConnectedByOr(Map<String, String> fieldMap) throws LuceneException {
        // TODO fetch from lucene
        List<ConceptEntry> results = new ArrayList<ConceptEntry>();
        List<String> ids = new ArrayList<String>();

        for (String fieldName : fieldMap.keySet()) {
            String searchString = fieldMap.get(fieldName);

            ConceptEntry[] entries = client.getEntriesByFieldContains(fieldName, searchString);

            for (ConceptEntry e : entries) {
                if (e != null) {
                    if (!ids.contains(e.getId())) {
                        fillConceptEntry(e);
                        results.add(e);
                        ids.add(e.getId());
                    }
                }
            }
        }

        return results.toArray(new ConceptEntry[results.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.core.IConceptManager#searchForConceptsConnectedByAnd
     * (java.util.Map)
     */
    @Override
    public ConceptEntry[] searchForConceptsConnectedByAnd(Map<String, String> fieldMap) throws LuceneException {
        // TODO Fetch from lucene
        List<ConceptEntry> results = null;
        List<String> ids = new ArrayList<String>();

        for (String fieldName : fieldMap.keySet()) {
            String searchString = fieldMap.get(fieldName);

            ConceptEntry[] entries = client.getEntriesByFieldContains(fieldName, searchString);

            if (results == null) {
                results = new ArrayList<ConceptEntry>();
                for (ConceptEntry e : entries) {
                    if (e != null) {
                        fillConceptEntry(e);
                        results.add(e);
                        ids.add(e.getId());
                    }
                }
                continue;
            }

            List<ConceptEntry> filteredResults = new ArrayList<ConceptEntry>();
            for (ConceptEntry e : entries) {
                if (e != null) {
                    if (ids.contains(e.getId()))
                        filteredResults.add(e);
                }
            }
            results = filteredResults;
        }

        return results.toArray(new ConceptEntry[results.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.core.IConceptManager#getSynonymsForConcept(java.lang
     * .String)
     */
    @Override
    public ConceptEntry[] getSynonymsForConcept(String id) throws LuceneException {

        ConceptEntry concept = getConceptEntry(id);
        List<ConceptEntry> allEntries = new ArrayList<ConceptEntry>();
        if (concept == null)
            return allEntries.toArray(new ConceptEntry[allEntries.size()]);

        fillConceptEntry(concept);

        List<String> ids = new ArrayList<String>();

        String synonymIds = concept.getSynonymIds();
        if (synonymIds != null && !synonymIds.isEmpty()) {
            String[] synIds = synonymIds.split(",");

            for (String synId : synIds) {
                ConceptEntry synonym = getConceptEntry(synId);
                if (synonym != null && !ids.contains(synId)) {
                    allEntries.add(synonym);
                    ids.add(synId);
                }
            }
        }

        ConceptEntry[] entries = client.getSynonymsPointingToId(id);

        for (ConceptEntry entry : entries) {
            fillConceptEntry(entry);
            if (!ids.contains(entry.getId())) {
                allEntries.add(entry);
                ids.add(entry.getId());
            }
        }

        return allEntries.toArray(new ConceptEntry[allEntries.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.core.IConceptManager#getConceptListEntriesForWord(
     * java.lang.String)
     */
    @Override
    public ConceptEntry[] getConceptListEntriesForWord(String word) throws LuceneException, IllegalAccessException, IndexerRunningException {
        Map<String,String> fieldMap = new HashMap<String,String>();
        fieldMap.put(SearchFieldNames.WORD, word);
        return indexService.searchForConcepts(fieldMap, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.core.IConceptManager#getConceptListEntries(java.lang
     * .String)
     */
    @Override
    public List<ConceptEntry> getConceptListEntries(String conceptList, int page, int pageSize, String sortBy,
            int sortDirection) throws LuceneException {

        if (pageSize == -1) {
            pageSize = defaultPageSize;
        }
        if (page < 1) {
            page = 1;
        }
        int pageCount = getConceptsCountForConceptList(conceptList);
        pageCount = pageCount > 0 ? pageCount : 1;
        if (page > pageCount) {
            page = pageCount;
        }

        List<ConceptEntry> entries = client.getAllEntriesFromList(conceptList, page, pageSize, sortBy, sortDirection);
        Collections.sort(entries, new Comparator<ConceptEntry>() {

            public int compare(ConceptEntry o1, ConceptEntry o2) {
                if (o1.getWord() == o2.getWord()) {
                    return 0;
                } else if (o1.getWord() == null) {
                    return -1;
                } else if (o2.getWord() == null) {
                    return 1;
                } else {
                    return o1.getWord().compareToIgnoreCase(o2.getWord());
                }
            }

        });
        List<ConceptEntry> notDeletedEntries = new ArrayList<ConceptEntry>();
        for (ConceptEntry entry : entries) {
            if (!entry.isDeleted()) {
                fillConceptEntry(entry);
                notDeletedEntries.add(entry);
            }
        }
        return notDeletedEntries;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.core.IConceptManager#addConceptListEntry(java.lang.
     * String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void addConceptListEntry(String word, String pos, String description, String conceptListName, String typeId,
            String equalTo, String similarTo, String synonymIds, String synsetIds, String narrows, String broadens)
                    throws DictionaryDoesNotExistException, DictionaryModifyException, DictionaryEntryExistsException {
        ConceptList dict = client.getConceptList(conceptListName);
        if (dict == null)
            throw new DictionaryDoesNotExistException();

        if (conceptListName.equals(Constants.WORDNET_DICTIONARY)) {
            throw new DictionaryModifyException();
        }

        ConceptEntry entry = new ConceptEntry();
        entry.setWord(word);
        entry.setDescription(description);
        entry.setPos(pos);
        entry.setConceptList(conceptListName);
        entry.setTypeId(typeId);
        entry.setEqualTo(equalTo);
        entry.setSimilarTo(similarTo);
        entry.setSynonymIds(synonymIds);
        entry.setSynsetIds(synsetIds);
        entry.setNarrows(narrows);
        entry.setBroadens(broadens);

        entry.setId(generateId(CONCEPT_PREFIX));

        client.store(entry, DBNames.DICTIONARY_DB);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.core.IConceptManager#addConceptListEntry(edu.asu.
     * conceptpower.core.ConceptEntry)
     */
    @Override
    public String addConceptListEntry(ConceptEntry entry, String userName) throws DictionaryDoesNotExistException,
            DictionaryModifyException, LuceneException, IllegalAccessException, IndexerRunningException {
        ConceptList dict = client.getConceptList(entry.getConceptList());
        if (dict == null)
            throw new DictionaryDoesNotExistException();

        if (entry.getConceptList().equals(Constants.WORDNET_DICTIONARY)) {
            throw new DictionaryModifyException();
        }

        // Creating the first change event
        ChangeEvent changeEvent = new ChangeEvent(userName, new Date(), ChangeEventTypes.CREATION);
        entry.addNewChangeEvent(changeEvent);
        String id = generateId(CONCEPT_PREFIX);
        entry.setId(id);

        client.store(entry, DBNames.DICTIONARY_DB);
        if (entry.getWordnetId() != null) {
            String wordnetId = entry.getWordnetId();
            if (wordnetId.endsWith(",")) {
                wordnetId = wordnetId.substring(0, wordnetId.length() - 1);
            }
            indexService.deleteById(wordnetId);
        }
        indexService.insertConcept(entry);
        return id;
    }
    

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.core.IConceptManager#storeModifiedConcept(edu.asu.
     * conceptpower.core.ConceptEntry)
     */
    @Override
    public void storeModifiedConcept(ConceptEntry entry, String userName)
            throws LuceneException, IllegalAccessException, IndexerRunningException {
        ChangeEvent changeEvent = new ChangeEvent();
        changeEvent.setDate(new Date());
        changeEvent.setUserName(userName);
        changeEvent.setType(ChangeEventTypes.MODIFICATION);
        entry.addNewChangeEvent(changeEvent);

        indexService.updateConceptEntry(entry);

        client.update(entry, DBNames.DICTIONARY_DB);
    }

    protected String generateId(String prefix) {
        while (true) {
            String id = prefix + generateUniqueId();

            ConceptEntry example = new ConceptEntry();
            example.setId(id);
            // if there doesn't exist an object with this id return id
            List<Object> results = client.queryByExample(example);
            if (results == null || results.size() == 0)
                return id;
        }
    }

    /**
     * This methods generates a new 12 character long id. Note that this method
     * does not assure that the id isn't in use yet.
     * 
     * Adapted from
     * http://stackoverflow.com/questions/9543715/generating-human-readable
     * -usable-short-but-unique-ids
     * 
     * @return 12 character id
     */
    private String generateUniqueId() {
        char[] chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            builder.append(chars[random.nextInt(62)]);
        }

        return builder.toString();
    }

    @Override
    public void deleteConcept(String id, String userName) throws LuceneException, IndexerRunningException {
        ConceptEntry concept = getConceptEntry(id);
        concept.setDeleted(true);
        ChangeEvent changeEvent = new ChangeEvent();
        changeEvent.setType(ChangeEventTypes.DELETION);
        changeEvent.setDate(new Date());
        changeEvent.setUserName(userName);
        concept.addNewChangeEvent(changeEvent);
        client.update(concept, DBNames.DICTIONARY_DB);
        indexService.deleteById(concept.getId());
    }

    @Override
    public ConceptEntry getConceptWrappedEntryByWordNetId(String wordNetID)
            throws IllegalAccessException, LuceneException, IndexerRunningException {
        List<ConceptEntry> conceptEntries = client.getConceptByWordnetId(wordNetID);
        for (ConceptEntry entry : conceptEntries) {
            // Wordnet is also added because lucene doesn't do an exact search
            // on fields
            if (entry.getId().contains(CONCEPT_PREFIX) && entry.getWordnetId().contains(wordNetID)) {
                return entry;
            }
        }
        // No concept wrapper found for wordnet id
        return null;
    }

    public int getPageCount(String conceptListName) {
        int totalUploads = getConceptsCountForConceptList(conceptListName);
        return (int) Math.ceil(new Double(totalUploads) / new Double(defaultPageSize));
    }

    private int getConceptsCountForConceptList(String listName) {
        List<ConceptEntry> allEntries = client.getAllEntriesFromList(listName);
        return allEntries.size();
    }

    @Override
    public List<ConceptEntry> getConceptEntryByTypeId(String typeId) {
        return client.getAllEntriesByTypeId(typeId);
    }

    @Override
    public List<ConceptEntry> getConceptEntriedByConceptListName(String conceptListName) {
        return client.getAllEntriesFromList(conceptListName);
    }
}
