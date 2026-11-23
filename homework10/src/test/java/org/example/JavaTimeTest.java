package org.example;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JavaTimeTest {
    private final ZoneId berlin = ZoneId.of("Europe/Berlin");
    private final MaintenanceScheduler scheduler = new MaintenanceScheduler(
            Clock.fixed(Instant.parse("2024-03-29T23:00:00Z"), ZoneOffset.UTC));

    @Test
    void parsesIncident() {
        Incident incident = IncidentParser.parse(
                "INC-1;billing;2024-03-30T22:30:00Z;2024-03-31T01:00:00Z");

        assertEquals("INC-1", incident.id());
        assertEquals("billing", incident.service());
        assertEquals(Instant.parse("2024-03-30T22:30:00Z"), incident.startedAt());
        assertEquals(Instant.parse("2024-03-31T01:00:00Z"), incident.resolvedAt());
        assertEquals(Duration.ofMinutes(150), incident.duration());
    }

    @Test
    void rejectsInvalidIncidentLines() {
        assertThrows(IllegalArgumentException.class, () -> IncidentParser.parse(
                "INC-1;billing;wrong;2024-03-31T01:00:00Z"));
        assertThrows(IllegalArgumentException.class, () -> IncidentParser.parse(
                ";billing;2024-03-30T22:30:00Z;2024-03-31T01:00:00Z"));
        assertThrows(IllegalArgumentException.class, () -> IncidentParser.parse(
                "INC-1;billing;2024-03-31T01:00:00Z;2024-03-30T22:30:00Z"));
        assertThrows(IllegalArgumentException.class, () -> IncidentParser.parse(
                "INC-1;billing;2024-03-30T22:30:00Z"));
    }

    @Test
    void calculatesDowntimeAndRoundsAverageDown() {
        Instant start = Instant.EPOCH;
        Incident first = new Incident("A", "billing", start, start.plusNanos(2));
        Incident second = new Incident("B", "billing", start, start.plusNanos(5));
        List<Incident> incidents = List.of(first, second);

        assertEquals(Duration.ofNanos(7), IncidentAnalytics.totalDowntime(incidents));
        assertEquals(Duration.ofNanos(3), IncidentAnalytics.averageDowntime(incidents));
        assertEquals(Optional.of(second), IncidentAnalytics.longestIncident(incidents));
    }

    @Test
    void handlesEmptyIncidentList() {
        assertEquals(Duration.ZERO, IncidentAnalytics.totalDowntime(List.of()));
        assertEquals(Duration.ZERO, IncidentAnalytics.averageDowntime(List.of()));
        assertEquals(Optional.empty(), IncidentAnalytics.longestIncident(List.of()));
    }

    @Test
    void averagesDurationsLargerThanLongNanosecondsAndBreaksTies() {
        Instant start = Instant.EPOCH;
        Instant later = start.plusSeconds(1);
        Duration duration = Duration.ofDays(365_000);
        Incident first = new Incident("A", "billing", start, start.plus(duration));
        Incident second = new Incident("B", "billing", start, start.plus(duration));
        Incident third = new Incident("0", "billing", later, later.plus(duration));
        List<Incident> incidents = List.of(third, second, first);

        assertEquals(duration, IncidentAnalytics.averageDowntime(incidents));
        assertEquals(Optional.of(first), IncidentAnalytics.longestIncident(incidents));
    }

    @Test
    void groupsByBerlinDateAndSortsGroups() {
        Incident first = IncidentParser.parse(
                "INC-1;billing;2024-03-30T22:30:00Z;2024-03-31T01:00:00Z");
        Incident second = IncidentParser.parse(
                "INC-2;gateway;2024-03-30T23:30:00Z;2024-03-31T00:15:00Z");
        Incident third = IncidentParser.parse(
                "INC-3;gateway;2024-03-30T23:30:00Z;2024-03-31T00:15:00Z");
        List<Incident> incidents = List.of(third, second, first);

        var groups = IncidentAnalytics.groupByStartDate(incidents, berlin);
        LocalDate march30 = LocalDate.of(2024, 3, 30);
        LocalDate march31 = LocalDate.of(2024, 3, 31);

        assertEquals(List.of(march30, march31), new ArrayList<>(groups.keySet()));
        assertEquals(List.of(first), groups.get(march30));
        assertEquals(List.of(second, third), groups.get(march31));
    }

    @Test
    void rejectsBerlinSpringGap() {
        assertThrows(DateTimeException.class, () -> scheduler.nextWindow(
                LocalDate.of(2024, 3, 31), LocalTime.of(2, 30),
                Duration.ofMinutes(30), berlin, OverlapPolicy.EARLIER));
    }

    @Test
    void choosesBerlinOverlapOffset() {
        LocalDate date = LocalDate.of(2024, 10, 27);
        LocalTime time = LocalTime.of(2, 30);
        Duration duration = Duration.ofHours(1);
        MaintenanceWindow earlier = scheduler.nextWindow(date, time, duration, berlin, OverlapPolicy.EARLIER);
        MaintenanceWindow later = scheduler.nextWindow(date, time, duration, berlin, OverlapPolicy.LATER);

        assertEquals(ZoneOffset.ofHours(2), earlier.start().atZone(berlin).getOffset());
        assertEquals(ZoneOffset.ofHours(1), later.start().atZone(berlin).getOffset());
        assertEquals(Duration.ofHours(1), Duration.between(earlier.start(), later.start()));
        assertEquals(later.start(), earlier.end());
        assertEquals(duration, later.duration());
    }

    @Test
    void schedulesDailyWindowsAcrossSpringTransition() {
        LocalTime time = LocalTime.of(3, 30);
        Duration duration = Duration.ofMinutes(30);
        List<MaintenanceWindow> windows = scheduler.upcomingWindows(
                2, time, duration, berlin, OverlapPolicy.EARLIER);

        assertEquals(2, windows.size());
        var first = windows.get(0).start().atZone(berlin);
        var second = windows.get(1).start().atZone(berlin);
        assertEquals(LocalDate.of(2024, 3, 30), first.toLocalDate());
        assertEquals(LocalDate.of(2024, 3, 31), second.toLocalDate());
        assertEquals(time, first.toLocalTime());
        assertEquals(time, second.toLocalTime());
        assertEquals(Duration.ofHours(23), Duration.between(first.toInstant(), second.toInstant()));
        assertEquals(windows, scheduler.upcomingWindows(2, time, duration, berlin, OverlapPolicy.EARLIER));
    }

    @Test
    void formatsWindow() {
        MaintenanceWindow window = new MaintenanceWindow(
                Instant.parse("2024-03-31T01:00:00Z"), Instant.parse("2024-03-31T02:00:00Z"));

        assertEquals("31.03.2024 03:00 +02:00 (Europe/Berlin) – 31.03.2024 04:00 +02:00 (Europe/Berlin)",
                ReportFormatter.formatWindow(window, berlin, Locale.ROOT));
    }

    @Test
    void convertsLegacyDates() {
        Instant instant = Instant.parse("2024-03-31T01:00:00.123Z");
        Date date = new Date(instant.toEpochMilli());
        var zoned = instant.atZone(berlin);

        assertEquals(instant, LegacyDateAdapter.toInstant(date));
        assertEquals(date, LegacyDateAdapter.toDate(LegacyDateAdapter.toInstant(date)));
        assertEquals(zoned, LegacyDateAdapter.toZonedDateTime(LegacyDateAdapter.toGregorianCalendar(zoned)));
    }
}
