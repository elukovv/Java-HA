package org.example;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public record Incident(String id, String service, Instant startedAt, Instant resolvedAt) {
    public Incident {
        Objects.requireNonNull(id, "id is required");
        Objects.requireNonNull(service, "service is required");
        Objects.requireNonNull(startedAt, "startedAt is required");
        Objects.requireNonNull(resolvedAt, "resolvedAt is required");

        if (id.isBlank() || service.isBlank()) {
            throw new IllegalArgumentException("id and service must not be blank");
        }
        if (resolvedAt.isBefore(startedAt)) {
            throw new IllegalArgumentException("resolvedAt must not be before startedAt");
        }
    }

    public Duration duration() {
        return Duration.between(startedAt, resolvedAt);
    }
}
