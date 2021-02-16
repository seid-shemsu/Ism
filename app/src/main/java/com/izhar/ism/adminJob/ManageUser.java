package com.izhar.ism.adminJob;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.izhar.ism.R;
import com.izhar.ism.WaiterPerformance;

public class ManageUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);
        setTitle("Manage Users");
    }

    public void waiter_performance(View view) {
        startActivity(new Intent(this, WaiterPerformance.class));
    }

    public void add_waiter(View view) {

    }
}