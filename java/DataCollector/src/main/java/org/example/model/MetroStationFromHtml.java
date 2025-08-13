package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MetroStationFromHtml {
    private final String nameStation;
    private final List<String> numbersLine;

    @Override
    public String toString() {
        String lines = String.join(", ", numbersLine);
        return nameStation + " - номер линии: " + lines;
    }
}
