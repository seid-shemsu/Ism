package com.izhar.ism.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.izhar.ism.R;
import com.izhar.ism.objects.Request;
import com.izhar.ism.objects.User;

import java.util.List;

public class WaiterPerformanceAdapter extends RecyclerView.Adapter<WaiterPerformanceAdapter.Holder> {
    Context context;
    List<User> users;

    public WaiterPerformanceAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.waiter_performance_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        User user = users.get(position);
        holder.name.setText(user.getName());
        holder.pending_text.setText(user.getRequested().size() +" ");
        holder.finished_text.setText(user.getApproved().size() +" ");
        holder.declined_text.setText(user.getDeclined().size() +" ");

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView name;
        TextView pending_text, finished_text, approved_amount, requested_amount, declined_text, declined_amount;
        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            pending_text = itemView.findViewById(R.id.requested);
            finished_text = itemView.findViewById(R.id.finished);
            approved_amount = itemView.findViewById(R.id.approved_amount);
            requested_amount = itemView.findViewById(R.id.requested_amount);
            declined_text = itemView.findViewById(R.id.declined);
            declined_amount = itemView.findViewById(R.id.declined_amount);
        }
    }
}
