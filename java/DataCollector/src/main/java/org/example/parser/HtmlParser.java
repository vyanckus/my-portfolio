package org.example.parser;

import lombok.Getter;
import org.example.model.MetroLineFromHtml;
import org.example.model.MetroStationFromHtml;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HtmlParser {
    @Getter
    private static final String url = "https://skillbox-java.github.io/";


    public static Document getHtmlPage() {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.err.println("Ошибка при подключении к " + url + ": " + e.getMessage());
            return null;
        }
        return document;
    }

    public static List<MetroLineFromHtml> parseMetroLines() {
        List<MetroLineFromHtml> metroLines = new ArrayList<>();
        Document htmlPage = getHtmlPage();
        if (htmlPage == null) {
            System.err.println("Не удалось получить HTML-страницу, парсинг невозможен.");
            return metroLines;
        }

        Elements lines = htmlPage.select(".js-metro-line");
        for (Element line : lines) {
            String numberLine = line.attr("data-line");
            String nameLine = line.text();
            metroLines.add(new MetroLineFromHtml(nameLine, numberLine));
        }
        return metroLines;
    }

    public static HashMap<String, MetroStationFromHtml> parseMetroStation() {
        HashMap<String, MetroStationFromHtml> metroStations = new HashMap<>();
        Document htmlPage = getHtmlPage();
        if (htmlPage == null) {
            System.err.println("Не удалось получить HTML-страницу, парсинг невозможен.");
            return metroStations;
        }

        Elements stations = htmlPage.select(".js-metro-stations");
        for (Element station : stations) {
            String numberLine = station.attr("data-line");
            Elements nameStations = station.select(".name");
            for (Element nameStation : nameStations) {
                String name = nameStation.text();
                List<String> numbersLines = new ArrayList<>();
                if (metroStations.containsKey(name)) {
                    numbersLines = metroStations.get(name).getNumbersLine();
                }
                numbersLines.add(numberLine);
                metroStations.put(name, new MetroStationFromHtml(name,numbersLines));
            }
        }
        return metroStations;
    }
}
