package com.izhar.ism;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.ism.adapters.PagerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Cashier extends AppCompatActivity {

    TextView pending_text, finished_text, approved_amount, requested_amount;
    TabLayout tab;
    ViewPager view;
    PagerAdapter pagerAdapter;
    int rTotal = 0 , aTotal = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("cashier");
        setContentView(R.layout.activity_cashier);
        SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
        user.edit().putString("user", "cashier").apply();
        pending_text = findViewById(R.id.pending);
        finished_text = findViewById(R.id.finished);
        approved_amount = findViewById(R.id.approved_amount);
        requested_amount = findViewById(R.id.requested_amount);
        setValues();
        tab = findViewById(R.id.tab);
        view = findViewById(R.id.viewpager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tab.getTabCount(), "cashier");
        view.setAdapter(pagerAdapter);
        view.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                view.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void setValues() {

        final DatabaseReference request = FirebaseDatabase.getInstance().getReference("cashier").child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        DatabaseReference approved = FirebaseDatabase.getInstance().getReference("cashier").child("approved").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
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
    }
}