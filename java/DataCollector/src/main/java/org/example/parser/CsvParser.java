package org.example.parser;

import org.example.model.StationDateFromCsv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {
    public List<StationDateFromCsv> parseCsv(String path) {
        List<StationDateFromCsv> stationsDates = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        List<String> lines = new ArrayList<>();

        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            System.err.println("Ошибка при чтении CSV файла " + path + ": " + e.getMessage());
        }

        List<String> dataLines = lines.subList(1, lines.size());

        if (dataLines.isEmpty()) {
            System.err.println("CSV файл пуст или не содержит заголовка: " + path);
            return stationsDates;
        }

        for (String dataLine : dataLines) {
            String[] fragments = dataLine.split(",");

            if (fragments.length != 2) {
                System.out.println("Неверный формат строки: " + dataLine);
                continue;
            }

            try {
                String name = fragments[0].trim();
                String dateString = fragments[1].trim();
                LocalDate date = LocalDate.parse(dateString, dateTimeFormatter);

                StationDateFromCsv stationDateFromCsv = new StationDateFromCsv();
                stationDateFromCsv.setName(name);
                stationDateFromCsv.setDate(date);
                stationsDates.add(stationDateFromCsv);
            } catch (java.time.format.DateTimeParseException e) {
                System.err.println("Ошибка при парсинге даты в строке: " + dataLine);
            }
        }
        return stationsDates;
    }

    public void printStationDate(List<StationDateFromCsv> stationsDates) {
        if (stationsDates == null || stationsDates.isEmpty()) {
            System.out.println("Не удалось получить данные о станциях из CSV.");
        } else {
            System.out.println("Список станций из CSV:");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            for (StationDateFromCsv stationDateFromCsv : stationsDates) {
                System.out.println("Станция: " + stationDateFromCsv.getName() +
                        "; дата открытия: " + stationDateFromCsv.getDate().format(outputFormatter) + " г.");
            }
        }
    }
}
