package com.mattjamesdev.tyft.navigation.tipoutcalculator;

public class PositionItem {
    private int mTipOut;
    private double mTipPerc;
    private String mPosition, mName;

    public PositionItem(double tipPercentage, String position, String name){
        mTipPerc = tipPercentage;
        mPosition = position;
        mName = name;
    }

    public double getmTipPerc() {
        return mTipPerc;
    }

    public void setmTipPerc(double mTipPerc) {
        this.mTipPerc = mTipPerc;
    }

    public String getmPosition() {
        return mPosition;
    }

    public void setmPosition(String mPosition) {
        this.mPosition = mPosition;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmTipOut() {
        return mTipOut;
    }

    public void setmTipOut(int mTipOut) {
        this.mTipOut = mTipOut;
    }
}
