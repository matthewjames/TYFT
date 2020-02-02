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
    private LocalDate date;

    @ColumnInfo(name = "tip_amt")
    private int tipAmount;

    public Shift(@NonNull LocalDate date, int tipAmount){
        this.date = date;
        this.tipAmount = tipAmount;
    }

    @NonNull
    public LocalDate getDate() {
        return date;
    }

    public void setDate(@NonNull LocalDate date) {
        this.date = date;
    }

    public int getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(int tipAmount) {
        this.tipAmount = tipAmount;
    }

    @Override
    public String toString() {
        return "Shift{" +
                "date=" + date +
                ", tipAmount=" + tipAmount +
                '}';
    }
}
