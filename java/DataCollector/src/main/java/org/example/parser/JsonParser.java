package org.example.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.StationInfoFromJson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class JsonParser {
    public List<StationInfoFromJson> parseJson(String path) {
        try {
            String json = Files.readString(Paths.get(path));
            ObjectMapper objectMapper = new ObjectMapper();
            StationInfoFromJson[] stationInfoFromJsonArray = objectMapper.readValue(json, StationInfoFromJson[].class);
            return Arrays.asList(stationInfoFromJsonArray);
        } catch (IOException e) {
            System.err.println("Ошибка при парсинге JSON файла " + path + ": " + e.getMessage());
            return null;
        }

    }

    public void printStationInfo(List<StationInfoFromJson> stationsInfo) {
        if (stationsInfo == null || stationsInfo.isEmpty()) {
            System.out.println("Не удалось получить данные о станциях из JSON.");
        } else {
            System.out.println("Список станций из JSON:");
            for (StationInfoFromJson stationInfoFromJson : stationsInfo) {
                System.out.println("Станция: " + stationInfoFromJson.getStationName() +
                        "; глубина: " + stationInfoFromJson.getDepth() + " м");
            }
        }
    }
}
