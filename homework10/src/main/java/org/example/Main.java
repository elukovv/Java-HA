package org.example;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Clock clock = Clock.fixed(Instant.parse("2024-03-30T00:00:00Z"), ZoneOffset.UTC);
        ZoneId zone = ZoneId.of("Europe/Berlin");
        MaintenanceScheduler scheduler = new MaintenanceScheduler(clock);

        for (MaintenanceWindow window : scheduler.upcomingWindows(
                2, LocalTime.of(3, 30), Duration.ofMinutes(30), zone, OverlapPolicy.EARLIER)) {
            System.out.println(ReportFormatter.formatWindow(window, zone, Locale.ROOT));
        }
    }
}
