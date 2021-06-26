package com.izhar.ism.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.izhar.ism.R;
import com.izhar.ism.objects.Food;

import java.util.List;

public class ApprovalAdapter extends RecyclerView.Adapter<ApprovalAdapter.Holder> {
    List<Food> foods;
    Context context;

    public ApprovalAdapter(List<Food> foods, Context context) {
        this.foods = foods;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.approval_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Food food = foods.get(position);
        holder.name.setText(food.getName());
        holder.amount.setText(food.getQuantity());
        int price = Integer.parseInt(food.getPrice());
        int amount = Integer.parseInt(food.getQuantity());
        holder.price.setText((price * amount) + " ETB");
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView name, amount, price;
        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.amount);
            price = itemView.findViewById(R.id.price);
        }
    }
}