package edu.asu.conceptpower.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.conceptpower.app.manager.ConceptListManager;
import edu.asu.conceptpower.app.manager.ConceptTypesManager;
import edu.asu.conceptpower.app.model.ConceptList;
import edu.asu.conceptpower.app.model.ConceptType;

public class ConceptWrapperServiceTest {

    @Mock
    private ConceptListManager conceptListManager;

    @Mock
    private ConceptTypesManager conceptTypesManager;

    @InjectMocks
    private ConceptWrapperService conceptWrapperService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_fetchAllConceptLists_successForConceptLists() {

        ConceptList firstConceptList = new ConceptList();
        firstConceptList.setConceptListName("First Concept List");

        ConceptList secondConceptList = new ConceptList();
        secondConceptList.setConceptListName("Second Concept List");

        List<ConceptList> conceptLists = new ArrayList<>();
        conceptLists.add(firstConceptList);
        conceptLists.add(secondConceptList);
        Mockito.when(conceptListManager.getAllConceptLists()).thenReturn(conceptLists);

        Map<String, String> conceptListMap = conceptWrapperService.fetchAllConceptLists();
        Mockito.verify(conceptListManager).getAllConceptLists();
        Assert.assertEquals(2, conceptListMap.size());
        Assert.assertEquals(firstConceptList.getConceptListName(),
                conceptListMap.get(firstConceptList.getConceptListName()));
        Assert.assertEquals(secondConceptList.getConceptListName(),
                conceptListMap.get(secondConceptList.getConceptListName()));

    }

    @Test
    public void test_fetchAllConceptLists_successForConceptListWithASingleEntry() {

        ConceptList firstConceptList = new ConceptList();
        firstConceptList.setConceptListName("First Concept List");

        List<ConceptList> conceptLists = new ArrayList<>();
        conceptLists.add(firstConceptList);

        Mockito.when(conceptListManager.getAllConceptLists()).thenReturn(conceptLists);

        Map<String, String> conceptListMap = conceptWrapperService.fetchAllConceptLists();
        Mockito.verify(conceptListManager).getAllConceptLists();
        Assert.assertEquals(1, conceptListMap.size());
        Assert.assertEquals(firstConceptList.getConceptListName(),
                conceptListMap.get(firstConceptList.getConceptListName()));

    }

    @Test
    public void test_fetchAllConceptLists_successForEmptyConceptList() {
        Mockito.when(conceptListManager.getAllConceptLists()).thenReturn(null);
        Map<String, String> conceptListMap = conceptWrapperService.fetchAllConceptLists();
        Mockito.verify(conceptListManager).getAllConceptLists();
        Assert.assertEquals(0, conceptListMap.size());

    }

    @Test
    public void test_fetchAllConceptTypes_successForConceptTypes() {

        ConceptType firstConceptType = new ConceptType();
        firstConceptType.setTypeName("Type-1");
        firstConceptType.setTypeId("TYPE-1234");

        ConceptType secondConceptType = new ConceptType();
        secondConceptType.setTypeName("TYPE-2");
        secondConceptType.setTypeId("TYPE-4567");

        ConceptType[] conceptTypes = new ConceptType[2];
        conceptTypes[0] = firstConceptType;
        conceptTypes[1] = secondConceptType;

        Mockito.when(conceptTypesManager.getAllTypes()).thenReturn(conceptTypes);
        Map<String, String> conceptTypesMap = conceptWrapperService.fetchAllConceptTypes();
        Mockito.verify(conceptTypesManager).getAllTypes();
        Assert.assertEquals(2, conceptTypesMap.size());
        Assert.assertEquals(firstConceptType.getTypeName(), conceptTypesMap.get(firstConceptType.getTypeId()));
        Assert.assertEquals(secondConceptType.getTypeName(), conceptTypesMap.get(secondConceptType.getTypeId()));
    }

    @Test
    public void test_fetchAllConceptTypes_successForSingleConceptType() {

        ConceptType firstConceptType = new ConceptType();
        firstConceptType.setTypeName("Type-1");
        firstConceptType.setTypeId("TYPE-1234");

        ConceptType[] conceptTypes = new ConceptType[1];
        conceptTypes[0] = firstConceptType;

        Mockito.when(conceptTypesManager.getAllTypes()).thenReturn(conceptTypes);
        Map<String, String> conceptTypesMap = conceptWrapperService.fetchAllConceptTypes();
        Mockito.verify(conceptTypesManager).getAllTypes();
        Assert.assertEquals(1, conceptTypesMap.size());
        Assert.assertEquals(firstConceptType.getTypeName(), conceptTypesMap.get(firstConceptType.getTypeId()));
    }

    @Test
    public void test_fetchAllConceptTypes_successForEmptyConceptTypes() {

        Mockito.when(conceptTypesManager.getAllTypes()).thenReturn(null);
        Map<String, String> conceptTypesMap = conceptWrapperService.fetchAllConceptTypes();
        Mockito.verify(conceptTypesManager).getAllTypes();
        Assert.assertEquals(0, conceptTypesMap.size());
    }
}
