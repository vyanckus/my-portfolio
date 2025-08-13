package org.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Station {
    private String name;
    private String line;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate date;
    private String depth;
    private boolean hasConnection;
}
