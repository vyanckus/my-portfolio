package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class WebPageParser {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome Safari/537.36";
    private static final int MIN_DELAY = 100;
    private static final int MAX_DELAY = 150;
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();


    private void delay() {
        try {
            Thread.sleep(random.nextInt(MAX_DELAY - MIN_DELAY) + MIN_DELAY);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public List<Node> getNodesFromPage(Node node, LinkFilter filter) throws WebPageParserException {
        delay();
        try {
            String url = node.getUrl();
            Document document = Jsoup.connect(url).userAgent(USER_AGENT).timeout(5000).get();
            Elements links = document.select("a[href]");

            List<Node> children = new ArrayList<>();

            links.stream()
                    .map(link -> link.absUrl("href"))
                    .filter(absoluteUrl -> !absoluteUrl.isEmpty())
                    .map(filter::normalizeLink)
                    .distinct()
                    .filter(absoluteUrl -> !absoluteUrl.equals(url))
                    .filter(filter::isValidLink)
                    .forEach(absoluteUrl -> children.add(new Node(absoluteUrl)));

            return children;
        } catch (SocketTimeoutException e) {
            throw new WebPageParserException("Timeout while connecting to " + node.getUrl(), e);
        } catch (IOException e) {
            throw new WebPageParserException("Error connecting to " + node.getUrl() + ": " + e.getMessage(), e);
        }

    }
}
