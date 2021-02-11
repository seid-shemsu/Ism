package com.izhar.ism;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.ism.adapters.OrderAdapter;
import com.izhar.ism.objects.Food;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    List<Food> foods;
    LottieAnimationView loader;
    ImageView fab;
    TextView number;
    RecyclerView recycler;
    OrderAdapter orderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("New Order");
        setContentView(R.layout.activity_order);
        recycler = findViewById(R.id.recycle);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, CartActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("foods", (Serializable) orderAdapter.toCart);
                intent.putExtra("bundle", args);
                startActivity(intent);
                finish();
            }
        });
        number = findViewById(R.id.fab_number);
        loader = findViewById(R.id.loader);
        foods = new ArrayList<>();
        getItems();

    }

    private void getItems() {
        final DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("items");
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String name = snapshot.child("name").getValue().toString();
                    String price = snapshot.child("price").getValue().toString();
                    foods.add(new Food(name, price));
                }
                orderAdapter = new OrderAdapter(foods, OrderActivity.this, number);
                recycler.setAdapter(orderAdapter);
                loader.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
