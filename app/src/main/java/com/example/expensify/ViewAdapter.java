package com.example.expensify;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewAdapter  extends RecyclerView.Adapter<ViewAdapter.ViewHolder>{

    private Context context;
    private ArrayList  transactionid,dates,amount,typeoftransaction,spinnername,note,paymentmode,image;

    public ViewAdapter(Context context, ArrayList transactionid, ArrayList dates, ArrayList amount, ArrayList typeoftransaction, ArrayList spinnername, ArrayList note, ArrayList paymentmode, ArrayList image) {
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

    @NonNull
    @Override
    public ViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View cardView = layoutInflater.inflate(R.layout.item_card_all_view, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageResource((Integer) image.get(position));
        holder.catname.setText(String.valueOf(spinnername.get(position)));
        holder.amount.setText(String.valueOf(amount.get(position)));
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
    }

    @Override
    public int getItemCount() {
        return transactionid.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView amount,catname;
        LinearLayout transactionlayout;
        CardView cardView;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.catimage);
            catname = itemView.findViewById(R.id.tvName);
            amount = itemView.findViewById(R.id.tvAmount);
            cardView = itemView.findViewById(R.id.carddayview);
        }
    }
}
