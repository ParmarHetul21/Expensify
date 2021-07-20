package com.example.expensify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MonthViewAdapter  extends RecyclerView.Adapter<MonthViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList typelist,amountlist,imagelist;

    public MonthViewAdapter(Context context, ArrayList typelist, ArrayList amountlist,ArrayList imagelist) {
        this.context = context;
        this.typelist = typelist;
        this.amountlist = amountlist;
        this.imagelist = imagelist;
    }
    @NonNull
    @Override
    public MonthViewAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View cardView = layoutInflater.inflate(R.layout.item_card_all_view, parent, false);
        return new MonthViewAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthViewAdapter.ViewHolder holder, int position) {
        holder.catname.setText(String.valueOf(typelist.get(position)));
        holder.amount.setText(String.valueOf(amountlist.get(position)));
        holder.imageView.setImageResource((Integer) imagelist.get(position));

    }

    @Override
    public int getItemCount() {
        return typelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView amount,catname;
        LinearLayout transactionlayout;
        CardView cardView;
        ImageView imageView;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.catimage);
            catname = itemView.findViewById(R.id.tvName);
            amount = itemView.findViewById(R.id.tvAmount);
            cardView = itemView.findViewById(R.id.carddayview);
        }
    }
}
