package org.example;

import org.example.filefinder.FileFinder;
import org.example.model.*;
import org.example.parser.CsvParser;
import org.example.parser.HtmlParser;
import org.example.parser.JsonParser;
import org.example.parser.JsonWriter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        List<MetroLineFromHtml> metroLines = HtmlParser.parseMetroLines();
        System.out.println("Список линий Московского метро:");
        for (MetroLineFromHtml line : metroLines) {
            System.out.println(line);
        }

        System.out.println();

        HashMap<String, MetroStationFromHtml> metroStations = HtmlParser.parseMetroStation();
        System.out.println("Список станций Московского метро:");
        metroStations.forEach((k, v) -> System.out.println(v));

        System.out.println();

        String fileData = "data/data";
        FileFinder fileFinder = new FileFinder();
        List<File> jsonFileList = fileFinder.findFiles(fileData, "json");
        List<File> csvFileList = fileFinder.findFiles(fileData, "CSV");
        fileFinder.printResults(jsonFileList, "json");
        System.out.println();
        fileFinder.printResults(csvFileList, "csv");

        System.out.println();

        JsonParser jsonParser = new JsonParser();
        List<StationInfoFromJson> stationsInfo = jsonParser.parseJson(jsonFileList.get(0).getAbsolutePath());
        jsonParser.printStationInfo(stationsInfo);

        System.out.println();

        CsvParser csvParser = new CsvParser();
        List<StationDateFromCsv> stationsDates = csvParser.parseCsv(csvFileList.get(0).getAbsolutePath());
        csvParser.printStationDate(stationsDates);

        String outputFileMap = "data/output/map.json";
        JsonWriter.writeMapJson(outputFileMap);

        String outputFileStations = "data/output/stations.json";
        Map<StationKey, Station> stations = JsonWriter.enrichStationMapWithDateAndDepth(csvFileList, jsonFileList);
        JsonWriter.writeStationsJson(stations, outputFileStations);
    }
}