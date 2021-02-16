package com.izhar.ism.waiter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.ism.MainActivity;
import com.izhar.ism.OrderActivity;
import com.izhar.ism.R;
import com.izhar.ism.adapters.PagerAdapter;
import com.izhar.ism.admin.SeeMore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Waiter extends AppCompatActivity {
    TextView pending_text, finished_text, approved_amount, requested_amount, declined_text, declined_amount;
    TabLayout tab;
    ViewPager view;
    PagerAdapter pagerAdapter;
    int rTotal = 0 , aTotal = 0, dTotal = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("waiter");
        setContentView(R.layout.activity_waiter);
        SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
        user.edit().putString("user", "waiter").apply();
        Button new_order = findViewById(R.id.new_order);
        pending_text = findViewById(R.id.pending);
        finished_text = findViewById(R.id.finished);
        approved_amount = findViewById(R.id.approved_amount);
        requested_amount = findViewById(R.id.requested_amount);
        declined_text = findViewById(R.id.declined);
        declined_amount = findViewById(R.id.declined_amount);
        new_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Waiter.this, OrderActivity.class));
                finish();
            }
        });
        setValues();
        tab = findViewById(R.id.tab);
        view = findViewById(R.id.viewpager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tab.getTabCount(), "waiter");
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
        final DatabaseReference request = FirebaseDatabase.getInstance().getReference("waiter").child("requested").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(getSharedPreferences("user", MODE_PRIVATE).getString("name", "default"));
        DatabaseReference approved = FirebaseDatabase.getInstance().getReference("waiter").child("approved").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(getSharedPreferences("user", MODE_PRIVATE).getString("name", "default"));
        DatabaseReference declined = FirebaseDatabase.getInstance().getReference("waiter").child("declined").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(getSharedPreferences("user", MODE_PRIVATE).getString("name", "default"));
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
                .putExtra("actor", "waiter")
                .putExtra("type", "requested"));
    }

    public void approved(View view) {
        startActivity(new Intent(this, SeeMore.class)
                .putExtra("actor", "waiter")
                .putExtra("type", "approved"));
    }

    public void declined(View view) {
        startActivity(new Intent(this, SeeMore.class)
                .putExtra("actor", "waiter")
                .putExtra("type", "declined"));
    }
    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout){
            SharedPreferences declined = getSharedPreferences("declined", MODE_PRIVATE);
            SharedPreferences approved = getSharedPreferences("finished", MODE_PRIVATE);
            SharedPreferences requested = getSharedPreferences("pending", MODE_PRIVATE);
            declined.edit().clear().apply();
            approved.edit().clear().apply();
            requested.edit().clear().apply();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return true;
    }
}