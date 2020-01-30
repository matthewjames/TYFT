package com.mattjamesdev.tyft.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ShiftDao {

    @Insert
    void insert(Shift shift);

    @Delete
    void delete(Shift shift);

    @Query("SELECT * FROM shift_table ORDER BY date ASC")
    LiveData<List<Shift>> getAllShifts();
}
