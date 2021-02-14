package com.izhar.ism.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.izhar.ism.Approval;
import com.izhar.ism.FoodView;
import com.izhar.ism.R;
import com.izhar.ism.objects.Request;

import java.util.List;

public class FinishedAdapter extends RecyclerView.Adapter<FinishedAdapter.Holder> {
    Context context;
    List<Request> requests;
    String type;

    public FinishedAdapter(Context context, List<Request> requests, String type) {
        this.context = context;
        this.requests = requests;
        this.type = type;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Request request = requests.get(position);
        holder.price.setText(request.getTotal() + "\nETB");
        holder.name.setText(request.getName());
        holder.time.setText(request.getDateTime());
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, time, price;
        RelativeLayout relative;
        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            price = itemView.findViewById(R.id.price);
            relative = itemView.findViewById(R.id.relative);

            name.setOnClickListener(this);
            time.setOnClickListener(this);
            price.setOnClickListener(this);
            relative.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String orderId = requests.get(getAdapterPosition()).getId();
            context.startActivity(new Intent(context, FoodView.class)
                    .putExtra("type", type)
                    .putExtra("id", orderId).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }
}
