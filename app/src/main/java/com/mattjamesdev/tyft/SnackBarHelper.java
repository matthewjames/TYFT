package com.mattjamesdev.tyft;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.ViewCompat;

import com.google.android.material.snackbar.Snackbar;

public class SnackBarHelper {
    private static final String TAG = "SnackBarHelper";

    public static void configSnackbar(Context context, Snackbar snackbar){
        addMargins(snackbar);
        setBackground(context, snackbar);
        ViewCompat.setElevation(snackbar.getView(), 6f);
    }

    private static void addMargins(Snackbar snackbar){
        Log.d(TAG, "Adding margins to Snackbar");
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackbar.getView().getLayoutParams();
        params.setMargins(12, 12, 12, 12);
        snackbar.getView().setLayoutParams(params);
    }

    private static void setBackground(Context context, Snackbar snackbar){
        Log.d(TAG, "Setting background to Snackbar");
        snackbar.getView().setBackground(context.getDrawable(R.drawable.background_snackbar));
    }
}
