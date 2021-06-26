package com.izhar.ism.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.ism.R;
import com.izhar.ism.adapters.ApprovalAdapter;
import com.izhar.ism.adapters.FinishedAdapter;
import com.izhar.ism.adapters.FoodListAdapter;
import com.izhar.ism.adminJob.FoodList;
import com.izhar.ism.objects.Food;
import com.izhar.ism.objects.Request;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SeeMore extends AppCompatActivity {
    LottieAnimationView loader;
    RecyclerView recycler;
    FinishedAdapter adapter;
    ApprovalAdapter adapter1;
    List<Request> requests;
    List<Food> foods;
    TextView empty;
    String type, actor;
    String date;
    LinearLayout linear;
    TextView total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_more);
        linear = findViewById(R.id.linear);
        total = findViewById(R.id.total_price);
        type = getIntent().getExtras().getString("type");
        actor = getIntent().getExtras().getString("actor");
        loader = findViewById(R.id.loader);
        empty = findViewById(R.id.empty);
        recycler = findViewById(R.id.recycle);
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        switch (actor){
            case "admin":
                getAdmin();
                break;
            case "waiter":
                getWaiter();
                break;
            case "cashier":
                getCashier();
                break;
            case "cooker":
                getCooker();
                break;
        }

    }
    private void getFoods() {
        requests = new ArrayList<>();
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("transaction").child(date).child(type);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requests.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    requests.add(snapshot.getValue(Request.class));
                }
                loader.setVisibility(View.GONE);
                if (requests.size() == 0) {
                    empty.setVisibility(View.VISIBLE);
                } else {
                    adapter = new FinishedAdapter(SeeMore.this, requests, type);
                    recycler.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getSold() {
        foods = new ArrayList<>();
        ApprovalAdapter adapter;
        linear.setVisibility(View.VISIBLE);

        DatabaseReference data = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("sold");
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foods.clear();
                int total_price = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    foods.add(snapshot.getValue(Food.class));
                    total_price += Integer.parseInt(snapshot.child("price").getValue().toString());
                }
                total.setText(total_price + " ETB");
                loader.setVisibility(View.GONE);
                if (foods.size() == 0) {
                    empty.setVisibility(View.VISIBLE);
                } else {
                    adapter1 = new ApprovalAdapter(foods, SeeMore.this);
                    recycler.setAdapter(adapter1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAdmin() {
        switch (type){
            case "sold":
                getSold();
                break;
            default:
                getFoods();
                break;
        }
    }

    private void getWaiter() {
        requests = new ArrayList<>();
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("waiter").child(type).child(getSharedPreferences("user", MODE_PRIVATE).getString("name", ""));
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requests.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    requests.add(snapshot.getValue(Request.class));
                }
                loader.setVisibility(View.GONE);
                adapter = new FinishedAdapter(SeeMore.this, requests, type);
                recycler.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getCashier() {
        requests = new ArrayList<>();
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cashier").child(type);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requests.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    requests.add(snapshot.getValue(Request.class));
                }
                loader.setVisibility(View.GONE);
                adapter = new FinishedAdapter(SeeMore.this, requests, type);
                recycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getCooker() {
        requests = new ArrayList<>();
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cooker").child(type);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requests.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    requests.add(snapshot.getValue(Request.class));
                }
                loader.setVisibility(View.GONE);
                adapter = new FinishedAdapter(SeeMore.this, requests, type);
                recycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}