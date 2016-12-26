package edu.asu.conceptpower.app.service.impl;

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
import edu.asu.conceptpower.core.ConceptType;

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

            Set wordList = createSet(entry.getWord(), conceptsMergeBean.getWords());
            conceptsMergeBean.setWords(wordList);
            conceptsMergeBean.setSelectedListName(entry.getConceptList());

            Set descriptionList = createSet(entry.getDescription(), conceptsMergeBean.getDescriptions());
            conceptsMergeBean.setDescriptions(descriptionList);

            Set<String> synonymIds = combineSynonymIds(conceptsMergeBean.getSynonymsids(), entry.getSynonymIds());
            conceptsMergeBean.setSynonymsids(synonymIds);

            Set conceptTypeIds = createSet(entry.getTypeId(), conceptsMergeBean.getConceptTypeIdList());
            conceptsMergeBean.setConceptTypeIdList(conceptTypeIds);

            Set<String> equalsSet = createSet(entry.getEqualTo(), conceptsMergeBean.getEqualsValues());
            conceptsMergeBean.setEqualsValues(equalsSet);

            Set<String> similarSet = createSet(entry.getSimilarTo(), conceptsMergeBean.getSimilarValues());
            conceptsMergeBean.setSimilarValues(similarSet);
        }

        // Fetch the types with the type id
        if (conceptsMergeBean.getConceptTypeIdList() != null && !conceptsMergeBean.getConceptTypeIdList().isEmpty()) {
            conceptsMergeBean.setTypes(getTypesByTypeId(conceptsMergeBean.getConceptTypeIdList()));
        }

        // By default new id will be created
        conceptsMergeBean.setSelectedConceptId("");

        // Adding all conceptlists
        conceptsMergeBean.setConceptListValues(getAllConceptLists());
        return conceptsMergeBean;
    }

    @Override
    public void mergeConcepts(ConceptsMergeBean conceptsMergeBean, String userName)
            throws LuceneException, IndexerRunningException, IllegalAccessException, DictionaryDoesNotExistException,
            DictionaryModifyException {

        deleteMergedConcepts(conceptsMergeBean.getConceptIds(), userName,
                conceptsMergeBean.getSelectedConceptId().trim());

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

    private void deleteMergedConcepts(List<String> conceptIds, String userName, String conceptIdToNotDelete)
            throws LuceneException, IndexerRunningException {
        for (String conceptId : conceptIds) {
            if (!conceptId.equalsIgnoreCase(conceptIdToNotDelete)
                    && ConceptTypes.SPECIFIC_WORDNET_CONCEPT != conceptTypesService
                            .getConceptTypeByConceptId(conceptId)) {
                // Wordnet ids cannot be deleted. So adding the wordnet ids to
                // the merged concept entry
                // in getConceptEntryFromConceptMergeBean()
                conceptManager.deleteConcept(conceptId, userName);
            }
        }
    }

    public void getConceptEntryFromConceptMergeBean(ConceptEntry entry, ConceptsMergeBean conceptMergeBean) {
        entry.setPos(conceptMergeBean.getSelectedPosValue());
        entry.setConceptList(conceptMergeBean.getSelectedListName());
        entry.setTypeId(conceptMergeBean.getSelectedTypeId());

        String prefix = ",";
        String words = conceptMergeBean.getWords().stream().map(i -> i.toString()).collect(Collectors.joining(prefix));
        entry.setWord(words);

        String descriptions = conceptMergeBean.getDescriptions().stream().map(i -> i.toString())
                .collect(Collectors.joining(prefix));
        entry.setDescription(descriptions);

        String synonymIds = conceptMergeBean.getSynonymsids().stream().map(i -> i.toString())
                .collect(Collectors.joining(prefix));
        entry.setSynonymIds(synonymIds);

        String equals = conceptMergeBean.getEqualsValues().stream().map(i -> i.toString())
                .collect(Collectors.joining(prefix));
        entry.setEqualTo(equals);

        String similar = conceptMergeBean.getSimilarValues().stream().map(i -> i.toString())
                .collect(Collectors.joining(prefix));
        entry.setSimilarTo(similar);

        addAlternativeIds(entry, conceptMergeBean);
        addMergedIdsToEntry(entry, conceptMergeBean);
        addWordNetIdsToEntry(entry, conceptMergeBean);
    }

    private void addWordNetIdsToEntry(ConceptEntry entry, ConceptsMergeBean conceptMergeBean) {
        StringBuilder wordnetBuilder = new StringBuilder();
        String prefix = "";
        for (String conceptId : conceptMergeBean.getConceptIds()) {
            if (ConceptTypes.SPECIFIC_WORDNET_CONCEPT == conceptTypesService.getConceptTypeByConceptId(conceptId)) {
                wordnetBuilder.append(prefix);
                wordnetBuilder.append(conceptId);
                prefix = ",";
            }
        }
        entry.setWordnetId(wordnetBuilder.toString());
    }

    private void addAlternativeIds(ConceptEntry entry, ConceptsMergeBean conceptMergeBean) {
        List<String> conceptIds = conceptMergeBean.getConceptIds();
        String selectedId = conceptMergeBean.getSelectedConceptId();
        Set<String> alternativeIds = new HashSet<>();

        for (String conceptId : conceptIds) {
            if (!conceptId.equalsIgnoreCase(selectedId)) {
                // Adding current id as alternative id and also its own
                // alternative ids
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
            if (!conceptId.equalsIgnoreCase(selectedId)) {
                mergedIdsBuffer.append(prefix);
                mergedIdsBuffer.append(conceptId);
                prefix = ",";
            }
        }
        entry.setMergedIds(mergedIdsBuffer.toString());
    }

    private Set<String> getAllConceptLists() {
        List<ConceptList> allLists = conceptListManager.getAllConceptLists();
        Set<String> conceptLists = new HashSet<>();
        for (ConceptList conceptList : allLists) {
            conceptLists.add(conceptList.getConceptListName());
        }
        return conceptLists;
    }

    private Set createSet(String value, Set collection) {

        if (isNullOrEmpty(value)) {
            return null;
        }

        if (collection == null) {
            collection = new HashSet();
        }
        collection.add(value);
        return collection;
    }

    private ConceptType[] getTypesByTypeId(Set<String> conceptTypesId) {
        ConceptType[] types = new ConceptType[conceptTypesId.size()];
        int i = 0;
        for (String typeId : conceptTypesId) {
            types[i] = conceptTypeManager.getType(typeId);
            i++;
        }
        return types;
    }

    private boolean isNullOrEmpty(String value) {
        if (value == null) {
            return true;
        }

        if (value.trim().isEmpty()) {
            return true;
        }
        return false;
    }

    private Set<String> combineSynonymIds(Set<String> existingSynonymId, String newSynonymIds) {

        if (newSynonymIds == null) {
            return existingSynonymId;
        }

        if (newSynonymIds.trim().isEmpty()) {
            return existingSynonymId;
        }

        String[] synonymIds = newSynonymIds.split(",");

        for (String synonymId : synonymIds) {
            if (existingSynonymId == null) {
                existingSynonymId = new HashSet<>();
            }
            existingSynonymId.add(synonymId);
        }

        return existingSynonymId;
    }

}
