package com.izhar.ism;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.ism.adapters.CartAdapter;
import com.izhar.ism.cashier.Cashier;
import com.izhar.ism.objects.Activity;
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
    Button order, makeOrder, cancel;
    RecyclerView recycle;
    CartAdapter cartAdapter;
    ArrayList<Integer> quantities;
    LottieAnimationView loader;
    String date;
    AutoCompleteTextView spinner;
    ArrayList<String> names, pins;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Cart");
        setContentView(R.layout.activity_cart);
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
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
                Dialog dialog = new Dialog(CartActivity.this);
                dialog.setCancelable(false);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.waiter_selection);
                spinner = dialog.findViewById(R.id.spinner);
                dialog.show();
                setSpinner();
                TextInputEditText pin = dialog.findViewById(R.id.pin_code);
                makeOrder = dialog.findViewById(R.id.order);
                cancel = dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                makeOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (check()){
                            makeOrder.setVisibility(View.INVISIBLE);
                            if (pin.getText().toString().equalsIgnoreCase(pins.get(names.indexOf(spinner.getText().toString())))){
                                order(spinner.getText().toString());
                                dialog.dismiss();
                            }
                            else {
                                pin.setError("incorrect pin number");
                                pin.setText("");
                                makeOrder.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    private boolean check() {
                        if (pin.getText().toString().length() < 4){
                            pin.setError("short pin length");
                            return false;
                        }
                        else if (spinner.getText().toString().length() == 0){
                            Toast.makeText(CartActivity.this, "select waiter", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        return true;
                    }
                });
            }

            private void setSpinner() {
                DatabaseReference waiter = FirebaseDatabase.getInstance().getReference("waiter");
                names = new ArrayList<>();
                pins = new ArrayList<>();
                waiter.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            names.add(snapshot.child("name").getValue().toString());
                            pins.add(snapshot.child("pin").getValue().toString());
                        }
                        //names.add(0, "select waiter");
                        String[] s = names.toArray(new String[0]);
                        ArrayAdapter aa = new ArrayAdapter(CartActivity.this, R.layout.list_item, s);
                        //aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        spinner.setAdapter(aa);
                        makeOrder.setVisibility(View.VISIBLE);
                        cancel.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
    private void order(String name) {
        loader.setVisibility(View.VISIBLE);
        order.setEnabled(false);
        recycle.setEnabled(false);
        quantities = cartAdapter.quantities;
        quantities.removeAll(Arrays.asList(0, null));
        List<Food> foodList = new ArrayList<>();
        try {
            for (int i = 0; i < foods.size(); i++) {
                foodList.add(new Food(foods.get(i).getName(), foods.get(i).getPrice(), Integer.toString(quantities.get(i))));
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
        //String name = user.getString("name", "username");
        SimpleDateFormat sdp = new SimpleDateFormat("hh:mm");
        String id = System.currentTimeMillis() + "";
        //add to total requested orders
        Request request = new Request(foodList, id, cartAdapter.getTotal() + "", sdp.format(new Date()), name);
        DatabaseReference ordered = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("requested");
        ordered.child(id).setValue(request);
        //add to cashier total requested orders
        DatabaseReference c_ordered = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cashier").child("requested");
        c_ordered.child(id).setValue(request);
        //add to individual requested
        ordered = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("waiter").child("requested").child(name);
        ordered.child(id).setValue(request);
        //add to individual pending order
        ordered = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("waiter").child("pending").child(name);
        ordered.child(id).setValue(request);
        //add to cooker requested
        final DatabaseReference pass = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cooker").child("request").child(id);
        DatabaseReference cooker_requested = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cooker").child("requested").child(id);
        pass.setValue(request);
        cooker_requested.setValue(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(CartActivity.this, Cashier.class));
                finish();
                finish();
            }
        });
        //send a request to cashier
        DatabaseReference pending = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cashier").child("pending");
        pending.child(id).setValue(new Request(foodList, id, cartAdapter.getTotal() + "", sdp.format(new Date()), name));

        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String time = new SimpleDateFormat("hh:mm").format(new Date());
        DatabaseReference activity = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("activity");
        String activity_id = System.currentTimeMillis() + "";
        activity.child(activity_id).setValue(new Activity("cashier", date, activity_id, getSharedPreferences("user", MODE_PRIVATE).getString("name", "unknown"), time, "request", id));
    }
}
