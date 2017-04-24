package edu.asu.conceptpower.app.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

import edu.asu.conceptpower.app.db4o.DBNames;
import edu.asu.conceptpower.app.db4o.IConceptDBManager;
import edu.asu.conceptpower.app.reflect.SearchField;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.servlet.core.ChangeEvent;

@Component
public class DatabaseClient implements IConceptDBManager {

    private ObjectContainer dictionaryClient;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("conceptDatabaseManager")
    private DatabaseManager dictionary;

    @PostConstruct
    public void init() {
        this.dictionaryClient = dictionary.getClient();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.db4o.IConceptDBManager#getEntry(java.lang.String)
     */
    @Override
    public ConceptEntry getEntry(String id) {
        ConceptEntry exampleEntry = new ConceptEntry();
        exampleEntry.setId(id);
        ObjectSet<ConceptEntry> results = dictionaryClient.queryByExample(exampleEntry);
        // there should only be exactly one object with this id
        if (results.size() == 1)
            return results.get(0);

        return null;
    }

    @Override
    public List<ConceptEntry> getWrapperEntryByWordnetId(String wordnetId) {
        ConceptEntry exampleEntry = new ConceptEntry();
        exampleEntry.setWordnetId(wordnetId);
        return Arrays.asList(getEntriesByFieldContains("wordnetid", wordnetId));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.db4o.IConceptDBManager#queryByExample(java.lang.
     * Object)
     */
    @Override
    public List<Object> queryByExample(Object example) {
        return dictionaryClient.queryByExample(example);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.db4o.IConceptDBManager#getEntriesByFieldContains(
     * java.lang.String, java.lang.String)
     */
    @Override
    public ConceptEntry[] getEntriesByFieldContains(String field, String containsString) {
        if (containsString == null || field == null)
            return new ConceptEntry[0];
        final String fField = field;
        final String fSearchFor = containsString;

        ObjectSet<ConceptEntry> results = dictionaryClient.query(new Predicate<ConceptEntry>() {
            public boolean match(ConceptEntry con) {

                // go through all fields of class to find field that
                // should be searched
                Field[] fields = con.getClass().getDeclaredFields();
                for (Field field : fields) {
                    SearchField searchFieldAnnotation = field.getAnnotation(SearchField.class);
                    if (searchFieldAnnotation != null) {
                        // if we found the field that should be search
                        // through
                        if (searchFieldAnnotation.fieldName().equals(fField)) {
                            String fieldContent = null;
                            // check content
                            try {
                                field.setAccessible(true);
                                Object contentOfField = field.get(con);
                                if (contentOfField instanceof String)
                                    fieldContent = contentOfField.toString();
                            } catch (IllegalArgumentException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                continue;
                            } catch (IllegalAccessException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                continue;
                            }

                            if (fieldContent != null) {
                                if (fieldContent.trim().toLowerCase().contains(fSearchFor.trim().toLowerCase()))
                                    return true;
                            }
                            return false;
                        }
                    }
                }

                return false;
            }
        });

        return results.toArray(new ConceptEntry[results.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.db4o.IConceptDBManager#getEntriesForWord(java.lang.
     * String, java.lang.String)
     */
    @Override
    public ConceptEntry[] getEntriesForWord(String word, String pos) {
        ConceptEntry[] allConcepts = getEntriesForWord(word);

        List<ConceptEntry> entries = new ArrayList<ConceptEntry>();
        for (ConceptEntry entry : allConcepts) {
            if (entry.getPos().toLowerCase().equals(pos.toLowerCase()) && !entry.isDeleted())
                entries.add(entry);
        }

        return entries.toArray(new ConceptEntry[entries.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.db4o.IConceptDBManager#getSynonymsPointingToId(java.
     * lang.String)
     */
    @Override
    public ConceptEntry[] getSynonymsPointingToId(String id) {
        List<ConceptEntry> entries = new ArrayList<ConceptEntry>();
        final String wordId = id;

        ObjectSet<ConceptEntry> results = dictionaryClient.query(new Predicate<ConceptEntry>() {
            public boolean match(ConceptEntry con) {
                return con.getSynonymIds().contains(wordId) && !con.isDeleted();
            }
        });

        if (results.size() > 0) {
            entries.addAll(results);
        }

        return entries.toArray(new ConceptEntry[entries.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.db4o.IConceptDBManager#getEntriesForWord(java.lang.
     * String)
     */
    @Override
    public ConceptEntry[] getEntriesForWord(String word) {

        List<ConceptEntry> entries = new ArrayList<ConceptEntry>();

        // check user build dictionary
        final String fWord = word;
        ObjectSet<ConceptEntry> results = dictionaryClient.query(new Predicate<ConceptEntry>() {
            public boolean match(ConceptEntry con) {
                return con.getWord().replace("_", " ").toLowerCase().contains(fWord.toLowerCase()) && !con.isDeleted();
            }
        });

        if (results.size() > 0) {
            entries.addAll(results);
        }

        return entries.toArray(new ConceptEntry[entries.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.db4o.IConceptDBManager#getConceptList(java.lang.
     * String)
     */
    @Override
    @SuppressWarnings("serial")
    public ConceptList getConceptList(String name) {
        final String fName = name;
        List<ConceptList> dicts = dictionaryClient.query(new Predicate<ConceptList>() {
            public boolean match(ConceptList dict) {
                return dict.getConceptListName().equals(fName);
            }
        });

        if (dicts.size() == 1)
            return dicts.get(0);
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.db4o.IConceptDBManager#getAllElementsOfType(java.
     * lang.Class)
     */
    @Override
    public List<?> getAllElementsOfType(Class<?> clazz) {
        return dictionaryClient.query(clazz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.db4o.IConceptDBManager#getAllEntriesFromList(java.
     * lang.String)
     */
    @Override
    public List<ConceptEntry> getAllEntriesFromList(String listname, int page, int pageSize, final String sortBy,
            final int sortDirection) {
        ConceptEntry entry = new ConceptEntry();
        entry.setConceptList(listname);

        Query dictQuery = dictionaryClient.query();
        dictQuery.constrain(ConceptEntry.class);
        dictQuery.descend("conceptList").constrain(listname);

        try {
            final Field sortField = ConceptEntry.class.getDeclaredField(sortBy);
            sortField.setAccessible(true);

            Comparator<ConceptEntry> conceptEntryComparator = new Comparator<ConceptEntry>() {

                @Override
                public int compare(ConceptEntry o1, ConceptEntry o2) {
                    Object o1FieldContent;
                    Object o2FieldContent;
                    try {
                        if (sortDirection == IConceptDBManager.ASCENDING) {
                            o1FieldContent = sortField.get(o1);
                            o2FieldContent = sortField.get(o2);
                        } else {
                            o2FieldContent = sortField.get(o1);
                            o1FieldContent = sortField.get(o2);
                        }
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        logger.error("Error accessing field.", e);
                        return 0;
                    }

                    if (o1FieldContent instanceof Integer) {
                        return ((Integer) o1FieldContent).compareTo((Integer) o2FieldContent);
                    }
                    return o1FieldContent.toString().compareTo(o2FieldContent.toString());
                }
            };
            
            dictQuery.sortBy(conceptEntryComparator);
        } catch (NoSuchFieldException | SecurityException e) {
            logger.error("Couldn't sort list.", e);
            return null;
        }

        List<ConceptEntry> dictResults = dictQuery.execute();

        int startIndex = (page - 1) * pageSize;
        int endIndex = startIndex + pageSize;
        if (endIndex > dictResults.size()) {
            endIndex = dictResults.size();
        }
        return new ArrayList<ConceptEntry>(dictResults.subList(startIndex, endIndex));
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.db4o.IConceptDBManager#getAllEntriesFromList(java.
     * lang.String)
     */
    @Override
    public List<ConceptEntry> getAllEntriesFromList(String listname) {
        ConceptEntry entry = new ConceptEntry();
        entry.setConceptList(listname);
        return dictionaryClient.queryByExample(entry);
    }

    @Override
    public List<ConceptEntry> getAllEntriesByTypeId(String typeId) {
        ConceptEntry entry = new ConceptEntry();
        entry.setTypeId(typeId);
        return dictionaryClient.queryByExample(entry);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.conceptpower.db4o.IConceptDBManager#store(java.lang.Object,
     * java.lang.String)
     */
    @Override
    public void store(Object element, String databasename) {
        // FIXME no caching?
        // if (databasename.equals(DBNames.WORDNET_CACHE)) {
        // wordnetCacheClient.store(element);
        // wordnetCacheClient.commit();
        // return;
        // }
        if (databasename.equals(DBNames.DICTIONARY_DB)) {
            dictionaryClient.store(element);
            dictionaryClient.commit();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.db4o.IConceptDBManager#update(edu.asu.conceptpower.
     * core.ConceptEntry, java.lang.String)
     */
    @Override
    public void update(ConceptEntry entry, String databasename) {
        if (databasename.equals(DBNames.DICTIONARY_DB)) {
            ConceptEntry toBeUpdated = getEntry(entry.getId());
            toBeUpdated.setBroadens(entry.getBroadens());
            toBeUpdated.setConceptList(entry.getConceptList());
            toBeUpdated.setDescription(entry.getDescription());
            toBeUpdated.setEqualTo(entry.getEqualTo());
            toBeUpdated.setModified(entry.getModified());
            toBeUpdated.setNarrows(entry.getNarrows());
            toBeUpdated.setPos(entry.getPos());
            toBeUpdated.setSimilarTo(entry.getSimilarTo());
            toBeUpdated.setSynonymIds(entry.getSynonymIds());
            toBeUpdated.setSynsetIds(entry.getSynsetIds());
            toBeUpdated.setTypeId(entry.getTypeId());
            toBeUpdated.setWord(entry.getWord());
            toBeUpdated.setWordnetId(entry.getWordnetId());
            toBeUpdated.setDeleted(entry.isDeleted());
            for(ChangeEvent changeEvent : entry.getChangeEvents()) {
                toBeUpdated.addNewChangeEvent(changeEvent);
            }
            
            dictionaryClient.store(toBeUpdated);
            dictionaryClient.commit();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.db4o.IConceptDBManager#deleteConceptList(java.lang.
     * String)
     */
    @Override
    public void deleteConceptList(String name) {
        ConceptList list = new ConceptList();
        list.setConceptListName(name);

        ObjectSet<ConceptList> results = dictionaryClient.queryByExample(list);
        for (ConceptList res : results) {
            dictionaryClient.delete(res);
            dictionaryClient.commit();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.db4o.IConceptDBManager#update(edu.asu.conceptpower.
     * core.ConceptList, java.lang.String, java.lang.String)
     */
    @Override
    public void update(ConceptList list, String listname, String databasename) {
        if (databasename.equals(DBNames.DICTIONARY_DB)) {
            ConceptList toBeUpdated = getConceptList(listname);
            toBeUpdated.setConceptListName(list.getConceptListName());
            toBeUpdated.setDescription(list.getDescription());
            dictionaryClient.store(list);
            dictionaryClient.commit();
        }
    }
}
