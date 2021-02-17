package com.izhar.ism.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.izhar.ism.FoodView;
import com.izhar.ism.R;
import com.izhar.ism.objects.Activity;

import java.util.List;

public class UserActivityAdapter extends RecyclerView.Adapter<UserActivityAdapter.Holder> {
    Context context;
    List<Activity> activities;

    public UserActivityAdapter(Context context, List<Activity> activities) {
        this.context = context;
        this.activities = activities;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_activity_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Activity activity = activities.get(position);
        holder.date.setText(activity.getDate());
        holder.time.setText(activity.getTime());
        holder.name.setText(activity.getName());
        holder.type.setText(activity.getType());
        holder.actor.setText(activity.getActor());
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date, time, name, type, actor;
        RelativeLayout relative;
        public Holder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            actor = itemView.findViewById(R.id.actor);
            relative = itemView.findViewById(R.id.relative);
            relative.setOnClickListener(this);
            date.setOnClickListener(this);
            time.setOnClickListener(this);
            name.setOnClickListener(this);
            type.setOnClickListener(this);
            actor.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String type = activities.get(getAdapterPosition()).getType();
            if (type.equalsIgnoreCase("decline") || type.equalsIgnoreCase("request") || type.equalsIgnoreCase("approval")){
                String orderId = activities.get(getAdapterPosition()).getFood_id();
                context.startActivity(new Intent(context, FoodView.class)
                        .putExtra("type", "requested")
                        .putExtra("id", orderId).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }
    }
}
