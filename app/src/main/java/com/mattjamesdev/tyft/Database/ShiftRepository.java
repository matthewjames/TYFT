package com.mattjamesdev.tyft.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class ShiftRepository {
    private ShiftDao mShiftDao;
    private LiveData<List<Shift>> mAllShifts;

    ShiftRepository(Application application){
        ShiftRoomDatabase db = ShiftRoomDatabase.getDatabase(application);
        mShiftDao = db.shiftDao();
        mAllShifts = mShiftDao.getAllShifts();
    }

    LiveData<List<Shift>> getAllShifts(){
        return mAllShifts;
    }

    void insert(Shift shift){
        ShiftRoomDatabase.databaseWriteExecutor.execute(()->{
            mShiftDao.insert(shift);
        });
    }

    void delete(Shift shift){
        ShiftRoomDatabase.databaseWriteExecutor.execute(()->{
            mShiftDao.delete(shift);
        });
    }
}
