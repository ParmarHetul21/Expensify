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

public class MonthIncomeViewAdapter extends RecyclerView.Adapter<MonthIncomeViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList typelist1,amountlist1,imagelist1;

    public MonthIncomeViewAdapter(Context context, ArrayList typelist1, ArrayList amountlist1, ArrayList imagelist1) {
        this.context = context;
        this.typelist1 = typelist1;
        this.amountlist1 = amountlist1;
        this.imagelist1 = imagelist1;
    }
    @NonNull
    @Override
    public MonthIncomeViewAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View cardView = layoutInflater.inflate(R.layout.item_card_all_view, parent, false);
        return new MonthIncomeViewAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull  MonthIncomeViewAdapter.ViewHolder holder, int position) {
        holder.catname.setText(String.valueOf(typelist1.get(position)));
        holder.amount.setText(String.valueOf(amountlist1.get(position)));
        holder.imageView.setImageResource((Integer) imagelist1.get(position));
    }

    @Override
    public int getItemCount() {
        return typelist1.size();
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
