package com.izhar.ism.adminJob;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.izhar.ism.admin.Admin;
import com.izhar.ism.R;
import com.izhar.ism.objects.Food;

public class AddFood extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("add food");
        setContentView(R.layout.activity_add_food);
        final Button add = findViewById(R.id.add);
        final EditText name = findViewById(R.id.name);
        final EditText price = findViewById(R.id.price);
        final LottieAnimationView loader = findViewById(R.id.loader);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add.setVisibility(View.GONE);
                loader.setVisibility(View.VISIBLE);
                DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("items");
                long id = System.currentTimeMillis();
                data.child(id + "").setValue(new Food(name.getText().toString(), price.getText().toString(), id)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(AddFood.this, Admin.class));
                        finish();
                    }
                });
            }
        });
    }
}