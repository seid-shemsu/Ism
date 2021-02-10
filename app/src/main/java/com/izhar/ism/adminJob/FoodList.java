package com.izhar.ism.adminJob;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.izhar.ism.R;

public class FoodList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        setTitle("Food List");
    }
}