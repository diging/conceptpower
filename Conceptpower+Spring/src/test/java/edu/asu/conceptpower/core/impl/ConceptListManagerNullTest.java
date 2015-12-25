package edu.asu.conceptpower.core.impl;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.db4o.IConceptDBManager;

public class ConceptListManagerNullTest {

    @Mock
    private IConceptDBManager client = Mockito.mock(IConceptDBManager.class);

    @InjectMocks
    private ConceptListManager conceptListManager;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(client.getAllElementsOfType(ConceptList.class)).thenReturn(null);
    }
}
