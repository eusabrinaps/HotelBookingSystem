package com.hotel.booking.ifsp.infrastructure.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Converter
public class LocalDateConverter implements AttributeConverter<LocalDate, String> {

    private static final DateTimeFormatter SQLITE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public String convertToDatabaseColumn(LocalDate date) {
        if (date == null) return null;
        return date.atStartOfDay().format(SQLITE_FORMAT);
    }

    @Override
    public LocalDate convertToEntityAttribute(String value) {
        if (value == null) return null;
        try {
            return LocalDate.parse(value, SQLITE_FORMAT);
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e2) {
                long epochMs = Long.parseLong(value);
                return Instant.ofEpochMilli(epochMs).atZone(ZoneId.systemDefault()).toLocalDate();
            }
        }
    }
}
