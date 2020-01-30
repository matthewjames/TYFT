package com.mattjamesdev.tyft.navigation.tipoutcalculator;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.mattjamesdev.tyft.R;
import com.mattjamesdev.tyft.SnackBarHelper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TipOutCalculatorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TipOutCalculatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TipOutCalculatorFragment extends Fragment implements EditPositionDialog.EditPositionDialogListener {
    // components
    private RecyclerView mPositionRecyclerView;
    private PositionAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<PositionItem> mPositionList;
    private Fragment hostFragment;
    private View root;

    // widgets
    private TextView textViewQuickAdd,
                     editTextGrossGratuity,
                     textViewGrossGratuityAmount,
                     textViewTotalTipOutAmount,
                     textViewNetGratuityAmount;
    private Button buttonClear,
                    buttonCalculate;

    // variables
    private PositionItem currentPositionItem;
    private boolean isQuickAdd = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TipOutCalculatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TipOutCalculatorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TipOutCalculatorFragment newInstance(String param1, String param2) {
        TipOutCalculatorFragment fragment = new TipOutCalculatorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        buildPositionRecyclerView(root);
        setupButtons(root);

        return root;
    }

    private void setupButtons(View v){
        // Quick add button
        textViewQuickAdd = v.findViewById(R.id.textView_quickAdd);
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
        buttonClear = v.findViewById(R.id.buttonReset);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextGrossGratuity.setText("");
                calculate("");
            }
        });

        // Calculate button
        buttonCalculate = v.findViewById(R.id.buttonCalculateTipOut);
        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate(editTextGrossGratuity.getText().toString());
            }
        });
    }

    private void calculate(String grossGratuityInput){
        if(!grossGratuityInput.equals("")) {
            int totalTipOut = 0,
                    grossGratuity = Integer.parseInt(grossGratuityInput);

            // Calculate individual tip outs
            for (PositionItem pi : mPositionList) {
                int tipOutAmount = (int) Math.round((pi.getmTipPerc() * 0.01) * grossGratuity);
                pi.setmTipOut(tipOutAmount);
                totalTipOut += tipOutAmount;
            }

            mAdapter.notifyDataSetChanged();

            textViewGrossGratuityAmount.setText("$" + String.valueOf(grossGratuity) + " ");
            textViewTotalTipOutAmount.setText("-$" + String.valueOf(totalTipOut) + " ");
            textViewNetGratuityAmount.setText("$" + String.valueOf(grossGratuity - totalTipOut) + " ");
        } else {
            for (PositionItem pi : mPositionList) {
                pi.setmTipOut(0);
            }

            mAdapter.notifyDataSetChanged();

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

    private void buildPositionRecyclerView(View root){
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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
