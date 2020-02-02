package com.mattjamesdev.tyft.navigation.shiftlog.shiftlog;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mattjamesdev.tyft.Database.Shift;
import com.mattjamesdev.tyft.Database.ShiftViewModel;
import com.mattjamesdev.tyft.R;

import java.util.List;

public class ShiftLogFragment extends Fragment {
    // Components
    private ShiftViewModel mShiftViewModel;
    private View root;

    // Widgets
    private RecyclerView mShiftRecyclerView;

    private OnFragmentInteractionListener mListener;

    public ShiftLogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShiftViewModel = ViewModelProviders.of(requireActivity()).get(ShiftViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_shift_log, container, false);
        buildRecyclerView();

        return root;
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

    private void buildRecyclerView(){
        mShiftRecyclerView = root.findViewById(R.id.recyclerView_shifts);
        final ShiftAdapter adapter = new ShiftAdapter();
        mShiftRecyclerView.setAdapter(adapter);
        mShiftRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        mShiftViewModel.getAllShifts().observe(this, new Observer<List<Shift>>() {
            @Override
            public void onChanged(List<Shift> shifts) {
                adapter.setShifts(shifts);
            }
        });
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
