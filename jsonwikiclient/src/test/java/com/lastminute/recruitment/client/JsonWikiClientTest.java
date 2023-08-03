package com.lastminute.recruitment.client;

import com.lastminute.recruitment.domain.WikiPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ActiveProfiles("json")
class JsonWikiClientTest {

    @Mock
    private JsonWikiClient jsonWikiClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void read() {
        String link = "http://wikiscrapper.test/json_page";
        List<String> links = Arrays.asList("http://wikiscrapper.test/site2", "http://wikiscrapper.test/site3");
        WikiPage wikiPage = new WikiPage("Page1", "Content for Page1", link, links);

        when(jsonWikiClient.read(link)).thenReturn(wikiPage);

        WikiPage wikiPageActual = jsonWikiClient.read(link);

        assertEquals(wikiPage.getTitle(), wikiPageActual.getTitle());
        assertEquals(wikiPage.getContent(), wikiPageActual.getContent());
        assertEquals(wikiPage.getSelfLink(), wikiPageActual.getSelfLink());
    }
}