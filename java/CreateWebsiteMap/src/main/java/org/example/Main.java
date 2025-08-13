package org.example;

import java.io.BufferedWriter;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {

//        String startUrl = "https://skillbox.ru/";
        String startUrl = "https://albagym.ru/";
        String outputFilePath = "data/website_map.txt";
        Set<String> uniqueLinks = Collections.synchronizedSet(new HashSet<>());
        LinkFilter filter;

        try {
            filter = new LinkFilter(startUrl);
        } catch (MalformedURLException e) {
            System.err.println("Некорректный стартовый URL: " + e.getMessage());
            return;
        }

        String normalizedStartUrl = filter.normalizeLink(startUrl);
        Node root = new Node(normalizedStartUrl);

        if (root.getUrl().equals(normalizedStartUrl)) {
            root.setNumberOfTabs(0);
        }

        uniqueLinks.add(normalizedStartUrl);

        try (ForkJoinPool pool = new ForkJoinPool()) {
            WebsiteMapGeneratorTask task = new WebsiteMapGeneratorTask(root, uniqueLinks, filter);

            Node siteMapRoot = pool.invoke(task);

            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath))) {
                new FileWriterHelper().writeTree(siteMapRoot, writer);
                System.out.println("Карта сайта успешно сформирована в файле: " + outputFilePath);
            }
        } catch (Exception e) {
            System.err.println("Критическая ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}