package com.izhar.ism.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.ism.MainActivity;
import com.izhar.ism.R;
import com.izhar.ism.UserActivity;
import com.izhar.ism.WaiterPerformance;
import com.izhar.ism.adminJob.AddFood;
import com.izhar.ism.adminJob.FoodList;
import com.izhar.ism.adminJob.ManageUser;
import com.izhar.ism.objects.Activity;
import com.izhar.ism.objects.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Admin extends AppCompatActivity {
    TextView pending_text, approved_text /*approved_amount*/ /*requested_amount*/, declined_text/*, declined_amount, sold_amount, sold_items*/;
    int rTotal = 0 , aTotal = 0, dTotal = 0, sTotal = 0;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        pending_text = findViewById(R.id.pending);
        approved_text = findViewById(R.id.approved);/*
        approved_amount = findViewById(R.id.approved_amount);
        requested_amount = findViewById(R.id.requested_amount);*/
        declined_text = findViewById(R.id.declined);
//        declined_amount = findViewById(R.id.declined_amount);
//        sold_amount = findViewById(R.id.sold_amount);
//        sold_items = findViewById(R.id.sold_items);
        setValues();

    }

    private void setValues() {
        DatabaseReference declined = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("declined");
        DatabaseReference request = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("requested");
        DatabaseReference approved = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("approved");
        DatabaseReference sold = FirebaseDatabase.getInstance().getReference("transaction").child(date).child("sold");
        request.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    pending_text.setText(dataSnapshot.getChildrenCount() + "");
                    rTotal = 0 ;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        rTotal += Integer.parseInt(snapshot.child("total").getValue().toString());
                    //requested_amount.setText(rTotal + " ETB");
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
                    //approved_amount.setText(aTotal + " ETB");
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
                    //declined_amount.setText(dTotal + " ETB");
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
                    //sold_items.setText(dataSnapshot.getChildrenCount() + "");
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                        sTotal += Integer.parseInt(snapshot.child("price").getValue().toString());
                    //sold_amount.setText(sTotal + " ETB");
                }
                    //sold_items.setText("0");
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
                                    Toast.makeText(Admin.this, "user added successfully", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Admin.this, "select user role", Toast.LENGTH_SHORT).show();
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