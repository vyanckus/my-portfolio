package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class MapData {
    private Map<String, List<String>> stations;
    private List<MapLines> lines;
}
