package com.izhar.ism;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
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
import com.izhar.ism.adapters.ApprovalAdapter;
import com.izhar.ism.objects.Food;
import com.izhar.ism.objects.Request;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FoodView extends AppCompatActivity {
    RecyclerView recycle;
    LottieAnimationView loader;
    TextView total;
    String id, type;
    List<Food> foods = new ArrayList<>();
    Request request;
    FoodViewAdapter adapter;
    DatabaseReference data;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_view);
        id = getIntent().getExtras().getString("id");
        type = getIntent().getExtras().getString("type");
        loader = findViewById(R.id.loader);
        total = findViewById(R.id.total_price);
        recycle = findViewById(R.id.recycle);
        recycle.setHasFixedSize(true);
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        recycle.setLayoutManager(new LinearLayoutManager(this));
        getData();
    }

    private void getData() {
        //Toast.makeText(this, type + "\n" + id, Toast.LENGTH_SHORT).show();
        data = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("requested").child(id);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                request = dataSnapshot.getValue(Request.class);
                for (DataSnapshot snapshot : dataSnapshot.child("foods").getChildren()) {
                    foods.add(snapshot.getValue(Food.class));
                }
                total.setText(dataSnapshot.child("total").getValue().toString() + " ETB");
                adapter = new FoodViewAdapter(foods, FoodView.this);
                loader.setVisibility(View.GONE);
                recycle.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}