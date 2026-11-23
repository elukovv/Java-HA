package org.example;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class ReportFormatter {
    public static String formatWindow(MaintenanceWindow window, ZoneId zone, Locale locale) {
        Objects.requireNonNull(window, "window is required");
        Objects.requireNonNull(zone, "zone is required");
        Objects.requireNonNull(locale, "locale is required");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm XXX (VV)", locale);
        return formatter.format(window.start().atZone(zone)) + " – "
                + formatter.format(window.end().atZone(zone));
    }
}
