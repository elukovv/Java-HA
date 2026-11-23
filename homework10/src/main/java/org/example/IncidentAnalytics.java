package org.example;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

public class IncidentAnalytics {
    private static final Comparator<Incident> START_ORDER = Comparator
            .comparing(Incident::startedAt)
            .thenComparing(Incident::id);

    public static Duration totalDowntime(List<Incident> incidents) {
        Objects.requireNonNull(incidents, "incidents is required");
        Duration total = Duration.ZERO;
        for (Incident incident : incidents) {
            total = total.plus(Objects.requireNonNull(incident, "incident is required").duration());
        }
        return total;
    }

    public static Duration averageDowntime(List<Incident> incidents) {
        Objects.requireNonNull(incidents, "incidents is required");
        if (incidents.isEmpty()) {
            return Duration.ZERO;
        }

        BigInteger nanosPerSecond = BigInteger.valueOf(1_000_000_000);
        BigInteger totalNanos = BigInteger.ZERO;
        for (Incident incident : incidents) {
            Duration duration = Objects.requireNonNull(incident, "incident is required").duration();
            BigInteger nanos = BigInteger.valueOf(duration.getSeconds())
                    .multiply(nanosPerSecond)
                    .add(BigInteger.valueOf(duration.getNano()));
            totalNanos = totalNanos.add(nanos);
        }

        BigInteger average = totalNanos.divide(BigInteger.valueOf(incidents.size()));
        BigInteger[] parts = average.divideAndRemainder(nanosPerSecond);
        return Duration.ofSeconds(parts[0].longValueExact(), parts[1].longValueExact());
    }

    public static Optional<Incident> longestIncident(List<Incident> incidents) {
        Objects.requireNonNull(incidents, "incidents is required");
        Comparator<Incident> order = Comparator.comparing(Incident::duration)
                .reversed()
                .thenComparing(START_ORDER);
        return incidents.stream()
                .map(incident -> Objects.requireNonNull(incident, "incident is required"))
                .min(order);
    }

    public static Map<LocalDate, List<Incident>> groupByStartDate(List<Incident> incidents, ZoneId zone) {
        Objects.requireNonNull(incidents, "incidents is required");
        Objects.requireNonNull(zone, "zone is required");

        Map<LocalDate, List<Incident>> groups = new TreeMap<>();
        for (Incident incident : incidents) {
            Objects.requireNonNull(incident, "incident is required");
            LocalDate date = incident.startedAt().atZone(zone).toLocalDate();
            groups.computeIfAbsent(date, day -> new ArrayList<>()).add(incident);
        }
        for (List<Incident> group : groups.values()) {
            group.sort(START_ORDER);
        }
        return groups;
    }
}
