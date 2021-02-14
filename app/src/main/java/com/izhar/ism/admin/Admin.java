package com.izhar.ism.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.ism.R;
import com.izhar.ism.adminJob.AddFood;
import com.izhar.ism.adminJob.FoodList;
import com.izhar.ism.adminJob.ManageUser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Admin extends AppCompatActivity {
    TextView pending_text, finished_text, approved_amount, requested_amount, declined_text, declined_amount;
    int rTotal = 0 , aTotal = 0, dTotal = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        pending_text = findViewById(R.id.pending);
        finished_text = findViewById(R.id.finished);
        approved_amount = findViewById(R.id.approved_amount);
        requested_amount = findViewById(R.id.requested_amount);
        declined_text = findViewById(R.id.declined);
        declined_amount = findViewById(R.id.declined_amount);

        setValues();

    }

    private void setValues() {
        DatabaseReference declined = FirebaseDatabase.getInstance().getReference().child("declined").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        DatabaseReference request = FirebaseDatabase.getInstance().getReference().child("requested").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        DatabaseReference approved = FirebaseDatabase.getInstance().getReference().child("approved").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        request.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    pending_text.setText(dataSnapshot.getChildrenCount() + "");
                    rTotal = 0 ;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        rTotal += Integer.parseInt(snapshot.child("total").getValue().toString());
                    requested_amount.setText(rTotal + " ETB");
                }
                else
                    pending_text.setText("0");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        approved.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    aTotal = 0;
                    finished_text.setText(dataSnapshot.getChildrenCount() + "");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        aTotal += Integer.parseInt(snapshot.child("total").getValue().toString());
                    approved_amount.setText(aTotal + " ETB");
                }

                else
                    finished_text.setText("0");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        declined.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    dTotal = 0;
                    declined_text.setText(dataSnapshot.getChildrenCount() + "");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        dTotal += Integer.parseInt(snapshot.child("total").getValue().toString());
                    declined_amount.setText(dTotal + " ETB");
                }
                else
                    declined_text.setText("0");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void requested(View view) {
        startActivity(new Intent(this, SeeMore.class)
                .putExtra("actor", "admin")
                .putExtra("type", "requested"));
    }

    public void sold(View view) {
        startActivity(new Intent(this, SeeMore.class)
                .putExtra("actor", "admin")
                .putExtra("type", "sold"));
    }

    public void declined(View view) {
        startActivity(new Intent(this, SeeMore.class)
                .putExtra("actor", "admin")
                .putExtra("type", "declined"));
    }


    public void manage(View view) {
        startActivity(new Intent(Admin.this, ManageUser.class));
    }

    public void addFood(View view) {
        startActivity(new Intent(Admin.this, AddFood.class));
    }

    public void foodList(View view) {
        startActivity(new Intent(Admin.this, FoodList.class));
    }
}