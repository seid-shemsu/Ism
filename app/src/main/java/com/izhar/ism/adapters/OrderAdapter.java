package com.izhar.ism.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.izhar.ism.R;
import com.izhar.ism.objects.Food;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Holder>/* implements Filterable*/ {
    List<Food> foods;
    Context context;
    List<Food> allFoods;
    public ArrayList toCart = new ArrayList<>();
    TextView textView;
    public int fabNumber = 0;

    public OrderAdapter(List<Food> foods, Context context, TextView textView) {
        this.foods = foods;
        this.context = context;
        this.textView = textView;
        allFoods = new ArrayList<>(foods);
        for (int i = 0; i < foods.size(); i++)
            toCart.add(i, null);
    }

    public OrderAdapter() {
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_single_item, parent, false);
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

    /*@Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Food> filteredItems = new ArrayList<>();
            if (constraint.toString().isEmpty())
                filteredItems.addAll(allFoods);
            else {
                for (Food item : allFoods) {
                    if (item.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredItems.addAll(allFoods);
                        filteredItems.add(0, item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredItems;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            foods.clear();
            foods.addAll((Collection<? extends Food>) results.values);
            notifyDataSetChanged();
        }
    };
*/

    class Holder extends RecyclerView.ViewHolder {
        TextView name, price;
        Button add;

        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            add = itemView.findViewById(R.id.btn_add);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (add.getText().toString().equalsIgnoreCase("add")) {
                        toCart.add(getAdapterPosition(), new Food(foods.get(getAdapterPosition()).getName(), foods.get(getAdapterPosition()).getPrice()));
                        add.setText("Remove");
                        add.setBackgroundResource(R.drawable.btn_remove);
                        fabNumber++;
                        textView.setText(fabNumber + "");
                    } else {
                        add.setText("Add");
                        add.setBackgroundResource(R.drawable.btn_add);
                        fabNumber--;
                        textView.setText(fabNumber + "");
                        if (fabNumber == 0){
                            toCart.clear();
                            for (int i = 0; i < foods.size(); i++)
                                toCart.add(i, null);
                        }
                        else
                            toCart.remove(getAdapterPosition());
                    }
                }
            });
        }
    }
}