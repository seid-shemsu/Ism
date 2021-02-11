package com.izhar.ism.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.ism.R;
import com.izhar.ism.adapters.PendingAdapter;
import com.izhar.ism.objects.Request;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingTab extends Fragment {

    public PendingTab() {
        // Required empty public constructor
    }
    String user;
    public PendingTab(String user){
        this.user = user;
    }
    List<Request> requests = new ArrayList<>();
    PendingAdapter pendingAdapter;
    RecyclerView recycle;
    TextView not_found;
    LottieAnimationView loader;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pending_tab, container, false);
        recycle = view.findViewById(R.id.recycle);
        recycle.setLayoutManager(new LinearLayoutManager(getContext()));
        recycle.setHasFixedSize(true);
        not_found = view.findViewById(R.id.not_found);
        loader = view.findViewById(R.id.loader);
        getData();
        return view;
    }

    DatabaseReference data;
    private void getData() {
        SharedPreferences u_name = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String name = u_name.getString("name", "default");
        if (user.equalsIgnoreCase("waiter")){
            data = FirebaseDatabase.getInstance().getReference().child("waiter").child("pending").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date())).child(name);
        }
        else{
            data = FirebaseDatabase.getInstance().getReference().child(user).child("request").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        }
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requests.clear();
                not_found.setVisibility(View.GONE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String id = snapshot.getKey();
                    String time = snapshot.child("dateTime").getValue().toString();
                    String price = snapshot.child("total").getValue().toString() + "\nETB";
                    String name = snapshot.child("name").getValue().toString();
                    requests.add(new Request(id, price, time, name));
                }
                if (requests.size()==0){
                    not_found.setVisibility(View.VISIBLE);
                }
                loader.setVisibility(View.GONE);
                pendingAdapter = new PendingAdapter(getContext(), requests);
                recycle.setAdapter(pendingAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
