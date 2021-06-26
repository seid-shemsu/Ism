package com.izhar.ism;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.ism.adapters.WaiterPerformanceAdapter;
import com.izhar.ism.objects.Request;
import com.izhar.ism.objects.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WaiterPerformance extends AppCompatActivity {
    RecyclerView recycle;
    TextView not_found;
    LottieAnimationView loader;
    List<String> names;
    List<User> users;
    WaiterPerformanceAdapter adapter;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_performance);
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        recycle = findViewById(R.id.recycle);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setHasFixedSize(true);
        not_found = findViewById(R.id.not_found);
        loader = findViewById(R.id.loader);
        users = new ArrayList<>();
        names = new ArrayList<>();
        getNames();
    }
    private void getNames() {

        DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("users");
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("type").getValue().toString().equalsIgnoreCase("waiter")) {
                        names.add(snapshot.child("name").getValue().toString());
                        getData(snapshot.child("name").getValue().toString());
                    }
                }
                //getData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getData(String name) {
        final int[] total_requested = new int[1];
        final int[] total_approved = new int[1];
        final int[] total_declined = new int[1];
        final List<Request>[] requested = new List[]{new ArrayList<>()};
        DatabaseReference request = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("waiter").child("requested").child(name);
        request.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total_requested[0] = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    requested[0].add(snapshot.getValue(Request.class));
                    total_requested[0] += Integer.parseInt(snapshot.child("total").getValue().toString());
                }
                List<Request> declined = new ArrayList<>();
                DatabaseReference decline = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("waiter").child("declined").child(name);
                decline.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        total_declined[0] = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            declined.add(snapshot.getValue(Request.class));
                            total_declined[0] += Integer.parseInt(snapshot.child("total").getValue().toString());
                        }
                        List<Request> approved = new ArrayList<>();
                        DatabaseReference approve = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("waiter").child("approved").child(name);
                        approve.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                total_approved[0] = 0;
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    approved.add(snapshot.getValue(Request.class));
                                    total_approved[0] += Integer.parseInt(snapshot.child("total").getValue().toString());
                                }
                                users.add(new User(name, requested[0], approved, declined, total_requested[0] + "", total_approved[0] + "", total_declined[0] + ""));
                                if (users.size() == 0)
                                    not_found.setVisibility(View.VISIBLE);
                                loader.setVisibility(View.GONE);
                                adapter = new WaiterPerformanceAdapter(WaiterPerformance.this, users);
                                recycle.setAdapter(adapter);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}