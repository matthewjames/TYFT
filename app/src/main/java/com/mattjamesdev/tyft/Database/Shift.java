package com.mattjamesdev.tyft.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "shift_table")
public class Shift {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "date")
    private LocalDate mDate;
    private int mTipAmount;

    public Shift(@NonNull LocalDate date, int tipAmount){
        this.mDate = date;
        this.mTipAmount = tipAmount;
    }

    public LocalDate getmDate() {
        return mDate;
    }

    public void setmDate(LocalDate mDate) {
        this.mDate = mDate;
    }

    public int getmTipAmount() {
        return mTipAmount;
    }

    public void setmTipAmount(int mTipAmount) {
        this.mTipAmount = mTipAmount;
    }
}
