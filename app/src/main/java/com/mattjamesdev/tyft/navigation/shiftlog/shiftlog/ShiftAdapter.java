package com.mattjamesdev.tyft.navigation.shiftlog.shiftlog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mattjamesdev.tyft.Database.Shift;
import com.mattjamesdev.tyft.R;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ShiftViewHolder> {

    private List<Shift> mShifts; // cached version of Shifts list
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy");

    @NonNull
    @Override
    public ShiftAdapter.ShiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shift, parent, false);

        return new ShiftViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftAdapter.ShiftViewHolder holder, int position) {
        // Use holder to access ShiftItem widgets.
        // Set values from cached version of List<Shift> using the position as index value.

        if(mShifts != null){
            if(holder.mDateTextView.getVisibility() == View.INVISIBLE){
                holder.mDateTextView.setVisibility(View.VISIBLE);
            }
            holder.mTipAmountTextView.setText("$" + String.valueOf(mShifts.get(position).getTipAmount())+ " ");
            holder.mDateTextView.setText(dateTimeFormatter.format(mShifts.get(position).getDate()) + " ");
        } else {
            holder.mDateTextView.setVisibility(View.INVISIBLE);
            holder.mTipAmountTextView.setText("No Shifts Recorded");
        }
    }

    void setShifts(List<Shift> shifts){
        mShifts = shifts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mShifts == null ? 0 : mShifts.size();
    }

    public static class ShiftViewHolder extends RecyclerView.ViewHolder{
        public TextView mDateTextView, mTipAmountTextView;

        public ShiftViewHolder(@NonNull View itemView) {
            super(itemView);

            mDateTextView = itemView.findViewById(R.id.textView_shiftDate);
            mTipAmountTextView = itemView.findViewById(R.id.textView_shiftTipAmount);
        }
    }
}
