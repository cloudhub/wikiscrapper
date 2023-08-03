package com.lastminute.recruitment.client;

import com.lastminute.recruitment.domain.WikiPage;
import com.lastminute.recruitment.domain.WikiReader;
import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HtmlWikiClient implements WikiReader {

    @Override
    public WikiPage read(String link) throws WikiPageNotFound {
        try {
            String pageContent = readHtml(link);
            Document document = Jsoup.parse(pageContent);
            String title = document.select("title").text();
            String content = document.select("#content").text();
            List<String> links = new ArrayList<>();
            Elements elements = document.select("a[href]");
            for (Element element : elements) {
                links.add(element.attr("href"));
            }
            return new WikiPage(title, content, link, links);
        } catch (IOException ex) {
            throw new WikiPageNotFound("Failed to read HTML content for link: " + link, ex);
        }
    }

    public String readHtml(String link) throws IOException {
        String name = link.replace("\"", "")
                .replace("http://wikiscrapper.test/", "/wikiscrapper/") + ".html";
        URL url = getClass().getResource(name);
        if (url == null) {
            throw new IOException("HTML content not found for link: " + link);
        }
        return IOUtils.toString(url, StandardCharsets.UTF_8);
    }
}
