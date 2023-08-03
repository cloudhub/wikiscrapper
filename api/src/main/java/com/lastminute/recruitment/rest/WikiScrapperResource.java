package com.lastminute.recruitment.rest;

import com.lastminute.recruitment.domain.WikiScrapper;
import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/wiki")
@RestController
public class WikiScrapperResource {

    private final WikiScrapper wikiScrapper;

    public WikiScrapperResource(WikiScrapper wikiScrapper) {
        this.wikiScrapper = wikiScrapper;
    }

    @PostMapping("/scrap")
    public ResponseEntity scrapWikipedia(@RequestBody String link) {
        try {
            System.out.println("Hello Scrap -> " + link);
            wikiScrapper.read(link);
            return ResponseEntity.ok("The page was scrapped successfully.");
        } catch (WikiPageNotFound ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The link cannot be found");
        }
    }
}
