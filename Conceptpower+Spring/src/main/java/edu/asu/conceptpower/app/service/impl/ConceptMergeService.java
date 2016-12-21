package edu.asu.conceptpower.app.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

            if (!isNullOrEmpty(entry.getPos())) {
                conceptsMergeBean.setSelectedPosValue(entry.getPos().toLowerCase());
            }

            if (!isNullOrEmpty(entry.getWord())) {
                Set wordList = createSet(entry.getWord(), conceptsMergeBean.getWord());
                conceptsMergeBean.setWord(wordList);
            }

            if (!isNullOrEmpty(entry.getConceptList())) {
                conceptsMergeBean.setSelectedListName(entry.getConceptList());
            }

            if (!isNullOrEmpty(entry.getDescription())) {
                Set descriptionList = createSet(entry.getDescription(), conceptsMergeBean.getDescription());
                conceptsMergeBean.setDescription(descriptionList);
            }

            if (!isNullOrEmpty(entry.getSynonymIds())) {
                Set<String> synonymIds = combineSynonymIds(conceptsMergeBean.getSynonymsids(), entry.getSynonymIds());
                conceptsMergeBean.setSynonymsids(synonymIds);

            }

            if (!isNullOrEmpty(entry.getTypeId())) {
                Set conceptTypeIds = createSet(entry.getTypeId(), conceptsMergeBean.getConceptTypeIdList());
                conceptsMergeBean.setConceptTypeIdList(conceptTypeIds);
            }

            if (!isNullOrEmpty(entry.getEqualTo())) {
                Set<String> equalsSet = createSet(entry.getEqualTo(), conceptsMergeBean.getEquals());
                conceptsMergeBean.setEquals(equalsSet);
            }

            if (!isNullOrEmpty(entry.getSimilarTo())) {
                Set<String> similarSet = createSet(entry.getSimilarTo(), conceptsMergeBean.getSimilar());
                conceptsMergeBean.setSimilar(similarSet);
            }
        }

        // Fetch the types with the type id
        if (conceptsMergeBean.getConceptTypeIdList() != null && !conceptsMergeBean.getConceptTypeIdList().isEmpty()) {
            conceptsMergeBean.setTypes(getTypesByTypeId(conceptsMergeBean.getConceptTypeIdList()));
        }

        // By default new id will be created
        conceptsMergeBean.setSelectedConceptId("");

        // Adding all conceptlists
        conceptsMergeBean.setConceptList(getAllConceptLists());
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
        StringBuffer wordBuffer = new StringBuffer();
        String prefix = "";
        for (String word : conceptMergeBean.getWord()) {
            wordBuffer.append(prefix);
            prefix = ",";
            wordBuffer.append(word);
        }
        entry.setWord(wordBuffer.toString().trim());

        entry.setPos(conceptMergeBean.getSelectedPosValue());
        entry.setConceptList(conceptMergeBean.getSelectedListName());

        StringBuffer descriptionBuffer = new StringBuffer();
        prefix = "";
        for (String description : conceptMergeBean.getDescription()) {
            descriptionBuffer.append(prefix);
            prefix = ",";
            descriptionBuffer.append(description);
        }

        entry.setDescription(descriptionBuffer.toString().trim());

        StringBuffer synonymBuffer = new StringBuffer();
        prefix = "";
        for (String synonymId : conceptMergeBean.getSynonymsids()) {
            synonymBuffer.append(prefix);
            prefix = ",";
            synonymBuffer.append(synonymId);
        }

        entry.setSynonymIds(synonymBuffer.toString());
        entry.setTypeId(conceptMergeBean.getSelectedTypeId());

        StringBuffer equalsBuffer = new StringBuffer();
        prefix = "";
        for (String equals : conceptMergeBean.getEquals()) {
            equalsBuffer.append(prefix);
            prefix = ",";
            equalsBuffer.append(equals);
        }
        entry.setEqualTo(equalsBuffer.toString().trim());

        StringBuffer similarBuffer = new StringBuffer();
        prefix = "";
        for (String similarTo : conceptMergeBean.getSimilar()) {
            similarBuffer.append(prefix);
            prefix = ",";
            similarBuffer.append(similarTo);
        }
        entry.setSimilarTo(similarBuffer.toString().trim());

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
