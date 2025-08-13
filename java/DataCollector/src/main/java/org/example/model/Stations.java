package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class Stations {
    private Map<String, Station> stations;
}
