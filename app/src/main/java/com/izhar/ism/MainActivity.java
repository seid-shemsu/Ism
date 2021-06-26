package com.izhar.ism;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.ism.admin.Admin;
import com.izhar.ism.cashier.Cashier;
import com.izhar.ism.cooker.Cooker;
import com.izhar.ism.waiter.Waiter;

public class MainActivity extends AppCompatActivity {

    Button login;
    EditText username, password;
    DatabaseReference data;
    LottieAnimationView loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("main");
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loader = findViewById(R.id.loader);
        data = FirebaseDatabase.getInstance().getReference().child("users");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.setVisibility(View.VISIBLE);
                login.setVisibility(View.GONE);
                getData();
            }
        });
    }

    int incorrect = 0 ;
    private void getData() {
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (!snapshot.child("username").getValue().toString().equalsIgnoreCase(username.getText().toString()) || !snapshot.child("password").getValue().toString().equalsIgnoreCase(password.getText().toString())){
                        loader.setVisibility(View.GONE);
                        login.setVisibility(View.VISIBLE);
                    }
                    else{
                        SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
                        user.edit().putString("user", snapshot.child("type").getValue().toString()).apply();
                        user.edit().putString("name", snapshot.child("name").getValue().toString()).apply();
                        switch (user.getString("user", "")) {
                            case "cashier":
                                startActivity(new Intent(MainActivity.this, Cashier.class));
                                finish();
                                break;
                            case "waiter":
                                startActivity(new Intent(MainActivity.this, Waiter.class));
                                finish();
                                break;
                            case "cooker":
                                startActivity(new Intent(MainActivity.this, Cooker.class));
                                finish();
                                break;
                            case "admin":
                                startActivity(new Intent(MainActivity.this, Admin.class));
                                finish();
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void gotoActivity(String type) {
        switch (type){

        }
    }
}