package com.izhar.ism;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.izhar.ism.objects.Food;
import com.izhar.ism.objects.Request;
import com.izhar.ism.waiter.Waiter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Approval extends AppCompatActivity {

    RecyclerView recycle;
    Button approve, decline;
    LottieAnimationView loader;
    TextView total;
    List<Food> foods = new ArrayList<>();
    String id;
    SharedPreferences user;
    ApprovalAdapter adapter;
    Request request;
    DatabaseReference approved, passed;
    String waiter_name;
    DatabaseReference waiter_request, cashier_request, declined, cooker_request, waiter_approved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("approval");
        setContentView(R.layout.activity_approval);
        id = getIntent().getExtras().getString("orderId");
        user = getSharedPreferences("user", MODE_PRIVATE);
        approve = findViewById(R.id.approve);
        decline = findViewById(R.id.decline);

        loader = findViewById(R.id.loader);
        total = findViewById(R.id.total_price);
        recycle = findViewById(R.id.recycle);
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        if (user.getString("user", "admin").equalsIgnoreCase("waiter")) {
            approve.setVisibility(View.GONE);
            decline.setText("cancel");
        }
        getData();
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getString("user", "").equalsIgnoreCase("waiter")) {
                    SharedPreferences u_name = getSharedPreferences("user", MODE_PRIVATE);
                    String name = u_name.getString("name", "default");
                    cashier_request = FirebaseDatabase.getInstance().getReference("cashier").child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    waiter_request = FirebaseDatabase.getInstance().getReference("waiter").child("pending").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(name).child(id);
                    declined = FirebaseDatabase.getInstance().getReference().child("declined").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    waiter_request.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            declined.setValue(dataSnapshot.getValue());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    cashier_request.removeValue();
                    waiter_request.removeValue();
                }

                else if (user.getString("user", "").equalsIgnoreCase("cashier")){
                    cashier_request = FirebaseDatabase.getInstance().getReference("cashier").child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    cashier_request.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            waiter_name = dataSnapshot.child("name").getValue().toString();
                            waiter_request = FirebaseDatabase.getInstance().getReference("waiter").child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(waiter_name).child(id);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    declined = FirebaseDatabase.getInstance().getReference().child("declined").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    cashier_request.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            declined.setValue(dataSnapshot.getValue());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    cashier_request.removeValue();
                    waiter_request.removeValue();
                }
                else {
                    DatabaseReference cashApproved = FirebaseDatabase.getInstance().getReference("cashier").child("approved").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    cashApproved.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            waiter_name = dataSnapshot.child("name").getValue().toString();
                            waiter_request = FirebaseDatabase.getInstance().getReference("waiter").child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(waiter_name).child(id);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    cooker_request = FirebaseDatabase.getInstance().getReference("cooker").child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    declined = FirebaseDatabase.getInstance().getReference().child("declined").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    cooker_request.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            declined.setValue(dataSnapshot.getValue());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    cooker_request.removeValue();
                    cashApproved.removeValue();
                    waiter_request.removeValue();
                }
                done();
            }
        });
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getString("user", "").equalsIgnoreCase("cashier")) {
                    final DatabaseReference req = FirebaseDatabase.getInstance().getReference("cashier").child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    final DatabaseReference app = FirebaseDatabase.getInstance().getReference("cashier").child("approved").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    final DatabaseReference pass = FirebaseDatabase.getInstance().getReference("cooker").child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
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
                else if (user.getString("user", "").equalsIgnoreCase("cooker")) {
                    final DatabaseReference req = FirebaseDatabase.getInstance().getReference("cooker").child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    req.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            waiter_name = dataSnapshot.child("name").getValue().toString();
                            waiter_request = FirebaseDatabase.getInstance().getReference("waiter").child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(waiter_name).child(id);
                            waiter_approved = FirebaseDatabase.getInstance().getReference("cooker").child("approved").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(waiter_name).child(id);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    final DatabaseReference app = FirebaseDatabase.getInstance().getReference("cooker").child("approved").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    final DatabaseReference pass = FirebaseDatabase.getInstance().getReference("approved").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    req.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            app.setValue(dataSnapshot.getValue());
                            pass.setValue(dataSnapshot.getValue());
                            waiter_approved.setValue(dataSnapshot.getValue());
                            waiter_request.removeValue();
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

    private void getData() {
        final DatabaseReference data = FirebaseDatabase.getInstance().getReference(user.getString("user", "")).child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(Approval.this, dataSnapshot.getChildrenCount() + "\n" + id + "\n" + user.getString("user", "admin"), Toast.LENGTH_SHORT).show();
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
}
