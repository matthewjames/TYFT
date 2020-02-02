package com.mattjamesdev.tyft.navigation.tipoutcalculator;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.mattjamesdev.tyft.Database.Shift;
import com.mattjamesdev.tyft.Database.ShiftViewModel;
import com.mattjamesdev.tyft.R;
import com.mattjamesdev.tyft.ShiftRecordEditorFragment;
import com.mattjamesdev.tyft.SnackBarHelper;

import java.time.LocalDate;
import java.util.ArrayList;

public class TipOutCalculatorFragment extends Fragment implements EditPositionDialog.EditPositionDialogListener {
    private static final String TAG = "TipOutCalculatorFragmen";

    // components
    private PositionAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<PositionItem> mPositionList;
    private Fragment hostFragment;
    private View root;
    private ShiftViewModel shiftViewModel;
    private OnFragmentInteractionListener mListener;

    // widgets
    private RecyclerView mPositionRecyclerView;
    private TextView textViewQuickAdd,
                        editTextGrossGratuity,
                        textViewGrossGratuityAmount,
                        textViewTotalTipOutAmount,
                        textViewNetGratuityAmount;
    private Button buttonClear,
                    buttonCalculate,
                    buttonStartShiftRecord;

    // variables
    private PositionItem currentPositionItem;
    private boolean isQuickAdd = false;
    private int grossGratuity = 0,
                netGratuity = 0,
                totalTipOut = 0;

    public TipOutCalculatorFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shiftViewModel = ViewModelProviders.of(requireActivity()).get(ShiftViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_tip_out_calculator, container, false);
        editTextGrossGratuity = root.findViewById(R.id.textView_grossGratuity);
        textViewGrossGratuityAmount = root.findViewById(R.id.textView_grossGratuityAmount);
        textViewTotalTipOutAmount = root.findViewById(R.id.textView_totalTipOutAmount);
        textViewNetGratuityAmount = root.findViewById(R.id.textView_netGratuityAmount);

        hostFragment = this;
        createPositionList();
        buildPositionRecyclerView();
        setupButtons();

        return root;
    }

    private void setupButtons(){
        // Quick add button
        textViewQuickAdd = root.findViewById(R.id.textView_quickAdd);
        textViewQuickAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isQuickAdd = true;
                FragmentManager fm = getFragmentManager();
                EditPositionDialog editPositionDialog = new EditPositionDialog("Add Position");
                editPositionDialog.setTargetFragment(hostFragment, 0);
                editPositionDialog.show(fm, "quick_add_position");
            }
        });

        // Clear button
        buttonClear = root.findViewById(R.id.buttonReset);
        buttonClear.setOnClickListener(v -> {
            editTextGrossGratuity.setText("");
            calculate("");
        });

        // Calculate button
        buttonCalculate = root.findViewById(R.id.buttonCalculateTipOut);
        buttonCalculate.setOnClickListener(v -> calculate(editTextGrossGratuity.getText().toString()));

        // Start Shift Record button
        buttonStartShiftRecord = root.findViewById(R.id.buttonStartShiftRecord);
        buttonStartShiftRecord.setOnClickListener(v -> {
            if (netGratuity != 0) {
                Shift shift = new Shift(LocalDate.now(), netGratuity);
                Log.d(TAG, "StartShiftRecord: Passing " + shift + " to ShiftViewModel");

                shiftViewModel.setShift(shift);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, ShiftRecordEditorFragment.newInstance()).addToBackStack(null).commit();
            }
        });
    }

    private void calculate(String grossGratuityInput){
        if(!grossGratuityInput.equals("")) { // Gross gratuity is populated
            grossGratuity = Integer.parseInt(grossGratuityInput);

            // Calculate individual tip outs
            for (PositionItem pi : mPositionList) {
                int tipOutAmount = (int) Math.round((pi.getmTipPerc() * 0.01) * grossGratuity);
                pi.setmTipOut(tipOutAmount);
                totalTipOut += tipOutAmount;
            }

            mAdapter.notifyDataSetChanged();

            netGratuity = grossGratuity - totalTipOut;

            textViewGrossGratuityAmount.setText("$" + String.valueOf(grossGratuity) + " ");
            textViewTotalTipOutAmount.setText("-$" + String.valueOf(totalTipOut) + " ");
            textViewNetGratuityAmount.setText("$" + String.valueOf(netGratuity) + " ");
        } else { // Gross gratuity is empty
            for (PositionItem pi : mPositionList) {
                pi.setmTipOut(0);
            }

            mAdapter.notifyDataSetChanged();

            grossGratuity = 0;
            netGratuity = 0;
            totalTipOut = 0;

            textViewGrossGratuityAmount.setText(R.string.tip_amount_placeholder);
            textViewTotalTipOutAmount.setText(R.string.tip_amount_placeholder);
            textViewNetGratuityAmount.setText(R.string.tip_amount_placeholder);
        }
    }

    private void addPosition(PositionItem pi){
        mPositionList.add(pi);
        mAdapter.notifyItemInserted(mPositionList.size()-1);
    }

    private void createPositionList(){
        mPositionList = new ArrayList<>();
        mPositionList.add(new PositionItem(20, "Busser", "Antonio"));
        mPositionList.add(new PositionItem(7.5, "Bar", "Leslie"));
        mPositionList.add(new PositionItem(5, "Runner", "Rudell"));
        mPositionList.add(new PositionItem(5, "Salad", "Glen"));
    }

    private void buildPositionRecyclerView(){
        mPositionRecyclerView = root.findViewById(R.id.recyclerView_positions);
        mLayoutManager = new LinearLayoutManager(root.getContext());
        mAdapter = new PositionAdapter(mPositionList);

        mPositionRecyclerView.setLayoutManager(mLayoutManager);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mPositionRecyclerView);
        mPositionRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new PositionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                currentPositionItem = mPositionList.get(position);
                FragmentManager fm = getFragmentManager();
                EditPositionDialog editPositionDialog = new EditPositionDialog(currentPositionItem);
                editPositionDialog.setTargetFragment(hostFragment, 0);
                editPositionDialog.show(fm, "edit_position");
            }
        });
    }

    // Swipe to delete a PositionItem from RecyclerView
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            final PositionItem temp = mPositionList.get(position); // save the deleted PositionItem temporarily for undo operation
            mPositionList.remove(position);

            // get and format name (if needed)
            String name = temp.getmName();
            // if there is a name entered
            if(!name.equals("")){
                // format name
                name = String.format("(%s)", name);
            }

            // alert the user of deleted positionItem
            String snackBarMessage = String.format("%s %s removed", temp.getmPosition(), name);

            Snackbar snackbar = Snackbar.make(viewHolder.itemView, snackBarMessage, Snackbar.LENGTH_LONG)
            .setAction("Undo", new View.OnClickListener() { // add undo option to alert
                @Override
                public void onClick(View v) {
                    mPositionList.add(position, temp);
                    mAdapter.notifyDataSetChanged();
                }
            });

            SnackBarHelper.configSnackbar(getContext().getApplicationContext(), snackbar);
            snackbar.show();

            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void applyPositionDetails(double tipOutPercentage, String position, String name) {
        if(isQuickAdd){
            addPosition(new PositionItem(tipOutPercentage, position, name));
            isQuickAdd = false;
        } else {
            currentPositionItem.setmTipPerc(tipOutPercentage);
            currentPositionItem.setmPosition(position);
            currentPositionItem.setmName(name);
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
