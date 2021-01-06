package edu.asu.conceptpower.app.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.app.manager.IConceptDBManager;
import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.app.model.ConceptList;
import static edu.asu.conceptpower.app.repository.ConceptEntrySpecification.customFieldSearch;

import edu.asu.conceptpower.app.repository.IConceptEntryRepository;
import edu.asu.conceptpower.app.repository.IConceptListRepository;

@Component
public class DatabaseClient implements IConceptDBManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IConceptEntryRepository conceptEntryRepository;
    
    @Autowired
    private IConceptListRepository conceptListRepository;
    
    @Value("${default_page_size}")
    private Integer defaultPageSize;
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.db4o.IConceptDBManager#getEntry(java.lang.String)
     */
    @Override
    public ConceptEntry getEntry(String id) {
        Optional<ConceptEntry> concept = conceptEntryRepository.findById(id);
        
        return concept.isPresent() ? concept.get() : null;
    }

    @Override
    public List<ConceptEntry> getWrapperEntryByWordnetId(String wordnetId) {
        return Arrays.asList(getEntriesByFieldContains("wordnetId", wordnetId));
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
        
        List<ConceptEntry> results = conceptEntryRepository.findAll(customFieldSearch(field, containsString));
        
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

        List<ConceptEntry> entries = new ArrayList<>();
        for (ConceptEntry entry : allConcepts) {
            if (entry.getPos().equalsIgnoreCase(pos.toLowerCase()) && !entry.isDeleted())
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
        List<ConceptEntry> entries = conceptEntryRepository.getConceptsForGivenSynonymId(id);
        
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
        List<ConceptEntry> entries = new ArrayList<>();
        
        List<ConceptEntry> results = conceptEntryRepository.findByWord(word);

        if (!results.isEmpty()) {
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
    public ConceptList getConceptList(String name) {
        return conceptListRepository.findByConceptListName(name);
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
        page = page< 0 ? 0 : page;

        pageSize = pageSize == -1 ? defaultPageSize : pageSize;

        return conceptEntryRepository.findAll(
                    sortDirection == 1 ?
                    PageRequest.of(page, pageSize, Sort.by(sortBy).ascending()) 
                    :
                    PageRequest.of(page, pageSize, Sort.by(sortBy).descending())).getContent();
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
       return conceptEntryRepository.findAllByConceptList(listname);
    }

    @Override
    public List<ConceptEntry> getAllEntriesByTypeId(String typeId) {
        return conceptEntryRepository.findAllByTypeId(typeId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.conceptpower.db4o.IConceptDBManager#store(java.lang.Object,
     * java.lang.String)
     */
    @Override
    public void store(ConceptEntry element) {
        conceptEntryRepository.save(element);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.db4o.IConceptDBManager#update(edu.asu.conceptpower.
     * core.ConceptEntry, java.lang.String)
     */
    @Override
    public void update(ConceptEntry entry) {
        conceptEntryRepository.save(entry);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.db4o.IConceptDBManager#deleteConceptList(java.lang.
     * String)
     */
    @Override
    public void deleteConceptList(String id) {
        conceptListRepository.deleteById(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.db4o.IConceptDBManager#update(edu.asu.conceptpower.
     * core.ConceptList, java.lang.String, java.lang.String)
     */
    @Override
    public void update(ConceptList list, String listname) {
        ConceptList toBeUpdated = getConceptList(listname);
        toBeUpdated.setConceptListName(list.getConceptListName());
        toBeUpdated.setDescription(list.getDescription());
        conceptListRepository.save(toBeUpdated);
    }

    @Override
    public List<ConceptEntry> getAllConcepts() {
        return conceptEntryRepository.findAll();
    }
    
    @Override
    public List<ConceptList> getAllConceptLists() {
        return conceptListRepository.findAll();
    }
    
    @Override
    public void storeConceptList(ConceptList element) {
        conceptListRepository.save(element);
    }
    
    @Override
    public boolean checkIfConceptListExists(String id) {
        return conceptListRepository.existsById(id);
    }
}
