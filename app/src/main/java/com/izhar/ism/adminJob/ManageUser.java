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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        Button add = dialog.findViewById(R.id.add);
        EditText name, username, password;
        name = dialog.findViewById(R.id.name);
        username = dialog.findViewById(R.id.username);
        password = dialog.findViewById(R.id.password);
        Spinner type = dialog.findViewById(R.id.spinner);
        dialog.show();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()){
                    DatabaseReference user = FirebaseDatabase.getInstance().getReference("users");
                    String id = System.currentTimeMillis() + "";
                    String date  = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                    String time = new SimpleDateFormat("hh:mm").format(new Date());
                    DatabaseReference activity = FirebaseDatabase.getInstance().getReference("activity").child(date);
                    String activity_id = System.currentTimeMillis() + "";
                    activity.child(activity_id).setValue(new Activity("admin", date, activity_id, getSharedPreferences("user", MODE_PRIVATE).getString("name", "unknown"), time, "add new user", id));
                    user.child(id).setValue(new User(name.getText().toString(), type.getSelectedItem().toString(), password.getText().toString(), username.getText().toString()))
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
                else if (type.getSelectedItemPosition() == 0){
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