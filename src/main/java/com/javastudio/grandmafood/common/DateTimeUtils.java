package com.javastudio.grandmafood.common;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeUtils {

    public static LocalDateTime convertToBogotaTime(LocalDateTime utcDateTime) {
        ZoneId utcZone = ZoneId.of("UTC");
        ZoneId bogotaZone = ZoneId.of("America/Bogota");

        ZonedDateTime utcZonedDateTime = utcDateTime.atZone(utcZone);
        ZonedDateTime bogotaZonedDateTime = utcZonedDateTime.withZoneSameInstant(bogotaZone);

        return bogotaZonedDateTime.toLocalDateTime();
    }
}
