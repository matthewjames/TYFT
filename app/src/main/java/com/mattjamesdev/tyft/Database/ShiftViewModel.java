package com.mattjamesdev.tyft.Database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ShiftViewModel extends AndroidViewModel {
    private ShiftRepository mRepository;
    private LiveData<List<Shift>> mAllShifts;

    public ShiftViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ShiftRepository(application);
        mAllShifts = mRepository.getAllShifts();
    }

    LiveData<List<Shift>> getAllShifts(){
        return mAllShifts;
    }

    public void insert(Shift shift){
        mRepository.insert(shift);
    }

    public void delete(Shift shift){
        mRepository.delete(shift);
    }
}
