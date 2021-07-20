package com.example.expensify;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;


public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.MyViewHolder> {

    private  Context context;
    private int pbamount=0;
    int famount,ffamount;
    private ArrayList budgetid,date,amount,total_Amt;

    public BudgetAdapter(Context context, ArrayList budgetid, ArrayList date, ArrayList amount, ArrayList total_Amt) {
        this.context = context;
        this.budgetid = budgetid;
        this.date = date;
        this.amount = amount;
        this.total_Amt = total_Amt;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view= inflater.inflate(R.layout.rv_budget,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.date.setText(String.valueOf(date.get(position)));
        holder.amount.setText(String.valueOf(amount.get(position)));
        holder.total_amount.setText(String.valueOf(total_Amt.get(position)));
        for(int iu = 0; iu < amount.size(); iu++){
            famount= Integer.parseInt((String) amount.get(iu));
        }
        for(int iu = 0; iu < total_Amt.size(); iu++){
            ffamount= (int) total_Amt.get(iu);
        }
        int pbamount=ffamount-famount;
        holder.pb.setMax(ffamount);
        holder.pb.setProgress(pbamount);
        holder.budget_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,AddBudget.class);
                i.putExtra("Id",String.valueOf(budgetid.get(position)));
                i.putExtra("date",String.valueOf(date.get(position)));
                i.putExtra("amount",String.valueOf(amount.get(position)));
                i.putExtra("total_amount",String.valueOf(total_Amt.get(position)));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return date.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date,amount,total_amount;
        ProgressBar pb;
        ConstraintLayout budget_cardview;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            amount=itemView.findViewById(R.id.amount);
            total_amount = itemView.findViewById(R.id.total_amount);
            budget_cardview=itemView.findViewById(R.id.budget_cardview);
            pb=itemView.findViewById(R.id.progressBar);
        }
    }
}
