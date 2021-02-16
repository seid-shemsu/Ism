package com.izhar.ism;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_performance);
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
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");
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

        List<Request> requested = new ArrayList<>();
        DatabaseReference request = FirebaseDatabase.getInstance().getReference("waiter").child("requested").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(name);
        request.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    requested.add(snapshot.getValue(Request.class));
                List<Request> declined = new ArrayList<>();
                DatabaseReference decline = FirebaseDatabase.getInstance().getReference("waiter").child("declined").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(name);
                decline.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            declined.add(snapshot.getValue(Request.class));
                        List<Request> approved = new ArrayList<>();
                        DatabaseReference approve = FirebaseDatabase.getInstance().getReference("waiter").child("approved").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(name);
                        approve.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                    approved.add(snapshot.getValue(Request.class));
                                users.add(new User(name, requested, approved, declined));
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

    private void set() {
        if (users.size() == 0)
            not_found.setVisibility(View.VISIBLE);
        loader.setVisibility(View.GONE);
        adapter = new WaiterPerformanceAdapter(this, users);
        recycle.setAdapter(adapter);
    }

    private void getDeclined(String name) {
        //Toast.makeText(this, declined.size() + "", Toast.LENGTH_SHORT).show();

    }

    private void getApproved(String name) {

    }

    private void getRequested(String name) {

    }
}