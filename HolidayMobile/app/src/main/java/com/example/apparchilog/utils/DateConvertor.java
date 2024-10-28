package com.example.apparchilog.utils;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateConvertor {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static OffsetDateTime convertStringToOffsetDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            return localDate.atStartOfDay().atOffset(ZoneOffset.UTC);
        } catch (DateTimeParseException e) {
            return null; // or throw an exception, depending on your needs
        }
    }
    public static String convertOffsetDateTimeToString(OffsetDateTime offsetDateTime) {
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return offsetDateTime.format(formatter);
        }
        return "";
    }

}
