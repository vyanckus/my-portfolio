package org.example.parser;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class JsonWriter {

    public static List<MapLines> createMapLines() {
        List<MapLines> mapLinesList = new ArrayList<>();
        List<MetroLineFromHtml> metroLinesFromHtml = HtmlParser.parseMetroLines();

        for (MetroLineFromHtml metroLineFromHtml : metroLinesFromHtml) {
            MapLines mapLine = new MapLines();
            mapLine.setName(metroLineFromHtml.getNameLine());
            mapLine.setNumber(metroLineFromHtml.getNumberLine());
            mapLinesList.add(mapLine);
        }
        return mapLinesList;
    }

    public static Map<String, List<String>> createMapStations() {
        HashMap<String, MetroStationFromHtml> metroStationsFromHtml = HtmlParser.parseMetroStation();
        Map<String, List<String>> mapStations = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                try {
                    int num1 = Integer.parseInt(o1);
                    int num2 = Integer.parseInt(o2);
                    return Integer.compare(num1, num2);
                } catch (NumberFormatException e) {
                    return o1.compareTo(o2);
                }
            }
        });

        for (Map.Entry<String, MetroStationFromHtml> entry : metroStationsFromHtml.entrySet()) {
            String stationName = entry.getKey();
            MetroStationFromHtml stationInfo = entry.getValue();
            List<String> numbersLine = stationInfo.getNumbersLine();

            for (String numberLine : numbersLine) {
                if (mapStations.containsKey(numberLine)) {
                    mapStations.get(numberLine).add(stationName);
                } else {
                    List<String> stationList = new ArrayList<>();
                    stationList.add(stationName);
                    mapStations.put(numberLine, stationList);
                }
            }
        }
        return mapStations;
    }

    public static MapData createMapData() {
        MapData mapData = new MapData();
        mapData.setStations(createMapStations());
        mapData.setLines(createMapLines());
        return mapData;
    }

    public static void writeMapJson(String outputFileMap) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());
        String mapJson = objectWriter.writeValueAsString(createMapData());
        File mapJsonFile = new File(outputFileMap);
        FileWriter fileWriter = new FileWriter(mapJsonFile);
        fileWriter.write(mapJson);
        fileWriter.close();
    }

    public static List<StationInfoFromJson> parseJsonFiles(List<File> jsonFiles) {
        JsonParser jsonParser = new JsonParser();
        List<StationInfoFromJson> stationsInfoDepthAllList = new ArrayList<>();

        for (File jsonFile : jsonFiles) {
            List<StationInfoFromJson> stationsInfoDepthList = jsonParser.parseJson(jsonFile.getAbsolutePath());
            stationsInfoDepthAllList.addAll(stationsInfoDepthList);
        }
        return stationsInfoDepthAllList;
    }

    public static List<StationDateFromCsv> parseCsvFiles(List<File> csvFiles) {
        CsvParser csvParser = new CsvParser();
        List<StationDateFromCsv> stationsInfoDateAllList = new ArrayList<>();

        for (File csvFile : csvFiles) {
            List<StationDateFromCsv> stationsInfoDateList = csvParser.parseCsv(csvFile.getAbsolutePath());
            stationsInfoDateAllList.addAll(stationsInfoDateList);
        }
        return stationsInfoDateAllList;
    }

    public static Map<StationKey, Station> createStationMapWithLineNumbers() {
        Map<StationKey, Station> stations = new HashMap<>();

        Map<String, List<String>> mapStationNameByLineNumber = createMapStations();

        for (Map.Entry<String, List<String>> entry : mapStationNameByLineNumber.entrySet()) {
            String numberLine = entry.getKey();
            List<String> nameStations = entry.getValue();

            for (String nameStation : nameStations) {
                StationKey stationKey = new StationKey(nameStation, numberLine);
                Station station = new Station();
                station.setName(nameStation);
                station.setLine(numberLine);
                stations.put(stationKey, station);
            }
        }
        return stations;
    }

    public static Map<StationKey, Station> enrichStationMapWithLineNames() {
        Map<StationKey, Station> stations = createStationMapWithLineNumbers();

        List<MetroLineFromHtml> metroLinesFromHtml = HtmlParser.parseMetroLines();
        Map<String, String> lineNameByNumber = new HashMap<>();

        for (MetroLineFromHtml metroLineFromHtml : metroLinesFromHtml) {
            lineNameByNumber.put(metroLineFromHtml.getNumberLine(), metroLineFromHtml.getNameLine());
        }

        for (Station station : stations.values()) {
            String lineNumber = station.getLine();
            String lineName = lineNameByNumber.get(lineNumber);
            if (lineName != null) {
                station.setLine(lineName);
            }
        }
        return stations;
    }

    public static Map<StationKey, Station> enrichStationMapWithInfoAboutConnection() {
        Map<StationKey, Station> stations = enrichStationMapWithLineNames();

        HashMap<String, MetroStationFromHtml> stationsInfoAboutConnection = HtmlParser.parseMetroStation();

        for (Station station : stations.values()) {
            String stationName = station.getName();
            List<String> numbersLine = stationsInfoAboutConnection.get(stationName).getNumbersLine();
            if (numbersLine.size() > 1) {
                station.setHasConnection(true);
            }
        }
        return stations;
    }

    public static Map<StationKey, Station> enrichStationMapWithDateAndDepth(List<File> csvFiles, List<File> jsonFiles) {
        Map<StationKey, Station> stations = enrichStationMapWithInfoAboutConnection();

        List<StationDateFromCsv> stationDateFromCsvList = parseCsvFiles(csvFiles);
        Map<String, LocalDate> stationDateByName = new HashMap<>();

        for (StationDateFromCsv stationDateFromCsv : stationDateFromCsvList) {
            stationDateByName.put(stationDateFromCsv.getName(), stationDateFromCsv.getDate());
        }

        List<StationInfoFromJson> stationDepthFromJsonList = parseJsonFiles(jsonFiles);
        Map<String, String> stationDepthByName = new HashMap<>();

        for (StationInfoFromJson stationDepthFromJson : stationDepthFromJsonList) {
            String stationNameFromJson = stationDepthFromJson.getStationName();
            String depthFromJson = stationDepthFromJson.getDepth();

            if (depthFromJson == null) {
                continue;
            }

            double doubleDepthFromJson;
            try {
                doubleDepthFromJson = Double.parseDouble(depthFromJson.replace(",", "."));
            } catch (NumberFormatException e) {
                continue;
            }

            if (stationDepthByName.containsKey(stationNameFromJson)) {
                String depthFromMap = stationDepthByName.get(stationNameFromJson);
                double doubleDepthFromMap = Double.parseDouble(depthFromMap.replace(",", "."));
                if (doubleDepthFromJson <= doubleDepthFromMap) {
                    stationDepthByName.replace(stationNameFromJson, depthFromJson);
                }
            } else {
                stationDepthByName.put(stationNameFromJson, depthFromJson);
            }
        }

        for (Station station : stations.values()) {
            String stationName = station.getName();
            if (stationDateByName.containsKey(stationName)) {
                station.setDate(stationDateByName.get(stationName));
            }
            if (stationDepthByName.containsKey(stationName)) {
                station.setDepth(stationDepthByName.get(stationName));
            }
        }
        return stations;
    }

    public static void writeStationsJson(Map<StationKey, Station> stations, String outputFileStations) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());
        String stationsJson = objectWriter.writeValueAsString(stations);
        File stationsJsonFile = new File(outputFileStations);
        FileWriter fileWriter = new FileWriter(stationsJsonFile);
        fileWriter.write(stationsJson);
        fileWriter.close();
    }
}
