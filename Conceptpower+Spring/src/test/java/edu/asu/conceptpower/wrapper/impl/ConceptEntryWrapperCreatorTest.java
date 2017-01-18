package edu.asu.conceptpower.wrapper.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.core.IConceptTypeManger;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.users.IUserManager;
import edu.asu.conceptpower.app.util.IURIHelper;
import edu.asu.conceptpower.app.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.app.wrapper.impl.ConceptEntryWrapperCreator;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.servlet.core.ChangeEvent;
import edu.asu.conceptpower.servlet.core.ChangeEvent.ChangeEventTypes;
import edu.asu.conceptpower.users.User;

public class ConceptEntryWrapperCreatorTest {

    @Mock
    private IConceptManager conceptManager = Mockito.mock(IConceptManager.class);

    @Mock
    private IConceptTypeManger typesManager = Mockito.mock(IConceptTypeManger.class);

    @Mock
    private IUserManager usersManager = Mockito.mock(IUserManager.class);

    @Mock
    private IURIHelper helper = Mockito.mock(IURIHelper.class);

    @InjectMocks
    private ConceptEntryWrapperCreator conceptEntryWrapperCreator;

    ConceptEntry[] entries = new ConceptEntry[1];
    ConceptEntry entry = new ConceptEntry();

    @Before
    public void init() throws LuceneException, IllegalAccessException, IndexerRunningException {

        ChangeEvent changeEvent = new ChangeEvent();
        changeEvent.setUserName("Test");
        changeEvent.setType(ChangeEventTypes.CREATION);
        changeEvent.setDate(new Date());

        entry.setTypeId("Type-1");
        entry.addNewChangeEvent(changeEvent);

        entry.setCreatorId("Test");
        entry.setWordnetId("WNET_1");
        entry.setSynonymIds("SYN_1");
        entries[0] = entry;

        MockitoAnnotations.initMocks(this);

        ConceptType type = new ConceptType();
        type.setTypeId("Type-1");
        type.setTypeName("Type-Name");
        Mockito.when(typesManager.getType("Type-1")).thenReturn(type);

        User user = new User();
        user.setUsername("Test");
        Mockito.when(usersManager.findUser("Test")).thenReturn(user);

        ConceptEntry entry2 = new ConceptEntry();
        entry2.setWordnetId("WNET_1");
        entry2.setDescription("Concept Description");
        entry2.setWord("Concept Word");
        Mockito.when(conceptManager.getWordnetConceptEntry("WNET_1")).thenReturn(entry2);

        ConceptEntry synonymEntry = new ConceptEntry();
        entry2.setSynonymIds("SYN_1");
        Mockito.when(conceptManager.getConceptEntry("SYN_1")).thenReturn(synonymEntry);

        String uri = "http://www.digitalhps.org/concepts/";
        Mockito.when(helper.getURI(entry)).thenReturn(uri);

    }

    @Test
    public void createWrappers() throws LuceneException, IllegalAccessException, IndexerRunningException {
        List<ConceptEntryWrapper> conceptEntryWrapperList = conceptEntryWrapperCreator.createWrappers(entries);
        assertNotNull(conceptEntryWrapperList);
        assertEquals("Type-1", conceptEntryWrapperList.get(0).getType().getTypeId());
        assertEquals("Type-Name", conceptEntryWrapperList.get(0).getType().getTypeName());
        assertEquals("Test", conceptEntryWrapperList.get(0).getCreator().getUsername());
        assertNotNull(conceptEntryWrapperList.get(0).getWrappedWordnetEntries());
        assertNotNull(conceptEntryWrapperList.get(0).getSynonyms());
        assertEquals(conceptEntryWrapperList.get(0).getUri(), "http://www.digitalhps.org/concepts/");

    }
    
    @Test
    public void createNullWrappers() throws LuceneException, IllegalAccessException, IndexerRunningException {
    	List<ConceptEntryWrapper> conceptEntryWrapperList = conceptEntryWrapperCreator.createWrappers(null);
    	assertNotNull(conceptEntryWrapperList);
    	assertEquals(conceptEntryWrapperList.size(),0);
    }
}
