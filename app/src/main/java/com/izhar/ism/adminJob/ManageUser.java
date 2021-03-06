package com.izhar.ism.adminJob;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.izhar.ism.R;
import com.izhar.ism.UserActivity;
import com.izhar.ism.WaiterPerformance;
import com.izhar.ism.objects.Activity;
import com.izhar.ism.objects.User;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.add_new_waiter);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        Button add = dialog.findViewById(R.id.add);
        Button cancel = dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextInputEditText name, username, password;

        name = dialog.findViewById(R.id.name);
        username = dialog.findViewById(R.id.username);
        password = dialog.findViewById(R.id.password);
        AutoCompleteTextView type = dialog.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.users, R.layout.list_item);
        type.setAdapter(adapter);
        dialog.show();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()){
                    String id = System.currentTimeMillis() + "";
                    DatabaseReference user = FirebaseDatabase.getInstance().getReference("users");
                    DatabaseReference waiter = FirebaseDatabase.getInstance().getReference("waiter");
                    if (type.getText().toString().equalsIgnoreCase("waiter"))
                        waiter.child(id).setValue(new User(name.getText().toString(), type.getText().toString(), password.getText().toString(), username.getText().toString()));
                    String date  = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                    String time = new SimpleDateFormat("hh:mm").format(new Date());
                    DatabaseReference activity = FirebaseDatabase.getInstance().getReference(date).child("activity");
                    String activity_id = System.currentTimeMillis() + "";
                    activity.child(activity_id).setValue(new Activity("admin", date, activity_id, getSharedPreferences("user", MODE_PRIVATE).getString("name", "unknown"), time, "add new user", id));
                    user.child(id).setValue(new User(name.getText().toString(), type.getText().toString(), password.getText().toString(), username.getText().toString()))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ManageUser.this, "user added successfully", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                }
            }

            private boolean valid() {
                if (name.getText().toString().isEmpty()){
                    name.setError("name required");
                    return false;
                }
                else if (username.getText().toString().isEmpty()){
                    username.setError("username required");
                    return false;
                }
                else if (password.getText().toString().isEmpty()){
                    password.setError("password required");
                    return false;
                }
                else if (type.getText().toString().length() == 0){
                    Toast.makeText(ManageUser.this, "select user role", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });
    }

    public void activity(View view) {
        startActivity(new Intent(this, UserActivity.class));
    }

}