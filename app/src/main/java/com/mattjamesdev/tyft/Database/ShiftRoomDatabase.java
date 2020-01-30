package com.mattjamesdev.tyft.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Shift.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ShiftRoomDatabase extends RoomDatabase {
    public abstract ShiftDao shiftDao();

    private static volatile ShiftRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ShiftRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (ShiftRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ShiftRoomDatabase.class, "shift_database")
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
