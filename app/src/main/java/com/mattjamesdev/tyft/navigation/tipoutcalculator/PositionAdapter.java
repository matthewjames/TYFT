package com.mattjamesdev.tyft.navigation.tipoutcalculator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mattjamesdev.tyft.R;

import java.util.ArrayList;

public class PositionAdapter extends RecyclerView.Adapter<PositionAdapter.PositionViewHolder> {
    private ArrayList<PositionItem> mPositionItemList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class PositionViewHolder extends RecyclerView.ViewHolder {
        public TextView mTipPercentageTextView,
                mPositionTextView,
                mNameTextView,
                mTipOutTextView;

        public PositionViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);

            mTipPercentageTextView = itemView.findViewById(R.id.textView_tipPerc);
            mPositionTextView = itemView.findViewById(R.id.textView_position);
            mNameTextView = itemView.findViewById(R.id.textView_name);
            mTipOutTextView = itemView.findViewById(R.id.textView_tipOut);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public PositionAdapter(ArrayList<PositionItem> positionItemList){
        mPositionItemList = positionItemList;
    }

    @NonNull
    @Override
    public PositionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_position, parent, false);
        PositionViewHolder positionViewHolder = new PositionViewHolder(v, mListener);
        return positionViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PositionViewHolder holder, int position) {
        // pass values to TextViews in position list items here
        PositionItem currentItem = mPositionItemList.get(position);

        holder.mTipPercentageTextView.setText(String.valueOf(currentItem.getmTipPerc()) + "% ");
        holder.mPositionTextView.setText(currentItem.getmPosition() + " ");

        if(currentItem.getmName().equals("")){
            holder.mNameTextView.setText("");
        } else {
            holder.mNameTextView.setText("(" + currentItem.getmName() + ") ");
        }

        if((Object)currentItem.getmTipOut() != null) {
            holder.mTipOutTextView.setText("$" + String.valueOf(currentItem.getmTipOut())+ " ");
        }
    }

    @Override
    public int getItemCount() {
        return mPositionItemList.size();
    }



}
