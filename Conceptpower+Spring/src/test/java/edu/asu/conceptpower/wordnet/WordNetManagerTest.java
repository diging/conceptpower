package edu.asu.conceptpower.wordnet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;    

public class WordNetManagerTest {

    @Mock
    private WordNetConfiguration configuration;

    @Mock
    private IWordID wordId = Mockito.mock(IWordID.class);

    @Mock
    private Dictionary dictionary;

    @InjectMocks
    private WordNetManager wordNetManager;

    @Mock
    private IWord wordId2 = Mockito.mock(IWord.class);

    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);
        try {
            this.wordNetManager.init();
        } catch (Exception ex) {
        }

        // Mockito.when(WordID.parseWordID("WID-04206225-N-0311")).thenReturn(wordId2);
        Mockito.when(dictionary.getWord(wordId)).thenReturn(wordId2);
    }

    @Test
    public void getConcept() {
        ConceptEntry entry = wordNetManager.getConcept("WID-04206225-N-0311");

    }
}
