package com.javastudio.grandmafood.core.utils;

import org.assertj.core.api.Assertions;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class CustomAssertions {


    /**
     * Asserts that the given datetime is close to the current datetime within the specified delta.
     *
     * @param datetime the datetime to compare with the current datetime
     * @param delta    the maximum difference (in seconds) allowed between the datetime and the current datetime
     */
    public static void assertDateTimeIsCloseToNow(LocalDateTime datetime, long delta) {
        long diffSeconds = ChronoUnit.SECONDS.between(datetime, LocalDateTime.now(ZoneOffset.UTC));
        Assertions.assertThat(Math.abs(diffSeconds)).isLessThan(delta);
    }
}
