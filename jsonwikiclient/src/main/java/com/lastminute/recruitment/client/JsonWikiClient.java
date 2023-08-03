package com.lastminute.recruitment.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lastminute.recruitment.domain.WikiPage;
import com.lastminute.recruitment.domain.WikiReader;
import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JsonWikiClient implements WikiReader {

    @Override
    public WikiPage read(String link) throws WikiPageNotFound {
        try {
            String pageContent = readJson(link);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(pageContent);
            String title = jsonNode.get("title").asText();
            String content = jsonNode.get("content").asText();
            List<String> links = new ArrayList<>();
            JsonNode linksNode = jsonNode.get("links");
            if (linksNode.isArray()) {
                for (JsonNode linkNode : linksNode) {
                    links.add(linkNode.asText());
                }
            }
            return new WikiPage(title, content, link, links);
        } catch (IOException ex) {
            throw new WikiPageNotFound("Failed to read JSON content for link: " + link, ex);
        }

    }

    private String readJson(String link) throws IOException {
        String name = link.replace("\"", "")
                .replace("http://wikiscrapper.test/", "/wikiscrapper/") + ".json";
        URL url = getClass().getResource(name);
        if (url == null) {
            throw new IOException("JSON content not found for link: " + link);
        }
        return IOUtils.toString(url, StandardCharsets.UTF_8);
    }
}
