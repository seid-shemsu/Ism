package com.izhar.ism.fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
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
import com.izhar.ism.adapters.FinishedAdapter;
import com.izhar.ism.objects.Request;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinishedTab extends Fragment {

    public FinishedTab() {
        // Required empty public constructor
    }
    String user;
    public FinishedTab(String user){
        this.user = user;
    }
    List<Request> requests = new ArrayList<>();
    FinishedAdapter finishedAdapter;
    RecyclerView recycle;
    TextView not_found;
    LottieAnimationView loader;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_finished_tab, container, false);
        recycle = view.findViewById(R.id.recycle);
        recycle.setLayoutManager(new LinearLayoutManager(getContext()));
        recycle.setHasFixedSize(true);
        not_found = view.findViewById(R.id.not_found);
        loader = view.findViewById(R.id.loader);
        getData();
        return view;
    }
    DatabaseReference data;
    private void getData() {
        SharedPreferences u_name = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String name = u_name.getString("name", "default");
        if (user.equalsIgnoreCase("waiter")){
            data = FirebaseDatabase.getInstance().getReference().child("waiter").child("approved").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(name);
        }
        else{
            data = FirebaseDatabase.getInstance().getReference().child(user).child("approved").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        }
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requests.clear();not_found.setVisibility(View.GONE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String id = snapshot.getKey();
                    String time = snapshot.child("dateTime").getValue().toString();
                    String price = snapshot.child("total").getValue().toString();
                    String name = snapshot.child("name").getValue().toString();
                    requests.add(new Request(id, price, time, name));
                }
                if (requests.size()==0){
                    not_found.setVisibility(View.VISIBLE);
                }
                else {
                    finishedAdapter = new FinishedAdapter(getContext(), requests, "requested");
                    recycle.setAdapter(finishedAdapter);
                    if (user.equalsIgnoreCase("waiter"))
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showNotification(String title, String body) {
        NotificationCompat.Builder notify = new NotificationCompat.Builder(getContext());
        notify.setSmallIcon(R.drawable.check)
                .setContentTitle("New Approval")
                .setContentText("Your order is waiting for you");
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(12, notify.build());

    }
}
