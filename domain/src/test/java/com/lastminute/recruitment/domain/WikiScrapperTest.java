package com.lastminute.recruitment.domain;

import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class WikiScrapperTest {

    @Mock
    private WikiReader wikiReader;

    @Mock
    private WikiPageRepository repository;

    private WikiScrapper wikiScrapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        wikiScrapper = new WikiScrapper(wikiReader, repository);
    }

    @Test
    void read() {
        String link = "http://wikiscrapper.test/site1";
        List<String> links = Arrays.asList("http://wikiscrapper.test/site2", "http://wikiscrapper.test/site3");
        WikiPage wikiPage = new WikiPage("Page1", "Content for Page1", link, links);
        when(wikiReader.read(link)).thenReturn(wikiPage);

        wikiScrapper.read(link);

        verify(wikiReader, times(1)).read(link);
        verify(repository, times(1)).save(any(WikiPage.class));
    }

    @Test
    void readWhenLoopedPages() {
        String link1 = "http://wikiscrapper.test/site1";
        String link2 = "http://wikiscrapper.test/site2";
        String link3 = "http://wikiscrapper.test/site3";
        List<String> links1 = Arrays.asList(link2, link3);
        List<String> links2 = Arrays.asList(link1);
        WikiPage wikiPage1 = new WikiPage("Page1", "Content for Page1", link1, links1);
        WikiPage wikiPage2 = new WikiPage("Page2", "Content for Page2", link2, links2);
        when(wikiReader.read(link1)).thenReturn(wikiPage1);
        when(wikiReader.read(link2)).thenReturn(wikiPage2);

        wikiScrapper.read(link1);

        verify(wikiReader, times(4)).read(anyString());
        verify(repository, times(2)).save(any(WikiPage.class));
    }

    @Test
    void testReadWhenPageNotFound() {
        String link = "http://wikiscrapper.test/non_existent_page";
        when(wikiReader.read(link)).thenThrow(new WikiPageNotFound("Page not found"));

        wikiScrapper.read(link);

        verify(wikiReader, times(1)).read(link);
        verify(repository, never()).save(any(WikiPage.class));
    }
}