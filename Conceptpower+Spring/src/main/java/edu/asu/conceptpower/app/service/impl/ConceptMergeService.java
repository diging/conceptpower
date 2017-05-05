package edu.asu.conceptpower.app.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.bean.ConceptsMergeBean;
import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.core.IConceptTypesService;
import edu.asu.conceptpower.app.core.IConceptTypesService.IdType;
import edu.asu.conceptpower.app.error.CPError;
import edu.asu.conceptpower.app.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.app.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.service.IConceptMergeService;
import edu.asu.conceptpower.core.ConceptEntry;

@Service
public class ConceptMergeService implements IConceptMergeService {

    @Autowired
    private IConceptManager conceptManager;

    @Autowired
    private IConceptTypesService conceptTypesService;

    @Override
    public ConceptsMergeBean prepareMergeConcepts(List<ConceptEntry> conceptEntries,
            ConceptsMergeBean conceptsMergeBean) {
        boolean typeWarning = false;
        boolean posWarning = false;
        conceptsMergeBean.clear();
        for (ConceptEntry entry : conceptEntries) {
            if (entry.getWord() != null && !entry.getWord().isEmpty()) {
                conceptsMergeBean.setWord(conceptsMergeBean.getWord() == null
                        || conceptsMergeBean.getWord().length() < entry.getWord().length() ? entry.getWord()
                                : conceptsMergeBean.getWord());
            }

            if (conceptsMergeBean.getSelectedPosValue() == null) {
                conceptsMergeBean.setSelectedPosValue(entry.getPos().toLowerCase());
            } else if (!conceptsMergeBean.getSelectedPosValue().equalsIgnoreCase(entry.getPos())) {
                posWarning = true;
            }

            if (entry.getDescription() != null && !entry.getDescription().isEmpty()) {
                conceptsMergeBean.getDescriptions().add(entry.getDescription().trim());
            }

            if (entry.getSynonymIds() != null && !entry.getSynonymIds().isEmpty()) {
                conceptsMergeBean.getSynonymsids().addAll(Arrays.asList(entry.getSynonymIds().split(",")));
            }

            if (entry.getEqualTo() != null && !entry.getEqualTo().isEmpty()) {
                conceptsMergeBean.getEqualsValues().add(entry.getEqualTo());
            }
            if (entry.getSimilarTo() != null && !entry.getSimilarTo().isEmpty()) {
                conceptsMergeBean.getSimilarValues().add(entry.getSimilarTo());
            }

            if (entry.getTypeId() != null && conceptsMergeBean.getSelectedTypeId() == null) {
                conceptsMergeBean.setSelectedTypeId(entry.getTypeId());
            } else if (entry.getTypeId() != null
                    && !entry.getTypeId().equalsIgnoreCase(conceptsMergeBean.getSelectedTypeId())) {
                typeWarning = true;
            }

            conceptsMergeBean.getMergeIds().add(entry.getId());

            if (IdType.LOCAL_CONCEPT_ID == conceptTypesService.getConceptTypeByConceptId(entry.getId())) {
                if(entry.getWordnetId() != null) {
                    conceptsMergeBean.getAlternativeIds().addAll(Arrays.asList(entry.getWordnetId().split(",")));
                }
            }
            conceptsMergeBean.getAlternativeIds().add(entry.getId());
            if (!entry.getAlternativeIds().isEmpty()) {
                conceptsMergeBean.getAlternativeIds().addAll(entry.getAlternativeIds());
            }
        }

        if (posWarning) {
            CPError errroMessage = new CPError("error_message_101");
            conceptsMergeBean.getErrorMessages().add(errroMessage);
        }

        if (typeWarning) {
            CPError errroMessage = new CPError("error_message_102");
            conceptsMergeBean.getErrorMessages().add(errroMessage);
        }

        // By default new id will be created
        conceptsMergeBean.setSelectedConceptId("");
        conceptsMergeBean.setSelectedListName("");

        // Removing all the wordnet ids from this list. Users can merge into any
        // of the user defined CCP id or can create a new id for the merging
        // concepts. As per design cannot merge into wordnet id, because wordnet
        // id concepts cannot be updated.

        return conceptsMergeBean;
    }

    @Override
    public void mergeConcepts(ConceptsMergeBean conceptsMergeBean, String userName)
            throws LuceneException, IndexerRunningException, IllegalAccessException, DictionaryDoesNotExistException,
            DictionaryModifyException {

        Map<String, ConceptEntry> localCCPConceptMap = fetchLocalCCPConcepts(conceptsMergeBean.getMergeIds(),
                conceptsMergeBean.getSelectedConceptId());

        if (conceptsMergeBean.getSelectedConceptId().trim().equals("")) {
            // Add
            ConceptEntry entry = new ConceptEntry();
            fillConceptEntry(entry, conceptsMergeBean);
            conceptManager.addConceptListEntry(entry, userName);
        } else {
            // Update
            ConceptEntry entry = conceptManager.getConceptEntry(conceptsMergeBean.getSelectedConceptId());
            fillConceptEntry(entry, conceptsMergeBean);
            conceptManager.storeModifiedConcept(entry, userName);
        }

        deleteMergedConcepts(userName, conceptsMergeBean, localCCPConceptMap);
    }

    private Map<String, ConceptEntry> fetchLocalCCPConcepts(Set<String> mergeIds,String selectedConceptId) {
        Map<String, ConceptEntry> localCCPConceptMap = new HashMap<>();
        for (String id : mergeIds) {
            if (IdType.SPECIFIC_WORDNET_CONCEPT_ID != conceptTypesService.getConceptTypeByConceptId(id)
                    && !id.equalsIgnoreCase(selectedConceptId.trim())) {
                localCCPConceptMap.put(id, conceptManager.getConceptEntry(id));
            }
        }
        return localCCPConceptMap;
    }

    private void deleteMergedConcepts(String userName, ConceptsMergeBean conceptsMergeBean,
            Map<String, ConceptEntry> localCCPConceptMap) throws LuceneException, IndexerRunningException,
                    IllegalAccessException, DictionaryDoesNotExistException, DictionaryModifyException {

        for (String id : conceptsMergeBean.getMergeIds()) {
            if (IdType.SPECIFIC_WORDNET_CONCEPT_ID == conceptTypesService.getConceptTypeByConceptId(id)) {
                // If its a wordnet concept, then create a wrapper and set
                // the delete flag to true. This is done in two steps
                // because changeevent object needs to be updated for
                // deletion correctly.
                String conceptWrapperId = createConceptWrapperById(id, userName, conceptsMergeBean);
                conceptManager.deleteConcept(conceptManager.getConceptEntry(conceptWrapperId), userName);
            } else if (!id.equalsIgnoreCase(conceptsMergeBean.getSelectedConceptId().trim())) {
                conceptManager.deleteConcept(localCCPConceptMap.get(id), userName);
            }
        }
    }

    private String createConceptWrapperById(String wrapperId, String userName, ConceptsMergeBean conceptsMergeBean)
            throws IllegalAccessException, DictionaryDoesNotExistException, DictionaryModifyException, LuceneException,
            IndexerRunningException {
        ConceptEntry entry = conceptManager.getWordnetConceptEntry(wrapperId);
        // Creating concept wrapper with all the values, because in future we
        // will be including manipulations on deleted wrappers as well.
        // WrapperId has been added to delete the wordnet id. If this wordnet
        // id is not deleted, then the merged wordnet id will be appearing on
        // concept search screen. If WID-1 and WID-2 are merged, then each time
        // we create a wrapper for WID-1 and WID-2, we need to have WID-1 and
        // WID-2 in wordnet id field to be deleted.
        entry.setConceptList(conceptsMergeBean.getSelectedListName());
        return conceptManager.addConceptListEntry(entry, userName).getId();
    }

    private void fillConceptEntry(ConceptEntry entry, ConceptsMergeBean conceptMergeBean) {
        entry.setPos(conceptMergeBean.getSelectedPosValue());
        entry.setConceptList(conceptMergeBean.getSelectedListName());
        entry.setTypeId(conceptMergeBean.getSelectedTypeId());

        String prefix = ",";
        entry.setWord(conceptMergeBean.getWord());

        String description = conceptMergeBean.getDescriptions().stream().map(i -> i.toString().trim())
                .collect(Collectors.joining(" "));
        entry.setDescription(description.trim().replaceAll("\\s+", " "));

        String synonymIds = conceptMergeBean.getSynonymsids().stream().map(i -> i.toString())
                .collect(Collectors.joining(prefix));
        entry.setSynonymIds(synonymIds);

        String equals = conceptMergeBean.getEqualsValues().stream().map(i -> i.toString())
                .collect(Collectors.joining(prefix));
        entry.setEqualTo(equals);

        String similar = conceptMergeBean.getSimilarValues().stream().map(i -> i.toString())
                .collect(Collectors.joining(prefix));
        entry.setSimilarTo(similar);
        
        if (entry.getAlternativeIds() != null) {
            entry.getAlternativeIds().addAll(conceptMergeBean.getAlternativeIds());
        } else {
            entry.setAlternativeIds(conceptMergeBean.getAlternativeIds());
        }

        entry.setMergedIds(conceptMergeBean.getMergeIds().stream().collect(Collectors.joining(",")));
        
    }

}
