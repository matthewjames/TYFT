package com.mattjamesdev.tyft;


import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mattjamesdev.tyft.Database.Shift;
import com.mattjamesdev.tyft.Database.ShiftViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShiftRecordEditorFragment extends Fragment {
    private static final String TAG = "ShiftRecordEditorFragme";

    // Components
    private ShiftViewModel shiftViewModel;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");

    // Widgets
    private EditText editTextTakeHomeTipAmount;
    private TextView textViewDate;
    private Button buttonCancel,
                    buttonSave;
    private ImageView iconDate,
                        iconTipOutCalculator;

    public ShiftRecordEditorFragment() {
        // Required empty public constructor
    }

    public static ShiftRecordEditorFragment newInstance(){
        return new ShiftRecordEditorFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shiftViewModel = ViewModelProviders.of(requireActivity()).get(ShiftViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_shift_record_editor, container, false);

        // Widget instantiation
        textViewDate = root.findViewById(R.id.textView_shiftRecordDate);
        editTextTakeHomeTipAmount = root.findViewById(R.id.editText_shiftRecordTakeHomeTipAmount);
        buttonCancel = root.findViewById(R.id.button_shiftCancel);
        buttonSave = root.findViewById(R.id.button_shiftSave);
        iconDate = root.findViewById(R.id.icon_shiftRecordDate);
        iconTipOutCalculator = root.findViewById(R.id.icon_shiftRecordTipOutCalc);

        setupButtons();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shiftViewModel.getShift().observe(requireActivity(), new Observer<Shift>() {
            @Override
            public void onChanged(Shift shift) {
                textViewDate.setText(dateTimeFormatter.format(shift.getDate()));
                editTextTakeHomeTipAmount.setText("$" + String.valueOf(shift.getTipAmount()));
            }
        });
    }

    private void setupButtons(){
        // Icon Date
        iconDate.setOnClickListener(v ->{
            LocalDate date = LocalDate.now();

            Log.d(TAG, "iconDate.onClick(): Opening date picker with date: " + date);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    textViewDate.setText(dateTimeFormatter.format(LocalDate.of(year, month+1, dayOfMonth)));
                }
            }, date.getYear(), date.getMonthValue()-1, date.getDayOfMonth());
            datePickerDialog.show();
        });

        // Icon Tip Out Calculator

        // Cancel button
        buttonCancel.setOnClickListener(v ->{
            getFragmentManager().popBackStack();
        });

        // Save button
        buttonSave.setOnClickListener(v ->{
            Shift shift = new Shift(
                    LocalDate.parse(textViewDate.getText().toString(), dateTimeFormatter),
                    Integer.parseInt(editTextTakeHomeTipAmount.getText().toString().substring(1))
            );

            Log.d(TAG, "buttonSave.onClick(): Inserting " + shift + " into database");
            shiftViewModel.insert(shift);
        });
    }
}
