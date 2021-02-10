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

import com.izhar.ism.R;
import com.izhar.ism.objects.Food;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Holder> {
    List<Food> foods;
    Context context;
    public int total = 0;
    public ArrayList<Integer> quantities = new ArrayList<>();
    public CartAdapter(List<Food> foods, Context context) {
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

        total += Integer.parseInt(food.getPrice());

    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView name, price, amount;
        ImageView plus, minus;
        Button remove;

        int i = 1, once = 0, single = 0;
        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            remove = itemView.findViewById(R.id.remove);
            plus = itemView.findViewById(R.id.img_add);
            minus = itemView.findViewById(R.id.img_minus);
            amount = itemView.findViewById(R.id.amount);
            amount.setText(i + "");
            quantities.add(1);
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i++;
                    amount.setText(i + "");
                    if (i == 2 && once == 0){
                        single = Integer.parseInt(price.getText().toString().substring(0, price.getText().toString().indexOf(" ")));
                        once = 1;
                    }
                    price.setText(Integer.toString(i * single) + " ETB");
                    total += single;
                    quantities.set(getAdapterPosition(), i);
                }
            });
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (i > 1) {
                        i--;
                        amount.setText(i + "");

                        if (i == 2 && once == 0){
                            single = Integer.parseInt(price.getText().toString().substring(0, price.getText().toString().indexOf(" ")));
                            once = 1;
                        }
                        price.setText(Integer.toString(i * single) + " ETB");
                        total -= single;
                        quantities.set(getAdapterPosition(), i);
                    }
                }
            });
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantities.set(getAdapterPosition(), 0);
                    deleteItem(getAdapterPosition());
                }
            });
        }
        private void deleteItem(int adapterPosition) {
            foods.remove(adapterPosition);
            total = 0;
            notifyItemRemoved(adapterPosition);
            notifyItemRangeChanged(0, foods.size());
        }

    }

    public int getTotal(){
        return total;
    }

}
