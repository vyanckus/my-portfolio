package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class StationDateFromCsv {
    private String name;
    private LocalDate date;
}
