package com.izhar.ism.fragments;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.ism.R;
import com.izhar.ism.adapters.PendingAdapter;
import com.izhar.ism.objects.Request;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingTab extends Fragment {

    public PendingTab() {
        // Required empty public constructor
    }
    String user;
    public PendingTab(String user){
        this.user = user;
    }
    List<Request> requests = new ArrayList<>();
    PendingAdapter pendingAdapter;
    RecyclerView recycle;
    TextView not_found;
    LottieAnimationView loader;
    SharedPreferences pending;
    int item;
    String date;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pending_tab, container, false);
        pending = getContext().getSharedPreferences("pending", Context.MODE_PRIVATE);
        item = pending.getInt("pending", 0);
        recycle = view.findViewById(R.id.recycle);
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        recycle.setLayoutManager(new LinearLayoutManager(getContext()));
        recycle.setHasFixedSize(true);
        not_found = view.findViewById(R.id.not_found);
        loader = view.findViewById(R.id.loader);
        getData();
        return view;
    }

    String notifyName;
    DatabaseReference data;
    private void getData() {
        SharedPreferences u_name = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String name = u_name.getString("name", "default");
        String user = getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("user", "");
        if (user.equalsIgnoreCase("waiter")){
            data = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("waiter").child("pending").child(name);
        }
        else if (user.equalsIgnoreCase("cashier")){
            data = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cashier").child("pending");
        }
        else{
            data = FirebaseDatabase.getInstance().getReference("transaction").child(date).child(user).child("request");
        }
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requests.clear();
                not_found.setVisibility(View.GONE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String id = snapshot.getKey();
                    String time = snapshot.child("dateTime").getValue().toString();
                    String price = snapshot.child("total").getValue().toString() + "\nETB";
                    String name = snapshot.child("name").getValue().toString();
                    notifyName = name;
                    requests.add(new Request(id, price, time, name));
                }
                if (requests.size()==0){
                    not_found.setVisibility(View.VISIBLE);
                }
                else {
                    pendingAdapter = new PendingAdapter(getContext(), requests);
                    recycle.setAdapter(pendingAdapter);
                    if (!user.equalsIgnoreCase("waiter") && item != requests.size())
                        playNotificationSound();
                }
                loader.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    Uri notify;
    public void playNotificationSound() {
        try {
            notify = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.notification);
            Ringtone r = RingtoneManager.getRingtone(getContext(), notify);
            r.play();
            showNotification("example", "body");
            pending.edit().putInt("pending", requests.size()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showNotification(String title, String body) {
        NotificationCompat.Builder notify = new NotificationCompat.Builder(getContext());
        notify.setSmallIcon(R.drawable.append)
                .setContentTitle("New Order")
                .setContentText("New Order is arrived");
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(12, notify.build());

    }
}
