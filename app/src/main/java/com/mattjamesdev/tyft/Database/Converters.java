package com.mattjamesdev.tyft.Database;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class Converters {
    @TypeConverter
    public static LocalDate fromTimeStamp(Long timeStamp){
        return timeStamp == null ? null : Instant.ofEpochMilli(timeStamp).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @TypeConverter
    public static Long dateToTimeStamp(LocalDate date){
        return date == null ? null : date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
    }
}
