package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MetroLineFromHtml {
    private final String nameLine;
    private final String numberLine;

    @Override
    public String toString() {
        return nameLine + " - номер " + numberLine;
    }
}
