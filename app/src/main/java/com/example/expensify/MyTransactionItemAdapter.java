package com.example.expensify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyTransactionItemAdapter extends RecyclerView.Adapter<MyTransactionItemAdapter.ViewHolder> {
    private Context context;
    private ArrayList  dates,amount,typeoftransaction,spinnername,note,paymentmode,image;
    private ArrayList<Integer> transactionid;

    public MyTransactionItemAdapter(Context context, ArrayList<Integer> transactionid, ArrayList dates, ArrayList amount, ArrayList typeoftransaction ,ArrayList spinnername,ArrayList note,ArrayList paymentmode,ArrayList image) {
        this.context = context;
        this.transactionid = transactionid;
        this.dates = dates;
        this.amount = amount;
        this.typeoftransaction = typeoftransaction;
        this.spinnername = spinnername;
        this.note = note;
        this.paymentmode = paymentmode;
        this.image = image;
    }
    public void setCards(ArrayList<Integer> transactionid, ArrayList dates, ArrayList amount, ArrayList typeoftransaction ,ArrayList spinnername,ArrayList note,ArrayList paymentmode,ArrayList image)
    {
        this.transactionid = transactionid;
        this.dates = dates;
        this.amount = amount;
        this.typeoftransaction = typeoftransaction;
        this.spinnername = spinnername;
        this.note = note;
        this.paymentmode = paymentmode;
        this.image = image;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transactions_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTransactionItemAdapter.ViewHolder holder, int position) {
        holder.amount.setText(String.valueOf(amount.get(position)));
        holder.imageView.setImageResource((Integer) image.get(position));
        holder.dates.setText(String.valueOf(dates.get(position)));
        holder.itemView.setTag(transactionid.get(position));
        String dat1 = String.valueOf(typeoftransaction.get(position));
        for (int i = 0; i < typeoftransaction.size(); i++) {
            switch (dat1){
                case "Income":
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#E2F5D0"));
                    break;
                default:
                    holder.cardView.setCardBackgroundColor(Color.WHITE);
                    break;
            }
        }

        holder.transactionlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,addincome.class);
                intent.putExtra("Id",String.valueOf(transactionid.get(position)));
                intent.putExtra("Amount",String.valueOf(amount.get(position)));
                intent.putExtra("Dates",String.valueOf(dates.get(position)));
                intent.putExtra("Types",String.valueOf(typeoftransaction.get(position)));
                intent.putExtra("Category",String.valueOf(spinnername.get(position)));
                intent.putExtra("Note",String.valueOf(note.get(position)));
                intent.putExtra("PaymentMode",String.valueOf(paymentmode.get(position)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionid.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView amount,dates;
        LinearLayout transactionlayout;
        CardView cardView;
        ImageView imageView;
        public ViewHolder(View view) {
            super(view);
            amount = view.findViewById(R.id.tvCustomSpinnerText);
            imageView = view.findViewById(R.id.ivCustomSpinnerImage);
            dates = view.findViewById(R.id.tvdate);
            transactionlayout =  view.findViewById(R.id.tvlayout);
            cardView = itemView.findViewById(R.id.CardData);
        }
    }
}
