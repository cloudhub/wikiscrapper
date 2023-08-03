package com.lastminute.recruitment.rest;

import com.lastminute.recruitment.domain.WikiScrapper;
import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class WikiScrapperResourceTest {

    @Mock
    private WikiScrapper wikiScrapper;

    @InjectMocks
    private WikiScrapperResource wikiScrapperResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testScrapeWikiWhenExistingPage() {
        String rootLink = "http://wikiscrapper.test/site1";

        Mockito.doNothing().when(wikiScrapper).read(rootLink);
        wikiScrapper.read(rootLink);

        ResponseEntity response = wikiScrapperResource.scrapWikipedia(rootLink);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The page was scrapped successfully.", response.getBody());
    }

    @Test
    void testScrapeWikiWhenNonExistingPage() {
        String rootLink = "http://wikiscrapper.test/non_existent_page";

        Mockito.doThrow(WikiPageNotFound.class).when(wikiScrapper).read(rootLink);

        ResponseEntity response = wikiScrapperResource.scrapWikipedia(rootLink);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("The link cannot be found", response.getBody());
    }
}