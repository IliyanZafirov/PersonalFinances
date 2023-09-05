package com.main.personalfinances.converter;

import androidx.room.TypeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Convert LocalDateTime to proper format for the database
 */
public class LocalDateTimeConverter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @TypeConverter
    public static LocalDateTime toLocalDateTime(String value) {
        return value == null ? null : LocalDateTime.parse(value, formatter);
    }

    @TypeConverter
    public static String fromLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.format(formatter);
    }

}
