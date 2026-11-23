package org.example;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class IncidentParser {
    public static Incident parse(String line) {
        Objects.requireNonNull(line, "line is required");
        String[] fields = line.split(";", -1);
        if (fields.length != 4) {
            throw new IllegalArgumentException("Expected four fields separated by semicolons");
        }
        try {
            return new Incident(fields[0], fields[1], Instant.parse(fields[2]), Instant.parse(fields[3]));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid incident timestamp: " + e.getParsedString(), e);
        }
    }
}
