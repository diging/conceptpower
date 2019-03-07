package edu.asu.conceptpower.profile.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import edu.asu.conceptpower.app.jaxb.viaf.Channel;
import edu.asu.conceptpower.app.jaxb.viaf.Item;
import edu.asu.conceptpower.app.profile.ISearchResult;
import edu.asu.conceptpower.app.profile.ISearchResultFactory;
import edu.asu.conceptpower.app.profile.impl.SearchResult;
import edu.asu.conceptpower.app.profile.impl.ViafReply;
import edu.asu.conceptpower.app.profile.impl.ViafService;

public class ViafServiceTest {

    @Mock
    private RestTemplate template = Mockito.mock(RestTemplate.class);

    @Mock
    private ISearchResultFactory searchResultFactory = Mockito.mock(ISearchResultFactory.class);
    
    @InjectMocks
    private ViafService viafService;

    @Before
    public void setUp() {
        template = Mockito.mock(RestTemplate.class);

        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(viafService, "viafURL", "http://viaf.org/viaf/search");
        ReflectionTestUtils.setField(viafService, "searchViafURLPath", "?query=local.names%20all");
        ReflectionTestUtils.setField(viafService, "searchViafURLPath1", "+&amp;maximumRecords=100&amp;startRecord=");
        ReflectionTestUtils.setField(viafService, "searchViafURLPath2",
                "&amp;sortKeys=holdingscount&amp;httpAccept=application/rss+xml");

        ViafReply viafReply = new ViafReply();
        Channel channel = new Channel();
        Item item = new Item();
        item.setLink("http://viaf.org/viaf/27173507");
        item.setTitle("Pirckheimer, Willibald, 1470-1530.");
        item.setPubDate("Mon, 12 Jul 2015 18:51:56 GMT");
        List<Item> items = new ArrayList<Item>();
        items.add(item);
        channel.setItems(items);

        viafReply.setChannel(channel);

        Mockito.when(template.getForObject(
                "http://viaf.org/viaf/search?query=local.names all Pirckheimer+&amp;maximumRecords=100&amp;startRecord=1&amp;sortKeys=holdingscount&amp;httpAccept=application/rss+xml",
                ViafReply.class)).thenReturn(viafReply);
        Mockito.when(searchResultFactory.getSearchResultObject()).thenReturn(new SearchResult());

        ViafReply emptyViafReply = new ViafReply();
        Channel emptyChannel = new Channel();
        List<Item> emptyitems = null;
        emptyChannel.setItems(emptyitems);

        emptyViafReply.setChannel(emptyChannel);

        Mockito.when(template.getForObject(
                "http://viaf.org/viaf/search?query=local.names all Test for Null+&amp;maximumRecords=100&amp;startRecord=1&amp;sortKeys=holdingscount&amp;httpAccept=application/rss+xml",
                ViafReply.class)).thenReturn(emptyViafReply);

    }

    @Test
    public void testSearch() {

        List<ISearchResult> searchResults = viafService.search("Pirckheimer");

        assertEquals(10, searchResults.size());
        assertEquals("Mon, 18 Feb 2018 10:37:15 GMT", searchResults.get(0).getDescription());
        assertEquals("http://viaf.org/viaf/4671151898623524190004", searchResults.get(0).getId());
        assertEquals("Bry, Theodor de 1528-1598 | Pirckheimer TIB X Kommentar.1001.106 C14", searchResults.get(0).getName());
    }

    @Test
    public void searchNullTest() {

        List<ISearchResult> searchResults = viafService.search("Test for Null");
        // Checked for zero size since the list is created and no result has
        // been added
        assertEquals(0, searchResults.size());
    }

}
