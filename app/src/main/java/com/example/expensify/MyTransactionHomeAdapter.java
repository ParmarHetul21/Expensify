package com.example.expensify;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

public class MyTransactionHomeAdapter extends RecyclerView.Adapter<MyTransactionHomeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> date,paymentmode,spinnername,note;
    private ArrayList<Integer> amount;
    private ArrayList<String> transaction_id;
    private ArrayList<String> type;
    private ArrayList<Integer> image;

    public MyTransactionHomeAdapter(ArrayList<String> spinnername,ArrayList<String> note,ArrayList<String> transaction_id, ArrayList<String> paymentmode, ArrayList<Integer> amount, ArrayList<String> date, ArrayList<String> type, ArrayList<Integer> image, MainActivity myTransactionHomeAdapter) {
        this.context = myTransactionHomeAdapter;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.image = image;
        this.transaction_id = transaction_id;
        this.paymentmode = paymentmode;
        this.spinnername = spinnername;
        this.note = note;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transactions_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTransactionHomeAdapter.ViewHolder holder, int position) {
        holder.iv.setImageResource(image.get(position));
        holder.tv1.setText(String.valueOf(amount.get(position)));
        holder.tv2.setText(date.get(position));
        String dat1 = type.get(position);
        for (int i = 0; i < type.size(); i++) {
            switch (dat1){
                case "Income":
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#E2F5D0"));
                    break;
                default:
                    holder.cardView.setCardBackgroundColor(Color.WHITE);
                    break;
            }
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,addincome.class);
                intent.putExtra("Id",String.valueOf(transaction_id.get(position)));
                intent.putExtra("Amount",String.valueOf(amount.get(position)));
                intent.putExtra("Dates",String.valueOf(date.get(position)));
                intent.putExtra("Types",String.valueOf(type.get(position)));
                intent.putExtra("Category",String.valueOf(spinnername.get(position)));
                intent.putExtra("Note",String.valueOf(note.get(position)));
                intent.putExtra("PaymentMode",String.valueOf(paymentmode.get(position)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return date.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView tv1,tv2;
        CardView cardView;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.tvlayout);
            iv = itemView.findViewById(R.id.ivCustomSpinnerImage);
            tv1 = itemView.findViewById(R.id.tvCustomSpinnerText);
            tv2 = itemView.findViewById(R.id.tvdate);
            cardView = itemView.findViewById(R.id.CardData);
        }
    }
}
