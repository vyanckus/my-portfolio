package org.example;

import java.net.MalformedURLException;
import java.net.URL;

public class LinkFilter {
    private final String normalizedDomain;

    public LinkFilter(String url) throws MalformedURLException {
        URL parsedUrl = new URL(url);
        String host = parsedUrl.getHost().replace("www.", "");

        String protocol = parsedUrl.getProtocol().equals("http") ? "https" : parsedUrl.getProtocol();
        this.normalizedDomain = protocol + "://" + host;
    }

    public String normalizeLink(String url) {
        return url.replaceFirst("^http://", "https://")
                .replaceFirst("www.", "");
    }

    public boolean isValidLink(String link) {
        try {
            URL url = new URL(link);
            String host = url.getHost().replace("www.", "");


            return host.equals(normalizedDomain.replaceFirst("https?://", ""))
                    && !link.contains("#");
        } catch (MalformedURLException e) {
            return false;
        }

    }
}
