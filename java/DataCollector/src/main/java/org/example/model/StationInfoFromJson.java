package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StationInfoFromJson {
    @JsonProperty("station_name")
    private String stationName;
    private String depth;
}
