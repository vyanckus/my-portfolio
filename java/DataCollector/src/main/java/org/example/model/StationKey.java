package org.example.model;

import lombok.Data;

@Data
public class StationKey {
    private final String name; // Station name
    private final String line; // Metro line number
}
