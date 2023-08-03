package com.lastminute.recruitment.domain;

import com.lastminute.recruitment.domain.error.WikiPageNotFound;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WikiScrapper {

    private final WikiReader wikiReader;
    private final WikiPageRepository repository;
    private final Set<String> visitedLinks;

    public WikiScrapper(WikiReader wikiReader, WikiPageRepository repository) {
        this.wikiReader = wikiReader;
        this.repository = repository;
        visitedLinks = new HashSet<>();
    }


    public void read(String link) {
        try {
            WikiPage page = wikiReader.read(link);
            if (page != null && !visitedLinks.contains(link)) {
                visitedLinks.add(link);
                repository.save(page);
                List<String> links = page.getLinks();
                if (links != null) {
                    for (String pageLink : links) {
                        read(pageLink);
                    }
                }
            }
        } catch (WikiPageNotFound ex) {
            System.out.println("Page was not found: " + ex);
        }
    }

}
