package com.izhar.ism.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.izhar.ism.R;
import com.izhar.ism.objects.Food;

import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.Holder> {
    List<Food> foods;
    Context context;

    public FoodListAdapter(List<Food> foods, Context context) {
        this.foods = foods;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_single, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Food food = foods.get(position);
        holder.name.setText(food.getName());
        holder.price.setText(food.getPrice() + " ETB");
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView name, price, amount;
        ImageView plus, minus;
        Button remove;
        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            remove = itemView.findViewById(R.id.remove);
            plus = itemView.findViewById(R.id.img_add);
            minus = itemView.findViewById(R.id.img_minus);
            amount = itemView.findViewById(R.id.amount);
            plus.setVisibility(View.GONE);
            amount.setVisibility(View.GONE);
            minus.setVisibility(View.GONE);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteData(foods.get(getAdapterPosition()).getId());
                    deleteItem(getAdapterPosition());

                }
            });
        }

        private void deleteData(long id) {
            DatabaseReference data = FirebaseDatabase.getInstance().getReference("items").child(id + "");
            data.removeValue();
        }

        private void deleteItem(int adapterPosition) {
            foods.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
            notifyItemRangeChanged(0, foods.size());
        }
    }
}
