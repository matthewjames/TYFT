package com.mattjamesdev.tyft.navigation.tipoutcalculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mattjamesdev.tyft.R;

import java.util.ArrayList;
import java.util.Locale;

public class EditPositionDialog extends DialogFragment {
    private static final String TAG = "EditPositionDialog";

    public interface EditPositionDialogListener{
        void applyPositionDetails(double tipOutPercentage, String position, String name);
    }

    // components
    ConstraintLayout dialogLayout;
    View view;
    Dialog dialog;
    AlertDialog.Builder builder;
    EditPositionDialogListener listener;

    // widgets
    CardView cancelButton, saveButton;
    MaterialSpinner spinnerPositions;
    EditText editTextEmployeeName, editTextTipOutPercentage;

    // variables
    String selectedPosition, title = "Edit Position";
    PositionItem positionItem;
    ArrayList<String> positions;

    public EditPositionDialog(String dialogTitle){
        title = dialogTitle;
    }

    public EditPositionDialog(PositionItem pi){
        positionItem = pi;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // component instantiation
        view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_position, null);
        builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        dialogLayout = view.findViewById(R.id.layout_positionDialog);

        // widget instantiation
//        cancelButton = view.findViewById(R.id.cardView_cancelButton);
//        saveButton = view.findViewById(R.id.cardView_saveButton);
        spinnerPositions = view.findViewById(R.id.spinner_positions);
        editTextEmployeeName = view.findViewById(R.id.editText_name);
        editTextTipOutPercentage = view.findViewById(R.id.editText_tipOutPercentage);

        buildSpinner();
        setDialogValues();

        dialogLayout.setVisibility(View.VISIBLE);
        builder.setView(view);
        builder.setTitle(title);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // handle null inputs
                try {
                    String employeeName = editTextEmployeeName.getText().toString();
                    double tipOutPercentage = Double.valueOf(editTextTipOutPercentage.getText().toString());
                    listener.applyPositionDetails(tipOutPercentage, selectedPosition, employeeName);
                } catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        dialog = builder.create();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // add OnClickListeners to buttons


        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (EditPositionDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement EditPositionDialogListener");
        }
    }

    private void setDialogValues(){
        if(positionItem != null) {
            Log.d(TAG, "setDialogValues: Passing values [Tip Out Perc: " + positionItem.getmTipPerc() +
                    ", Position: " + positionItem.getmPosition() +
                    ", Name: " + positionItem.getmName() + "]");

            editTextTipOutPercentage.setText(String.valueOf(positionItem.getmTipPerc()));
            spinnerPositions.setSelectedIndex(positions.indexOf(positionItem.getmPosition()));
            selectedPosition = positions.get(positions.indexOf(positionItem.getmPosition()));
            editTextEmployeeName.setText(positionItem.getmName());
        } else {
            editTextTipOutPercentage.setText("0.0");
        }
    }

    private void buildSpinner(){
        positions = new ArrayList<>();
        positions.add("Busser");
        positions.add("Runner");
        positions.add("Salad");
        positions.add("Host");
        positions.add("Kitchen");
        positions.add("Bar");

        spinnerPositions.setItems(positions);
        selectedPosition = positions.get(spinnerPositions.getSelectedIndex());
        spinnerPositions.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                selectedPosition = item;
            }
        });
    }

}
