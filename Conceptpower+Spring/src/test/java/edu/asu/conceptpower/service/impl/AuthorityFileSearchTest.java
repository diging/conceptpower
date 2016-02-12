package edu.asu.conceptpower.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.conceptpower.servlet.profile.ISearchResult;
import edu.asu.conceptpower.servlet.profile.IService;
import edu.asu.conceptpower.servlet.profile.IServiceRegistry;
import edu.asu.conceptpower.servlet.profile.impl.SearchResult;
import edu.asu.conceptpower.servlet.service.impl.AuthorityFileSearch;

public class AuthorityFileSearchTest {

    @Mock
    private IServiceRegistry serviceRegistry = Mockito.mock(IServiceRegistry.class);

    @Mock
    private IService viafService = Mockito.mock(IService.class);

    @InjectMocks
    private AuthorityFileSearch authorityFileSearch;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        ISearchResult searchResult = new SearchResult();
        searchResult.setDescription("Mon, 12 Jul 2015 18:51:56 GMT");
        searchResult.setId("http://viaf.org/viaf/27173507");
        searchResult.setName("Pirckheimer, Willibald, 1470-1530.");
        List<ISearchResult> searchResults = new ArrayList<ISearchResult>();
        searchResults.add(searchResult);

        Mockito.when(serviceRegistry.getServiceObject("edu.asu.viaf")).thenReturn(viafService);
        Mockito.when(viafService.search("Pirckheimer")).thenReturn(searchResults);
    }

    @Test
    public void getSearchResultBackBeanListTest() {
        List<ISearchResult> result = authorityFileSearch.getSearchResultBackBeanList("edu.asu.viaf", "Pirckheimer");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mon, 12 Jul 2015 18:51:56 GMT", result.get(0).getDescription());
        assertEquals("http://viaf.org/viaf/27173507", result.get(0).getId());
        assertEquals("Pirckheimer, Willibald, 1470-1530.", result.get(0).getName());
    }

    @Test
    public void getNullSearchResultBeanListTest() {

        List<ISearchResult> result = authorityFileSearch.getSearchResultBackBeanList("edu.asu.viaf", "Test For Null");
        assertEquals(0, result.size());

    }

}
