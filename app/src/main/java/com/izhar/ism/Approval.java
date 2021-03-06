package com.izhar.ism;

import android.app.Notification;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.ism.adapters.ApprovalAdapter;
import com.izhar.ism.cashier.Cashier;
import com.izhar.ism.cooker.Cooker;
import com.izhar.ism.objects.Activity;
import com.izhar.ism.objects.Food;
import com.izhar.ism.objects.Request;
import com.izhar.ism.waiter.Waiter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Approval extends AppCompatActivity {

    String date;
    RecyclerView recycle;
    Button approve, decline;
    LottieAnimationView loader;
    TextView total;
    List<Food> foods = new ArrayList<>();
    List<Food> soldFoods = new ArrayList<>();
    String id, type;
    SharedPreferences user;
    ApprovalAdapter adapter;
    Request request;
    String waiter_name;
    DatabaseReference waiter_request, cashier_request, declined, cooker_request, waiter_approved, waiter_declined, cashier_declined, cashier_requested, cooker_requested, cooker_declined, cashier_approved;
    int old_amount, new_amount, old_price, new_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("approval");
        setContentView(R.layout.activity_approval);
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        id = getIntent().getExtras().getString("orderId");
        type = getIntent().getExtras().getString("type");
        user = getSharedPreferences("user", MODE_PRIVATE);
        approve = findViewById(R.id.approve);
        decline = findViewById(R.id.decline);
        loader = findViewById(R.id.loader);
        total = findViewById(R.id.total_price);
        recycle = findViewById(R.id.recycle);
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        if (user.getString("user", "admin").equalsIgnoreCase("waiter") || user.getString("user", "admin").equalsIgnoreCase("cashier")) {
            approve.setVisibility(View.GONE);
            decline.setText("cancel");
        }
        if (type.equalsIgnoreCase("finished")){
            approve.setVisibility(View.GONE);
            decline.setVisibility(View.GONE);
        }
        getData();
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getString("user", "").equalsIgnoreCase("waiter")) {
                    SharedPreferences u_name = getSharedPreferences("user", MODE_PRIVATE);
                    String name = u_name.getString("name", "default");
                    //add to user activities
                    String time = new SimpleDateFormat("hh:mm").format(new Date());
                    DatabaseReference activity = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("activity");
                    String activity_id = System.currentTimeMillis() + "";
                    activity.child(activity_id).setValue(new Activity("waiter", date, activity_id, name, time, "decline", id));
                    //end of user activity
                    cooker_requested = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cooker").child("requested").child(id);
                    cooker_request = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cooker").child("request").child(id);
                    cashier_approved = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cashier").child("approved").child(id);
                    cashier_request = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cashier").child("pending").child(id);
                    cashier_requested = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cashier").child("requested").child(id);
                    waiter_request = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("waiter").child("pending").child(name).child(id);
                    declined = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("declined").child(id);
                    waiter_declined = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("waiter").child("declined").child(name).child(id);
                    waiter_request.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            declined.setValue(dataSnapshot.getValue());
                            waiter_declined.setValue(dataSnapshot.getValue());
                            cooker_request.removeValue();
                            cooker_requested.removeValue();
                            cashier_request.removeValue();
                            cashier_requested.removeValue();
                            waiter_request.removeValue();
                            cashier_approved.removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }

                else if (user.getString("user", "").equalsIgnoreCase("cashier")){
                    //add to user activity
                    String date  = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                    String time = new SimpleDateFormat("hh:mm").format(new Date());
                    DatabaseReference activity = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("activity");
                    String activity_id = System.currentTimeMillis() + "";
                    activity.child(activity_id).setValue(new Activity("cashier", date, activity_id, getSharedPreferences("user", MODE_PRIVATE).getString("name", "unknown"), time, "decline", id));
                    //end of user activity
                    cashier_request = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cashier").child("pending").child(id);
                    cashier_declined = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cashier").child("declined").child(id);
                    declined = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("declined").child(id);
                    cooker_requested = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cooker").child("requested").child(id);
                    cooker_request = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cooker").child("request").child(id);
                    cashier_request.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            waiter_name = dataSnapshot.child("name").getValue().toString();
                            waiter_request = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("waiter").child("pending").child(waiter_name).child(id);
                            waiter_declined = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("waiter").child("declined").child(waiter_name).child(id);
                            waiter_request.removeValue();
                            waiter_declined.setValue(dataSnapshot.getValue());
                            declined.setValue(dataSnapshot.getValue());
                            cashier_declined.setValue(dataSnapshot.getValue());
                            cashier_request.removeValue();
                            cooker_request.removeValue();
                            cooker_requested.removeValue();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                else {
                    //add to user activity
                    String date  = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                    String time = new SimpleDateFormat("hh:mm").format(new Date());
                    DatabaseReference activity = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("activity");
                    String activity_id = System.currentTimeMillis() + "";
                    activity.child(activity_id).setValue(new Activity("cooker", date, activity_id, getSharedPreferences("user", MODE_PRIVATE).getString("name", "unknown"), time, "decline", id));
                    //end of user activity
                    final DatabaseReference req_ca = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cashier").child("pending").child(id);
                    cooker_request = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cooker").child("request").child(id);
                    cooker_request.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            waiter_name = dataSnapshot.child("name").getValue().toString();
                            waiter_request = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("waiter").child("pending").child(waiter_name).child(id);
                            waiter_declined = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("waiter").child("declined").child(waiter_name).child(id);
                            cashier_declined = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cashier").child("declined").child(id);
                            cashier_approved = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cashier").child("approved").child(id);
                            declined = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("declined").child(id);
                            cooker_declined = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cooker").child("declined").child(id);

                            waiter_declined.setValue(dataSnapshot.getValue());
                            cashier_declined.setValue(dataSnapshot.getValue());
                            cooker_declined.setValue(dataSnapshot.getValue());
                            declined.setValue(dataSnapshot.getValue());

                            waiter_request.removeValue();
                            cooker_request.removeValue();
                            cashier_approved.removeValue();
                            req_ca.removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                done();
            }
        });
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getString("user", "").equalsIgnoreCase("cashier")) {
                    //add to user activity
                    String time = new SimpleDateFormat("hh:mm").format(new Date());
                    DatabaseReference activity = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("activity");
                    String activity_id = System.currentTimeMillis() + "";
                    activity.child(activity_id).setValue(new Activity("cashier", date, activity_id, getSharedPreferences("user", MODE_PRIVATE).getString("name", "unknown"), time, "approval", id));
                    //end of user activity
                    final DatabaseReference req = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cashier").child("request").child(id);
                    final DatabaseReference app = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cashier").child("approved").child(id);
                    final DatabaseReference pass = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cooker").child("request").child(id);
                    cooker_requested = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cooker").child("requested").child(id);
                    req.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            app.setValue(dataSnapshot.getValue());
                            pass.setValue(dataSnapshot.getValue());
                            cooker_requested.setValue(dataSnapshot.getValue());
                            req.removeValue();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else if (user.getString("user", "").equalsIgnoreCase("cooker")) {
                    //add to user activity
                    String time = new SimpleDateFormat("hh:mm").format(new Date());
                    DatabaseReference activity = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("activity");
                    String activity_id = System.currentTimeMillis() + "";
                    activity.child(activity_id).setValue(new Activity("cooker", date, activity_id, getSharedPreferences("user", MODE_PRIVATE).getString("name", "unknown"), time, "approval", id));
                    //end of user activity
                    final DatabaseReference req_ca = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cashier").child("pending").child(id);
                    final DatabaseReference app_ca = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cashier").child("approved").child(id);
                    final DatabaseReference req = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cooker").child("request").child(id);
                    req.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            waiter_name = dataSnapshot.child("name").getValue().toString();
                            waiter_request = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("waiter").child("pending").child(waiter_name).child(id);
                            waiter_request.removeValue();
                            req_ca.removeValue();
                            app_ca.setValue(dataSnapshot.getValue());
                            waiter_approved = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("waiter").child("approved").child(waiter_name).child(id);
                            waiter_approved.setValue(dataSnapshot.getValue());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    final DatabaseReference app = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cooker").child("approved").child(id);
                    final DatabaseReference pass = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("approved").child(id);
                    final DatabaseReference sold = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("sold");

                    req.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.child("foods").getChildren()){
                                soldFoods.add(snapshot.getValue(Food.class));
                            }
                            for (int i = 0 ; i < soldFoods.size(); i++){
                                int finalI = i;
                                sold.child(soldFoods.get(i).getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        old_amount = 0;
                                        old_price = 0;
                                        if (dataSnapshot.hasChildren()){
                                            old_amount = Integer.parseInt(dataSnapshot.child("quantity").getValue().toString());
                                            old_price = Integer.parseInt(dataSnapshot.child("price").getValue().toString());
                                        }
                                        new_amount = Integer.parseInt(soldFoods.get(finalI).getQuantity()) + old_amount;
                                        new_price = new_amount * Integer.parseInt(soldFoods.get(finalI).getPrice());
                                        sold.child(soldFoods.get(finalI).getName()).setValue(new Food(soldFoods.get(finalI).getName(), Integer.toString(new_price), Integer.toString(new_amount)));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    req.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            app.setValue(dataSnapshot.getValue());
                            pass.setValue(dataSnapshot.getValue());
                            req.removeValue();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                else {
                }
                done();
            }
        });
    }

    private void done(){
        switch (user.getString("user", "")){
            case "waiter":
                startActivity(new Intent(this, Waiter.class));
                finish();
                break;
            case "cashier":
                startActivity(new Intent(this, Cashier.class));
                finish();
                break;
            case "cooker":
                startActivity(new Intent(this, Cooker.class));
                finish();
                break;
            default:
                startActivity(new Intent(this, MainActivity.class));
                finish();;
        }
    }

    DatabaseReference data;
    private void getData() {
        if (user.getString("user", "").equalsIgnoreCase("waiter")){
            data = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("waiter").child("pending").child(user.getString("name", "")).child(id);
        }
        else if (user.getString("user", "").equalsIgnoreCase("cashier")){
            data = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("cashier").child("pending").child(id);
        }
        else {
            data = FirebaseDatabase.getInstance().getReference("transaction").child(date).child(user.getString("user", "")).child("request").child(id);
        }
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                request = dataSnapshot.getValue(Request.class);
                for (DataSnapshot snapshot : dataSnapshot.child("foods").getChildren()) {
                    foods.add(snapshot.getValue(Food.class));
                }
                total.setText(dataSnapshot.child("total").getValue().toString() + " ETB");
                adapter = new ApprovalAdapter(foods, Approval.this);
                loader.setVisibility(View.GONE);
                recycle.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        done();
    }
}
