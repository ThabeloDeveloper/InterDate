package com.mecaroid.interdate.Fragments.Requests;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mecaroid.interdate.Adapters.Recycler.RequestSentAdapter;
import com.mecaroid.interdate.Models.RequestSentModel;
import com.mecaroid.interdate.R;
import com.mecaroid.interdate.databinding.RequestSentBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Sent extends Fragment {



    public Sent() {
        // Required empty public constructor
    }

    RequestSentBinding binding;
    List<RequestSentModel> data;
    RequestSentAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = RequestSentBinding.inflate(getLayoutInflater());
        data = new ArrayList<>();
        adapter = new RequestSentAdapter(data);
        binding.shima.startShimmer();
        binding.requestSent.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.requestSent.setAdapter(adapter);
        DatabaseReference referenceMe = FirebaseDatabase.getInstance().getReference("Request").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Sent");
        referenceMe.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                binding.shima.stopShimmer();
                binding.shima.setVisibility(View.GONE);
                for (DataSnapshot datata : snapshot.getChildren()){
                    RequestSentModel model = datata.getValue(RequestSentModel.class);
                    data.add(model);


                }
                if (!snapshot.exists()){
                    binding.noData.setVisibility(View.VISIBLE);
                }else {
                    binding.noData.setVisibility(View.GONE);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        referenceMe.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Inflate the layout for this fragment

        return binding.getRoot();
    }
}