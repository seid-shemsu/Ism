package com.izhar.ism.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.ism.MainActivity;
import com.izhar.ism.R;
import com.izhar.ism.adminJob.AddFood;
import com.izhar.ism.adminJob.FoodList;
import com.izhar.ism.adminJob.ManageUser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Admin extends AppCompatActivity {
    TextView pending_text, approved_text, approved_amount, requested_amount, declined_text, declined_amount, sold_amount, sold_items;
    int rTotal = 0 , aTotal = 0, dTotal = 0, sTotal = 0;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        pending_text = findViewById(R.id.pending);
        approved_text = findViewById(R.id.approved);
        approved_amount = findViewById(R.id.approved_amount);
        requested_amount = findViewById(R.id.requested_amount);
        declined_text = findViewById(R.id.declined);
        declined_amount = findViewById(R.id.declined_amount);
        sold_amount = findViewById(R.id.sold_amount);
        sold_items = findViewById(R.id.sold_items);
        setValues();

    }

    private void setValues() {
        DatabaseReference declined = FirebaseDatabase.getInstance().getReference(date).child("declined");
        DatabaseReference request = FirebaseDatabase.getInstance().getReference(date).child("requested");
        DatabaseReference approved = FirebaseDatabase.getInstance().getReference(date).child("approved");
        DatabaseReference sold = FirebaseDatabase.getInstance().getReference(date).child("sold");
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
                    approved_text.setText(dataSnapshot.getChildrenCount() + "");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        aTotal += Integer.parseInt(snapshot.child("total").getValue().toString());
                    approved_amount.setText(aTotal + " ETB");
                }

                else
                    approved_text.setText("0");
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
        sold.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    sTotal = 0;
                    sold_items.setText(dataSnapshot.getChildrenCount() + "");
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                        sTotal += Integer.parseInt(snapshot.child("price").getValue().toString());
                    sold_amount.setText(sTotal + " ETB");
                }
                else
                    sold_items.setText("0");
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
    public void approved(View view) {
        startActivity(new Intent(this, SeeMore.class)
                .putExtra("actor", "admin")
                .putExtra("type", "approved"));
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