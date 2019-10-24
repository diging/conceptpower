package edu.asu.conceptpower.app.core.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.conceptpower.app.core.IConceptTypesService;
import edu.asu.conceptpower.app.core.IConceptTypesService.IdType;
import edu.asu.conceptpower.core.ConceptEntry;

public class AlternativeIdServiceTest {

    @InjectMocks
    private AlternativeIdService alternativeIdService;

    @Mock
    private IConceptTypesService conceptTypesService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_addAlternativeIds_successQueriedParameterIsNull() {
        String queriedId = null;
        ConceptEntry entry = null;
        Mockito.when(conceptTypesService.getConceptTypeByConceptId(queriedId)).thenReturn(null);
        alternativeIdService.addAlternativeIds(queriedId, entry);
        // There should not be any change to the concept entry when entry is
        // null.
        Assert.assertEquals(null, entry);
    }

    @Test
    public void test_addAlternativeIds_successQueriedByGenericWordnetIdWithoutMergedId() {
        String queriedId = "WID-05562015-N-??-shank's_pony";
        ConceptEntry entry = new ConceptEntry();
        entry.setId("CONc3405898-074b-4b97-9a43-b4f869e59e81");
        entry.setWordnetId("WID-05562015-N-03-shank's_pony, WID-05562015-N-05-shank's_pony");

        Mockito.when(conceptTypesService.getConceptTypeByConceptId(queriedId))
                .thenReturn(IdType.GENERIC_WORDNET_CONCEPT_ID);

        alternativeIdService.addAlternativeIds(queriedId, entry);

        Set<String> alternativeIdsSet = new HashSet<>();
        alternativeIdsSet.add("WID-05562015-N-05-shank's_pony");
        alternativeIdsSet.add("WID-05562015-N-03-shank's_pony");
        alternativeIdsSet.add("CONc3405898-074b-4b97-9a43-b4f869e59e81");
        alternativeIdsSet.add(queriedId);

        Assert.assertEquals(alternativeIdsSet, entry.getAlternativeIds());

    }

    @Test
    public void test_addAlternativeIds_successQueriedByGenericWordnetIdWithMergedIds() {
        String queriedId = "WID-05562015-N-??-shank's_pony";
        ConceptEntry entry = new ConceptEntry();
        entry.setId("CONc3405898-074b-4b97-9a43-b4f869e59e81");
        entry.setMergedIds("WID-05562015-N-03-shank's_pony, WID-05562015-N-05-shank's_pony");

        Mockito.when(conceptTypesService.getConceptTypeByConceptId(queriedId))
                .thenReturn(IdType.GENERIC_WORDNET_CONCEPT_ID);

        alternativeIdService.addAlternativeIds(queriedId, entry);

        Set<String> alternativeIdsSet = new HashSet<>();
        alternativeIdsSet.add("WID-05562015-N-05-shank's_pony");
        alternativeIdsSet.add("WID-05562015-N-03-shank's_pony");
        alternativeIdsSet.add("CONc3405898-074b-4b97-9a43-b4f869e59e81");
        alternativeIdsSet.add(queriedId);

        Assert.assertEquals(alternativeIdsSet, entry.getAlternativeIds());

    }

    @Test
    public void test_addAlternativeIds_successQueriedByGenericWordnetIdWithMergedIdsAndWordnetIds() {
        String queriedId = "WID-05562015-N-??-shank's_pony";
        ConceptEntry entry = new ConceptEntry();
        entry.setId("CONc3405898-074b-4b97-9a43-b4f869e59e81");
        entry.setWordnetId("WID-05562015-N-03-shank's_pony");
        entry.setMergedIds("WID-05562015-N-05-shank's_pony");

        Mockito.when(conceptTypesService.getConceptTypeByConceptId(queriedId))
                .thenReturn(IdType.GENERIC_WORDNET_CONCEPT_ID);

        alternativeIdService.addAlternativeIds(queriedId, entry);

        Set<String> alternativeIdsSet = new HashSet<>();
        alternativeIdsSet.add("WID-05562015-N-05-shank's_pony");
        alternativeIdsSet.add("WID-05562015-N-03-shank's_pony");
        alternativeIdsSet.add("CONc3405898-074b-4b97-9a43-b4f869e59e81");
        alternativeIdsSet.add(queriedId);

        Assert.assertEquals(alternativeIdsSet, entry.getAlternativeIds());

    }

    @Test
    public void test_addAlternativeIds_successQueriedBySpecificWordnetIdWithMergedIdsAndWordnetIds() {
        String queriedId = "WID-05562015-N-03-shank's_pony";
        ConceptEntry entry = new ConceptEntry();
        entry.setId("CONc3405898-074b-4b97-9a43-b4f869e59e81");
        entry.setWordnetId("WID-05562015-N-03-shank's_pony");
        entry.setMergedIds("WID-05562015-N-05-shank's_pony");

        Mockito.when(conceptTypesService.getConceptTypeByConceptId(queriedId))
                .thenReturn(IdType.GENERIC_WORDNET_CONCEPT_ID);

        alternativeIdService.addAlternativeIds(queriedId, entry);

        Set<String> alternativeIdsSet = new HashSet<>();
        alternativeIdsSet.add("WID-05562015-N-05-shank's_pony");
        alternativeIdsSet.add("WID-05562015-N-03-shank's_pony");
        alternativeIdsSet.add("CONc3405898-074b-4b97-9a43-b4f869e59e81");

        Assert.assertEquals(alternativeIdsSet, entry.getAlternativeIds());

    }

    @Test
    public void test_addAlternativeIds_successQueriedBySpecificWordnetIdWithWordnetIds() {
        String queriedId = "WID-05562015-N-05-shank's_pony";
        ConceptEntry entry = new ConceptEntry();
        entry.setId("CONc3405898-074b-4b97-9a43-b4f869e59e81");
        entry.setWordnetId("WID-05562015-N-05-shank's_pony");

        Mockito.when(conceptTypesService.getConceptTypeByConceptId(queriedId))
                .thenReturn(IdType.GENERIC_WORDNET_CONCEPT_ID);

        alternativeIdService.addAlternativeIds(queriedId, entry);

        Set<String> alternativeIdsSet = new HashSet<>();
        alternativeIdsSet.add("WID-05562015-N-05-shank's_pony");
        alternativeIdsSet.add("CONc3405898-074b-4b97-9a43-b4f869e59e81");

        Assert.assertEquals(alternativeIdsSet, entry.getAlternativeIds());

    }

    @Test
    public void test_addAlternativeIds_successQueriedByLocalConceptIdWithWordnetAndMergedIds() {
        String queriedId = "CONc3405898-074b-4b97-9a43-b4f869e59e81";
        ConceptEntry entry = new ConceptEntry();
        entry.setId("CONc3405898-074b-4b97-9a43-b4f869e59e81");
        entry.setWordnetId("WID-05562015-N-05-shank's_pony");
        entry.setMergedIds("WID-05562015-N-05-shank's_pony,WID-05562015-N-03-shank's_pony");

        Mockito.when(conceptTypesService.getConceptTypeByConceptId(queriedId))
                .thenReturn(IdType.GENERIC_WORDNET_CONCEPT_ID);

        alternativeIdService.addAlternativeIds(queriedId, entry);

        Set<String> alternativeIdsSet = new HashSet<>();
        alternativeIdsSet.add("WID-05562015-N-05-shank's_pony");
        alternativeIdsSet.add("WID-05562015-N-03-shank's_pony");
        alternativeIdsSet.add("CONc3405898-074b-4b97-9a43-b4f869e59e81");

        Assert.assertEquals(alternativeIdsSet, entry.getAlternativeIds());

    }

    @Test
    public void test_addAlternativeIds_successWithNullAlternativeIds() {

        String queriedId = "CONc3405898-074b-4b97-9a43-b4f869e59e81";
        ConceptEntry entry = new ConceptEntry();
        entry.setId("CONc3405898-074b-4b97-9a43-b4f869e59e81");
        entry.setWordnetId("WID-05562015-N-05-shank's_pony");
        entry.setMergedIds("WID-05562015-N-05-shank's_pony,WID-05562015-N-03-shank's_pony");
        entry.setAlternativeIds(null);

        Mockito.when(conceptTypesService.getConceptTypeByConceptId(queriedId))
                .thenReturn(IdType.GENERIC_WORDNET_CONCEPT_ID);

        alternativeIdService.addAlternativeIds(queriedId, entry);

        Set<String> alternativeIdsSet = new HashSet<>();
        alternativeIdsSet.add("WID-05562015-N-05-shank's_pony");
        alternativeIdsSet.add("WID-05562015-N-03-shank's_pony");
        alternativeIdsSet.add("CONc3405898-074b-4b97-9a43-b4f869e59e81");

        Assert.assertEquals(alternativeIdsSet, entry.getAlternativeIds());
    }

    @Test
    public void test_addAlternativeIds_successWithCollectionConceptEntry() {

        ConceptEntry entry1 = new ConceptEntry();
        entry1.setId("CONc3405898-074b-4b97-9a43-b4f869e59e81");
        entry1.setWordnetId("WID-05562015-N-05-shank's_pony");
        entry1.setMergedIds("WID-05562015-N-05-shank's_pony,WID-05562015-N-03-shank's_pony");

        ConceptEntry entry2 = new ConceptEntry();
        entry2.setId("CONc3405898-074b-4b97-9a43-b4f869e59e82");
        entry2.setWordnetId("WID-05562015-N-06-shank's_pony");
        entry2.setMergedIds("WID-05562015-N-07-shank's_pony,WID-05562015-N-01-shank's_pony");

        List<ConceptEntry> conceptEntries = new ArrayList<>();
        conceptEntries.add(entry1);
        conceptEntries.add(entry2);

        Mockito.when(conceptTypesService.getConceptTypeByConceptId("WID-05562015-N-05-shank's_pony"))
                .thenReturn(IdType.SPECIFIC_WORDNET_CONCEPT_ID);

        Mockito.when(conceptTypesService.getConceptTypeByConceptId("CONc3405898-074b-4b97-9a43-b4f869e59e82"))
                .thenReturn(IdType.LOCAL_CONCEPT_ID);

        alternativeIdService.addAlternativeIds(conceptEntries);

        Set<String> alternativeIds1 = new HashSet<>();
        alternativeIds1.add("WID-05562015-N-05-shank's_pony");
        alternativeIds1.add("WID-05562015-N-03-shank's_pony");
        alternativeIds1.add("CONc3405898-074b-4b97-9a43-b4f869e59e81");

        Set<String> alternativeIds2 = new HashSet<>();
        alternativeIds2.add("CONc3405898-074b-4b97-9a43-b4f869e59e82");
        alternativeIds2.add("WID-05562015-N-07-shank's_pony");
        alternativeIds2.add("WID-05562015-N-06-shank's_pony");
        alternativeIds2.add("WID-05562015-N-01-shank's_pony");

        Assert.assertEquals(alternativeIds1, conceptEntries.get(0).getAlternativeIds());
        Assert.assertEquals(alternativeIds2, conceptEntries.get(1).getAlternativeIds());

    }

    @Test
    public void test_addAlternativeIds_successWithConceptEntryArray() {

        ConceptEntry entry1 = new ConceptEntry();
        entry1.setId("CONc3405898-074b-4b97-9a43-b4f869e59e81");
        entry1.setWordnetId("WID-05562015-N-05-shank's_pony");
        entry1.setMergedIds("WID-05562015-N-05-shank's_pony,WID-05562015-N-03-shank's_pony");

        ConceptEntry entry2 = new ConceptEntry();
        entry2.setId("CONc3405898-074b-4b97-9a43-b4f869e59e82");
        entry2.setWordnetId("WID-05562015-N-06-shank's_pony");
        entry2.setMergedIds("WID-05562015-N-07-shank's_pony,WID-05562015-N-01-shank's_pony");

        ConceptEntry[] conceptEntries = new ConceptEntry[2];
        conceptEntries[0] = entry1;
        conceptEntries[1] = entry2;

        Mockito.when(conceptTypesService.getConceptTypeByConceptId("WID-05562015-N-05-shank's_pony"))
                .thenReturn(IdType.SPECIFIC_WORDNET_CONCEPT_ID);

        Mockito.when(conceptTypesService.getConceptTypeByConceptId("CONc3405898-074b-4b97-9a43-b4f869e59e82"))
                .thenReturn(IdType.LOCAL_CONCEPT_ID);

        alternativeIdService.addAlternativeIds(conceptEntries);

        Set<String> alternativeIds1 = new HashSet<>();
        alternativeIds1.add("WID-05562015-N-05-shank's_pony");
        alternativeIds1.add("WID-05562015-N-03-shank's_pony");
        alternativeIds1.add("CONc3405898-074b-4b97-9a43-b4f869e59e81");

        Set<String> alternativeIds2 = new HashSet<>();
        alternativeIds2.add("CONc3405898-074b-4b97-9a43-b4f869e59e82");
        alternativeIds2.add("WID-05562015-N-07-shank's_pony");
        alternativeIds2.add("WID-05562015-N-06-shank's_pony");
        alternativeIds2.add("WID-05562015-N-01-shank's_pony");

        Assert.assertEquals(alternativeIds1, conceptEntries[0].getAlternativeIds());
        Assert.assertEquals(alternativeIds2, conceptEntries[1].getAlternativeIds());

    }

}
