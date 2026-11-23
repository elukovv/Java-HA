package org.example;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public record MaintenanceWindow(Instant start, Instant end) {
    public MaintenanceWindow {
        Objects.requireNonNull(start, "start is required");
        Objects.requireNonNull(end, "end is required");
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("end must be after start");
        }
    }

    public Duration duration() {
        return Duration.between(start, end);
    }
}
