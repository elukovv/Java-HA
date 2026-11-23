package org.example;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MaintenanceScheduler {
    private final Clock clock;

    public MaintenanceScheduler(Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock is required");
    }

    public MaintenanceWindow nextWindow(
            LocalDate date, LocalTime localStart, Duration duration,
            ZoneId zone, OverlapPolicy policy) {
        Objects.requireNonNull(date, "date is required");
        validate(localStart, duration, zone, policy);
        return createWindow(date, localStart, duration, zone, policy);
    }

    public List<MaintenanceWindow> upcomingWindows(
            int days, LocalTime localStart, Duration duration,
            ZoneId zone, OverlapPolicy policy) {
        if (days < 0) {
            throw new IllegalArgumentException("days must not be negative");
        }
        validate(localStart, duration, zone, policy);
        if (days == 0) {
            return List.of();
        }

        LocalDate today = LocalDate.now(clock.withZone(zone));
        List<MaintenanceWindow> windows = new ArrayList<>();
        for (int day = 0; day < days; day++) {
            windows.add(createWindow(today.plusDays(day), localStart, duration, zone, policy));
        }
        return windows;
    }

    private void validate(LocalTime localStart, Duration duration, ZoneId zone, OverlapPolicy policy) {
        Objects.requireNonNull(localStart, "localStart is required");
        Objects.requireNonNull(duration, "duration is required");
        Objects.requireNonNull(zone, "zone is required");
        Objects.requireNonNull(policy, "policy is required");
        if (duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException("duration must be positive");
        }
    }

    private MaintenanceWindow createWindow(
            LocalDate date, LocalTime localStart, Duration duration,
            ZoneId zone, OverlapPolicy policy) {
        LocalDateTime localDateTime = date.atTime(localStart);
        if (zone.getRules().getValidOffsets(localDateTime).isEmpty()) {
            throw new DateTimeException("Local time " + localDateTime + " does not exist in " + zone);
        }

        ZonedDateTime zonedStart = localDateTime.atZone(zone);
        if (policy == OverlapPolicy.EARLIER) {
            zonedStart = zonedStart.withEarlierOffsetAtOverlap();
        } else {
            zonedStart = zonedStart.withLaterOffsetAtOverlap();
        }
        Instant start = zonedStart.toInstant();
        return new MaintenanceWindow(start, start.plus(duration));
    }
}
