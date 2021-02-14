package com.izhar.ism.adminJob;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.izhar.ism.R;
import com.izhar.ism.objects.Food;

import java.util.List;

public class FLA extends RecyclerView.Adapter<FLA.Holder> {
    List<Food> foods;
    Context context;

    public FLA(List<Food> foods, Context context) {
        this.foods = foods;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_list_item, parent, false);
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
        TextView name, price;
        ImageButton remove, edit;
        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            edit = itemView.findViewById(R.id.btn_edit);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.new_price);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    EditText name = dialog.findViewById(R.id.name);
                    EditText price = dialog.findViewById(R.id.price);
                    Button update = dialog.findViewById(R.id.btn_update);
                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!price.getText().toString().isEmpty()){
                                updatePrice(foods.get(getAdapterPosition()).getId(), name.getText().toString(), price.getText().toString(), dialog);
                            }

                        }
                    });
                }
            });
            remove = itemView.findViewById(R.id.btn_remove);
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

    private void updatePrice(long id, String name, String price, Dialog dialog) {
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("items").child(id+"");
        data.child("name").setValue(name);
        data.child("price").setValue(price).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "price updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }
}