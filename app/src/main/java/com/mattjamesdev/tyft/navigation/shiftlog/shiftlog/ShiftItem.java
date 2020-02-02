package com.mattjamesdev.tyft.navigation.shiftlog.shiftlog;

import java.time.LocalDate;

public class ShiftItem {
    private LocalDate mDate;
    private int mTipAmount;

    public ShiftItem(LocalDate mDate, int mTipAmount) {
        this.mDate = mDate;
        this.mTipAmount = mTipAmount;
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
