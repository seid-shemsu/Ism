package com.izhar.ism;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.izhar.ism.adapters.CartAdapter;
import com.izhar.ism.objects.Food;
import com.izhar.ism.objects.Request;
import com.izhar.ism.waiter.Waiter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    List<Food> foods;
    Button order;
    RecyclerView recycle;
    CartAdapter cartAdapter;
    ArrayList<Integer> quantities;
    LottieAnimationView loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Cart");
        setContentView(R.layout.activity_cart);
        order = findViewById(R.id.btn_order);
        loader = findViewById(R.id.loader);
        recycle = findViewById(R.id.recycle);
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("bundle");
        foods = (ArrayList<Food>) args.getSerializable("foods");
        foods.removeAll(Arrays.asList("", null));
        cartAdapter = new CartAdapter(foods, this);
        recycle.setAdapter(cartAdapter);
        if (foods.size() == 0)
            order.setVisibility(View.GONE);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.setVisibility(View.VISIBLE);
                order.setEnabled(false);
                recycle.setEnabled(false);
                quantities = cartAdapter.quantities;
                quantities.removeAll(Arrays.asList(0, null));
                List<Food> foodList = new ArrayList<>();
                for (int i = 0; i < foods.size(); i++) {
                    foodList.add(new Food(foods.get(i).getName(), foods.get(i).getPrice(), Integer.toString(quantities.get(i))));
                }
                SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
                String name = user.getString("name", "username");
                SimpleDateFormat sdp = new SimpleDateFormat("hh:mm");
                String id = System.currentTimeMillis() + "";
                //add to total requested orders
                DatabaseReference ordered = FirebaseDatabase.getInstance().getReference().child("requested").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + "");
                ordered.child(id).setValue(new Request(foodList, cartAdapter.getTotal() + "", sdp.format(new Date()), name));
                //add to cashier total requested orders
                DatabaseReference c_ordered = FirebaseDatabase.getInstance().getReference("cashier").child("requested").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + "");
                c_ordered.child(id).setValue(new Request(foodList, cartAdapter.getTotal() + "", sdp.format(new Date()), name));
                //add to individual requested
                ordered = FirebaseDatabase.getInstance().getReference("waiter").child("requested").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + "").child(name);
                ordered.child(id).setValue(new Request(foodList, cartAdapter.getTotal() + "", sdp.format(new Date()), name));
                //add to individual pending order
                ordered = FirebaseDatabase.getInstance().getReference("waiter").child("pending").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + "").child(name);
                ordered.child(id).setValue(new Request(foodList, cartAdapter.getTotal() + "", sdp.format(new Date()), name));
                //send a request to cashier
                DatabaseReference pending = FirebaseDatabase.getInstance().getReference().child("cashier").child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + "");
                pending.child(id).setValue(new Request(foodList, cartAdapter.getTotal() + "", sdp.format(new Date()), name)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(CartActivity.this, Waiter.class));
                        finish();
                    }
                });
            }

        });

    }
}
