package com.izhar.ism.adminJob;

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
import com.izhar.ism.R;
import com.izhar.ism.adapters.FoodListAdapter;
import com.izhar.ism.objects.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {
    LottieAnimationView loader;
    RecyclerView recycler;
    FoodListAdapter fla;
    List<Food> foods;
    TextView empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        setTitle("Food List");
        loader = findViewById(R.id.loader);
        empty = findViewById(R.id.empty);
        recycler = findViewById(R.id.recycle);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        getFoods();
    }

    private void getFoods() {
        foods = new ArrayList<>();
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("items");
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foods.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    foods.add(snapshot.getValue(Food.class));
                }
                loader.setVisibility(View.GONE);
                if (foods.size()==0){
                    empty.setVisibility(View.VISIBLE);
                }
                else {
                    fla = new FoodListAdapter(foods, FoodList.this);
                    recycler.setAdapter(fla);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}