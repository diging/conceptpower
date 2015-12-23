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

import edu.asu.conceptpower.jaxb.viaf.Channel;
import edu.asu.conceptpower.jaxb.viaf.Item;
import edu.asu.conceptpower.profile.ISearchResult;
import edu.asu.conceptpower.profile.ISearchResultFactory;

public class ViafServiceTest {

    @Mock
    private RestTemplate template;

    @Mock
    private ISearchResultFactory searchResultFactory;

    @InjectMocks
    private ViafService viafService;

    @Before
    public void setUp() {
        template = Mockito.mock(RestTemplate.class);

        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(viafService, "viafURL", "http://viaf.org/viaf/search");
        ReflectionTestUtils.setField(viafService, "searchViafURLPath", "?query=local.names all");
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

        Mockito.when(template.getForObject(Mockito.anyString(), Mockito.eq(ViafReply.class))).thenReturn(viafReply);
        Mockito.when(searchResultFactory.getSearchResultObject()).thenReturn(new SearchResult());

    }

    @Test
    public void testSearch() {

        List<ISearchResult> searchResults = viafService.search("Pirckheimer");
        ISearchResult searchResult = new SearchResult();
        searchResult.setDescription("Mon, 12 Jul 2015 18:51:56 GMT");
        searchResult.setId("http://viaf.org/viaf/27173507");
        searchResult.setName("Pirckheimer, Willibald, 1470-1530.");

        assertEquals(1, searchResults.size());
        assertEquals(searchResult.getDescription(), searchResults.get(0).getDescription());
        assertEquals(searchResult.getId(), searchResults.get(0).getId());
        assertEquals(searchResult.getName(), searchResults.get(0).getName());
    }

}
