package com.izhar.ism;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
import com.izhar.ism.adapters.UserActivityAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    LottieAnimationView loader;
    RecyclerView recycle;
    TextView no;
    List<com.izhar.ism.objects.Activity> activities;
    UserActivityAdapter adapter;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("user Activities");
        setContentView(R.layout.activity_user);
        loader = findViewById(R.id.loader);
        recycle = findViewById(R.id.recycle);
        no = findViewById(R.id.not_found);
        activities = new ArrayList<>();
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference activity = FirebaseDatabase.getInstance().getReference(date).child("activity");
        activity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        activities.add(snapshot.getValue(com.izhar.ism.objects.Activity.class));
                    }
                }
                if (activities.size() == 0)
                    no.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
                adapter = new UserActivityAdapter(UserActivity.this, activities);
                recycle.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}