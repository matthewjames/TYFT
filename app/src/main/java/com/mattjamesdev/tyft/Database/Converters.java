package com.mattjamesdev.tyft.Database;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class Converters {
    @TypeConverter
    public static LocalDate toDate(String dateString){
        return dateString == null ? null : LocalDate.parse(dateString);
    }


    @TypeConverter
    public static String toDateString(LocalDate date){
        return date == null ? null : date.toString();
    }
}
