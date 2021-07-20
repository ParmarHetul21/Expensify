package com.example.expensify;


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

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.MyViewHolder> {

    private Context context;
    private ArrayList  categoryid,categorynm,categorytype,categorydesc,categoryimage;
    private ArrayList<Integer> catimage;
    CategoryItemAdapter(Context context,ArrayList categoryid,ArrayList categorynm,ArrayList categorytype,ArrayList categorydesc,ArrayList catimage)
    {
            this.context = context;
            this.categoryid =categoryid;
            this.categorynm =categorynm;
            this.categorytype =categorytype;
            this.categorydesc =categorydesc;
            this.catimage=catimage;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.showcategory_items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.categorynm.setText(String.valueOf(categorynm.get(position)));
//        holder.rb.setText(String.valueOf(categorytype.get(position)));
        holder.categorydesc.setText(String.valueOf(categorydesc.get(position)));
        holder.imageView.setImageResource(Integer.parseInt(String.valueOf(catimage.get(position))));
        String dat1 = String.valueOf(categorytype.get(position));
        for (int i = 0; i < categorytype.size(); i++) {
            switch (dat1){
                case "Income":
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#E2F5D0"));
                    break;
                default:
                    holder.cardView.setCardBackgroundColor(Color.WHITE);
                    break;
            }
        }
        holder.categorylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AddCategory.class);
                intent.putExtra("Id",String.valueOf(categoryid.get(position)));
                intent.putExtra("Name",String.valueOf(categorynm.get(position)));
                intent.putExtra("Type",String.valueOf(categorytype.get(position)));
                intent.putExtra("Note",String.valueOf(categorydesc.get(position)));
                intent.putExtra("Image",catimage.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {        return categorynm.size();    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView categorynm,categorydesc;
        ImageView imageView;
        CardView cardView;
//        RadioGroup categorytype;
//        RadioButton rb;
        LinearLayout categorylayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categorynm = itemView.findViewById(R.id.tvshowcategorytext);
//              categorytype = itemView.findViewById(R.id.catgroup);
//              rb = categorytype.check(R.id.rbexpense);
//            rb = itemView.findViewById(R.id.rbexpense);
            categorydesc = itemView.findViewById(R.id.tvcategorydesctext);
            imageView = itemView.findViewById(R.id.catIcon);
            categorylayout = itemView.findViewById(R.id.categoryLayout);
            cardView = itemView.findViewById(R.id.categoryid);
        }
    }
}
