package edu.asu.conceptpower.app.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.bean.ConceptsMergeBean;
import edu.asu.conceptpower.app.core.ConceptTypesService;
import edu.asu.conceptpower.app.core.ConceptTypesService.ConceptTypes;
import edu.asu.conceptpower.app.core.IConceptListManager;
import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.core.IConceptTypeManger;
import edu.asu.conceptpower.app.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.app.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.service.IConceptMergeService;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;

@Service
public class ConceptMergeService implements IConceptMergeService {

    @Autowired
    private IConceptTypeManger conceptTypeManager;

    @Autowired
    private IConceptManager conceptManager;

    @Autowired
    private IConceptListManager conceptListManager;

    @Autowired
    private ConceptTypesService conceptTypesService;

    @Override
    public ConceptsMergeBean prepareMergeConcepts(List<ConceptEntry> conceptEntries,
            ConceptsMergeBean conceptsMergeBean) {
        for (ConceptEntry entry : conceptEntries) {
            conceptsMergeBean.setSelectedPosValue(entry.getPos().toLowerCase());

            Set words = createSet(entry.getWord(), conceptsMergeBean.getWords());
            conceptsMergeBean.setWords(words);
            conceptsMergeBean.setSelectedListName(entry.getConceptList());

            Set descriptions = createSet(entry.getDescription(), conceptsMergeBean.getDescriptions());
            conceptsMergeBean.setDescriptions(descriptions);

            Set<String> synonymIds = combineSynonymIds(conceptsMergeBean.getSynonymsids(), entry.getSynonymIds());
            conceptsMergeBean.setSynonymsids(synonymIds);

            Set<String> equalsSet = createSet(entry.getEqualTo(), conceptsMergeBean.getEqualsValues());
            conceptsMergeBean.setEqualsValues(equalsSet);

            Set<String> similarSet = createSet(entry.getSimilarTo(), conceptsMergeBean.getSimilarValues());
            conceptsMergeBean.setSimilarValues(similarSet);
        }

        // By default new id will be created
        conceptsMergeBean.setSelectedConceptId("");

        // Removing all the wordnet ids from this list. Users can merge into any
        // of the user defined CCP id or can create a new id for the merging
        // concepts. As per design cannot merge into wordnet id, because wordnet
        // id concepts cannot be updated.
        Set<String> wordnetSet = new HashSet<>();
        for (String conceptId : conceptsMergeBean.getConceptIds()) {
            if (ConceptTypes.SPECIFIC_WORDNET_CONCEPT == conceptTypesService.getConceptTypeByConceptId(conceptId)) {
                wordnetSet.add(conceptId);
            }
        }

        conceptsMergeBean.getConceptIds().removeAll(wordnetSet);
        conceptsMergeBean.setWordnetIds(wordnetSet);
        return conceptsMergeBean;
    }

    @Override
    public void mergeConcepts(ConceptsMergeBean conceptsMergeBean, String userName)
            throws LuceneException, IndexerRunningException, IllegalAccessException, DictionaryDoesNotExistException,
            DictionaryModifyException {

        deleteMergedConcepts(conceptsMergeBean.getConceptIds(), userName,
                conceptsMergeBean.getSelectedConceptId().trim(), conceptsMergeBean.getSelectedListName());

        if (conceptsMergeBean.getSelectedConceptId().trim().equals("")) {
            // Add
            ConceptEntry entry = new ConceptEntry();
            getConceptEntryFromConceptMergeBean(entry, conceptsMergeBean);
            conceptManager.addConceptListEntry(entry, userName);
        } else {
            // Update
            ConceptEntry entry = conceptManager.getConceptEntry(conceptsMergeBean.getSelectedConceptId());
            getConceptEntryFromConceptMergeBean(entry, conceptsMergeBean);
            conceptManager.storeModifiedConcept(entry, userName);
        }

    }

    private void deleteMergedConcepts(List<String> conceptIds, String userName, String conceptIdToNotDelete,
            String selectedConceptList) throws LuceneException, IndexerRunningException, IllegalAccessException,
            DictionaryDoesNotExistException, DictionaryModifyException {
        for (String conceptId : conceptIds) {
            if (!conceptId.equalsIgnoreCase(conceptIdToNotDelete)) {
                if (ConceptTypes.SPECIFIC_WORDNET_CONCEPT == conceptTypesService
                            .getConceptTypeByConceptId(conceptId)) {
                    // If its a wordnet concept, then create a wrapper and set
                    // the delete flag to true. This is done in two steps
                    // because changeevent object needs to be updated for
                    // deletion correctly.
                    String conceptWrapperId = createConceptWrapperById(conceptId, userName, selectedConceptList);
                    conceptManager.deleteConcept(conceptWrapperId, userName);
                } else {
                    // If its a CCP concept, just delete it.
                    conceptManager.deleteConcept(conceptId, userName);
                }
            }
        }
    }

    private String createConceptWrapperById(String conceptId, String userName, String conceptListName)
            throws IllegalAccessException, DictionaryDoesNotExistException, DictionaryModifyException, LuceneException,
            IndexerRunningException {
        ConceptEntry entry = conceptManager.getConceptEntry(conceptId);
        entry.setConceptList(conceptListName);
        // Not adding other details of merged concepts. Since its not relevant
        // to add these details here.
        return conceptManager.addConceptListEntry(entry, userName);
    }

    public void getConceptEntryFromConceptMergeBean(ConceptEntry entry, ConceptsMergeBean conceptMergeBean) {
        entry.setPos(conceptMergeBean.getSelectedPosValue());
        entry.setConceptList(conceptMergeBean.getSelectedListName());
        entry.setTypeId(conceptMergeBean.getSelectedTypeId());

        String prefix = ",";
        String words = conceptMergeBean.getWords().stream().map(i -> i.toString()).collect(Collectors.joining(prefix));
        entry.setWord(words);

        String description = conceptMergeBean.getDescriptions().stream().map(i -> i.toString())
                .collect(Collectors.joining(prefix));
        entry.setDescription(description);

        String synonymIds = conceptMergeBean.getSynonymsids().stream().map(i -> i.toString())
                .collect(Collectors.joining(prefix));
        entry.setSynonymIds(synonymIds);

        String equals = conceptMergeBean.getEqualsValues().stream().map(i -> i.toString())
                .collect(Collectors.joining(prefix));
        entry.setEqualTo(equals);

        String similar = conceptMergeBean.getSimilarValues().stream().map(i -> i.toString())
                .collect(Collectors.joining(prefix));
        entry.setSimilarTo(similar);

        String wordnetIds = conceptMergeBean.getWordnetIds().stream().map(i -> i.toString())
                .collect(Collectors.joining(prefix));
        entry.setWordnetId(wordnetIds);

        addAlternativeIdsToEntry(entry, conceptMergeBean);
        addMergedIdsToEntry(entry, conceptMergeBean);
    }

    private void addWordNetIdsToEntry(ConceptEntry entry, ConceptsMergeBean conceptMergeBean) {
        StringBuilder wordnetBuilder = new StringBuilder();
        String prefix = "";
        for (String conceptId : conceptMergeBean.getConceptIds()) {
            wordnetBuilder.append(prefix);
            wordnetBuilder.append(conceptId);
            prefix = ",";
        }
        entry.setWordnetId(wordnetBuilder.toString());
    }

    private void addAlternativeIdsToEntry(ConceptEntry entry, ConceptsMergeBean conceptMergeBean) {
        List<String> conceptIds = conceptMergeBean.getConceptIds();
        String selectedId = conceptMergeBean.getSelectedConceptId();
        Set<String> alternativeIds = new HashSet<>();

        for (String conceptId : conceptIds) {
            if (!conceptId.equalsIgnoreCase(selectedId) || ConceptTypes.SPECIFIC_WORDNET_CONCEPT == conceptTypesService
                    .getConceptTypeByConceptId(selectedId)) {
                alternativeIds.add(conceptId);
                ConceptEntry conceptEntry = conceptManager.getConceptEntry(conceptId);
                alternativeIds.addAll(conceptEntry.getAlternativeIds());
            }
        }
        entry.setAlternativeIds(alternativeIds);
    }

    private void addMergedIdsToEntry(ConceptEntry entry, ConceptsMergeBean conceptMergeBean) {
        List<String> conceptIds = conceptMergeBean.getConceptIds();
        String selectedId = conceptMergeBean.getSelectedConceptId();
        
        StringBuffer mergedIdsBuffer = new StringBuffer();
        String prefix = "";
        for (String conceptId : conceptIds) {
            if (!conceptId.equalsIgnoreCase(selectedId) || ConceptTypes.SPECIFIC_WORDNET_CONCEPT == conceptTypesService
                    .getConceptTypeByConceptId(selectedId)) {
                // If its wordnet add it to mergedIds because for wordnet we
                // will be creating a new wrapper.
                mergedIdsBuffer.append(prefix);
                mergedIdsBuffer.append(conceptId);
                prefix = ",";
            }
        }
        entry.setMergedIds(mergedIdsBuffer.toString());
    }

    @Override
    public Set<String> getAllConceptLists() {
        List<ConceptList> allLists = conceptListManager.getAllConceptLists();
        Set<String> conceptLists = new HashSet<>();
        for (ConceptList conceptList : allLists) {
            conceptLists.add(conceptList.getConceptListName());
        }
        return conceptLists;
    }

    private Set createSet(String value, Set collection) {
        if (value == null) {
            return null;
        }
        if (value.trim().isEmpty()) {
            return null;
        }
        if (collection == null) {
            collection = new HashSet();
        }
        collection.add(value);
        return collection;
    }

    private Set<String> combineSynonymIds(Set<String> existingSynonymIds, String newSynonymIds) {

        if (newSynonymIds == null || newSynonymIds.trim().isEmpty()) {
            return existingSynonymIds;
        }

        if (existingSynonymIds == null) {
            existingSynonymIds = new HashSet<>();
        }

        String[] synonymIds = newSynonymIds.split(",");
        existingSynonymIds.addAll(Arrays.asList(synonymIds));
        return existingSynonymIds;
    }

}
