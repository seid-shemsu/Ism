package com.izhar.ism;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.tasks.OnSuccessListener;
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
                    DatabaseReference cashrequest = FirebaseDatabase.getInstance().getReference("cashier").child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    DatabaseReference waiterrequest = FirebaseDatabase.getInstance().getReference("waiter").child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    final DatabaseReference declined = FirebaseDatabase.getInstance().getReference().child("declined").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    waiterrequest.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            declined.setValue(dataSnapshot.getValue());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    cashrequest.removeValue();
                    waiterrequest.removeValue();
                }
                else if (user.getString("user", "").equalsIgnoreCase("cashier")){
                    DatabaseReference cashrequest = FirebaseDatabase.getInstance().getReference("cashier").child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    DatabaseReference waiterrequest = FirebaseDatabase.getInstance().getReference("waiter").child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    final DatabaseReference declined = FirebaseDatabase.getInstance().getReference().child("declined").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    cashrequest.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            declined.setValue(dataSnapshot.getValue());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    cashrequest.removeValue();
                    waiterrequest.removeValue();
                }
                else {
                    DatabaseReference cashApproved = FirebaseDatabase.getInstance().getReference("cashier").child("approved").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    DatabaseReference waiterRequest = FirebaseDatabase.getInstance().getReference("waiter").child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    DatabaseReference cookerRequest = FirebaseDatabase.getInstance().getReference("cooker").child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    final DatabaseReference declined = FirebaseDatabase.getInstance().getReference().child("declined").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    cookerRequest.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            declined.setValue(dataSnapshot.getValue());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    cookerRequest.removeValue();
                    cashApproved.removeValue();
                    waiterRequest.removeValue();
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
                    final DatabaseReference waiter_req = FirebaseDatabase.getInstance().getReference("waiter").child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    final DatabaseReference waiter_app = FirebaseDatabase.getInstance().getReference("waiter").child("approved").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    final DatabaseReference req = FirebaseDatabase.getInstance().getReference("cooker").child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    final DatabaseReference app = FirebaseDatabase.getInstance().getReference("cooker").child("approved").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    final DatabaseReference pass = FirebaseDatabase.getInstance().getReference("approved").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(id);
                    req.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            app.setValue(dataSnapshot.getValue());
                            pass.setValue(dataSnapshot.getValue());
                            waiter_app.setValue(dataSnapshot.getValue());
                            waiter_req.removeValue();
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
