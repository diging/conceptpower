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
import edu.asu.conceptpower.app.core.IConceptManager;
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
    private ConceptTypesService conceptTypesService;

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
                
                String conceptIdWithName = entry.getWord().trim() + " -- " + entry.getId();
                conceptsMergeBean.getConceptNamesWithIds().add(conceptIdWithName);
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
            } else if (entry.getTypeId() != null) {
                typeWarning = true;
            }

            if (entry.getWordnetId() != null && !entry.getWordnetId().isEmpty()) {
                String[] wordNetIds = entry.getWordnetId().split(",");
                conceptsMergeBean.getMergedIds()
                        .addAll(Arrays.asList(wordNetIds).stream()
                                .filter(wordNetId -> !wordNetId.trim().equalsIgnoreCase("")).map(wordNetId -> wordNetId)
                                .collect(Collectors.toSet()));
            }
        }

        if (posWarning) {
            CPError errroMessage = new CPError("error_message_101",
                    "There are difference in pos within the merging concepts.");
            conceptsMergeBean.getErrorMessages().add(errroMessage);
        }

        if (typeWarning) {
            CPError errroMessage = new CPError("error_message_102",
                    "Merging concepts have different concept type.");
            conceptsMergeBean.getErrorMessages().add(errroMessage);
        }

        // By default new id will be created
        conceptsMergeBean.setSelectedConceptId("");
        conceptsMergeBean.setSelectedListName("");

        // Removing all the wordnet ids from this list. Users can merge into any
        // of the user defined CCP id or can create a new id for the merging
        // concepts. As per design cannot merge into wordnet id, because wordnet
        // id concepts cannot be updated.

        Set<String> wordnetSet = conceptsMergeBean.getConceptIds().stream()
                .filter(conceptId -> ConceptTypes.SPECIFIC_WORDNET_CONCEPT == conceptTypesService
                        .getConceptTypeByConceptId(conceptId))
                .collect(Collectors.toSet());

        conceptsMergeBean.getConceptIds().removeAll(wordnetSet);
        conceptsMergeBean.setConceptWordnetIds(wordnetSet);
        return conceptsMergeBean;
    }

    @Override
    public void mergeConcepts(ConceptsMergeBean conceptsMergeBean, String userName)
            throws LuceneException, IndexerRunningException, IllegalAccessException, DictionaryDoesNotExistException,
            DictionaryModifyException {

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

        deleteMergedConcepts(userName, conceptsMergeBean);
    }

    private void deleteMergedConcepts(String userName, ConceptsMergeBean conceptsMergeBean)
            throws LuceneException, IndexerRunningException, IllegalAccessException,
            DictionaryDoesNotExistException, DictionaryModifyException {

        for (String wordnetId : conceptsMergeBean.getConceptWordnetIds()) {
            // If its a wordnet concept, then create a wrapper and set
            // the delete flag to true. This is done in two steps
            // because changeevent object needs to be updated for
            // deletion correctly.
            String conceptWrapperId = createConceptWrapperById(wordnetId, userName, conceptsMergeBean);
            conceptManager.deleteConcept(conceptWrapperId, userName);
        }

        for (String conceptId : conceptsMergeBean.getConceptIds()) {
            if (!conceptId.equalsIgnoreCase(conceptsMergeBean.getSelectedConceptId().trim())) {
                // If its a CCP concept, just delete it.
                conceptManager.deleteConcept(conceptId, userName);
            }
        }
    }

    private String createConceptWrapperById(String wrapperId, String userName, ConceptsMergeBean conceptsMergeBean)
            throws IllegalAccessException, DictionaryDoesNotExistException, DictionaryModifyException, LuceneException,
            IndexerRunningException {
        ConceptEntry entry = conceptManager.getConceptEntry(wrapperId);
        // Creating concept wrapper with all the values, because in future we
        // will be including manipulations on deleted wrappers as well.
        // WrapperId has been added to delete the wordnet id. If this wordnet
        // id is not deleted, then the merged wordnet id will be appearing on
        // concept search screen. If WID-1 and WID-2 are merged, then each time
        // we create a wrapper for WID-1 and WID-2, we need to have WID-1 and
        // WID-2 in wordnet id field to be deleted.
        entry.setConceptList(conceptsMergeBean.getSelectedListName());
        entry.setWordnetId(wrapperId);
        return conceptManager.addConceptListEntry(entry, userName);
    }

    private void fillConceptEntry(ConceptEntry entry, ConceptsMergeBean conceptMergeBean) {
        entry.setPos(conceptMergeBean.getSelectedPosValue());
        entry.setConceptList(conceptMergeBean.getSelectedListName());
        entry.setTypeId(conceptMergeBean.getSelectedTypeId());

        String prefix = ",";
        entry.setWord(conceptMergeBean.getWord());

        String description = conceptMergeBean.getDescriptions().stream().map(i -> i.toString())
                .collect(Collectors.joining(" "));
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

        StringBuilder mergedIdBuilder = new StringBuilder("");
        mergedIdBuilder.append(
                conceptMergeBean.getMergedIds().stream().map(i -> i.toString()).collect(Collectors.joining(prefix)));
        String conceptIdsToBeMerged = conceptMergeBean.getConceptIds().stream().collect(Collectors.joining(","));
        if (conceptIdsToBeMerged.length() > 0 && mergedIdBuilder.length() > 0) {
            mergedIdBuilder.append(",");
        }
        mergedIdBuilder.append(conceptIdsToBeMerged);
        entry.setMergedIds(mergedIdBuilder.toString());

        addAlternativeIdsToEntry(entry, conceptMergeBean);
    }

    private void addAlternativeIdsToEntry(ConceptEntry entry, ConceptsMergeBean conceptMergeBean) {
        List<String> conceptIds = conceptMergeBean.getConceptIds();
        Set<String> alternativeIds = new HashSet<>();

        for (String conceptId : conceptIds) {
            alternativeIds.add(conceptId);
            ConceptEntry conceptEntry = conceptManager.getConceptEntry(conceptId);
            if (conceptEntry.getAlternativeIds() != null) {
                alternativeIds.addAll(conceptEntry.getAlternativeIds());
            }
        }
        entry.setAlternativeIds(alternativeIds);
    }

}
