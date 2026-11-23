package org.example;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class LegacyDateAdapter {
    public static Instant toInstant(Date date) {
        Objects.requireNonNull(date, "date is required");
        return Instant.ofEpochMilli(date.getTime());
    }

    public static Date toDate(Instant instant) {
        return Date.from(Objects.requireNonNull(instant, "instant is required"));
    }

    public static ZonedDateTime toZonedDateTime(GregorianCalendar calendar) {
        return Objects.requireNonNull(calendar, "calendar is required").toZonedDateTime();
    }

    public static GregorianCalendar toGregorianCalendar(ZonedDateTime dateTime) {
        return GregorianCalendar.from(Objects.requireNonNull(dateTime, "dateTime is required"));
    }
}
