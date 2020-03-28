package com.arvin.klar.assignment.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtility {
    private static SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("yyyy-MM-dd");

    /**
     * Converts a UNIX timestamp in milliseconds to a formatted date string.
     *
     * @param timestamp UNIX timestamp in milliseconds to convert
     * @return a formatted {@link String} describing a date
     */
    public static String timestampToDateString(final long timestamp) {
        final Date date = new Date(timestamp);
        return DATE_FORMAT.format(date);
    }
}
