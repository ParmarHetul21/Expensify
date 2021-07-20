package com.example.expensify;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CustomViewAdapter extends RecyclerView.Adapter<CustomViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> datelist;
    ArrayList<Integer> amount;
    ArrayList<Integer> imagelist;

    public CustomViewAdapter(Context context, ArrayList<String> datelist, ArrayList<Integer> amount , ArrayList<Integer> imagelist) {
        this.context = context;
        this.amount = amount;
        this.datelist = datelist;
        this.imagelist = imagelist;
    }

    @NonNull
    @Override
    public CustomViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transactions_item,parent,false);
        return new CustomViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewAdapter.MyViewHolder holder, int position) {
        holder.amount.setText(String.valueOf(amount.get(position)));
        holder.imageView.setImageResource((Integer)imagelist.get(position));
        holder.dates.setText(String.valueOf(datelist.get(position)));
    }

    @Override
    public int getItemCount() {
        return datelist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView amount,dates;
        LinearLayout transactionlayout;
        CardView cardView;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            amount =  itemView.findViewById(R.id.tvCustomSpinnerText);
            imageView = itemView.findViewById(R.id.ivCustomSpinnerImage);
            dates = itemView.findViewById(R.id.tvdate);
            transactionlayout =  itemView.findViewById(R.id.tvlayout);
            cardView = itemView.findViewById(R.id.CardData);
        }
    }
}
