package com.mecaroid.interdate.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mecaroid.interdate.Adapters.Recycler.FriendsOnChatsAdapter;
import com.mecaroid.interdate.Models.AcquaintanceModel;
import com.mecaroid.interdate.databinding.FragmentChatsBinding;

import java.util.ArrayList;
import java.util.Objects;


public class Chats extends Fragment {
    FriendsOnChatsAdapter adapter;
    ArrayList<AcquaintanceModel> data;
    FragmentChatsBinding binding;



    public Chats() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(getLayoutInflater());
        data = new ArrayList<>();
        adapter = new FriendsOnChatsAdapter(data);
        binding.chatsRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,true));
        binding.chatsRecycler.setAdapter(adapter);
        binding.shima.startShimmer();
        binding.chatsRecycler.setItemAnimator(new DefaultItemAnimator());
        chats();

        return binding.getRoot();
    }
    private void chats(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Friends").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        Query query = reference.orderByChild("timestamp");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.shima.stopShimmer();
                binding.shima.setVisibility(View.GONE);
                data.clear();
                for (DataSnapshot LastSent : snapshot.getChildren()){
                    AcquaintanceModel chats = LastSent.getValue(AcquaintanceModel.class);
                    assert chats != null;
                    if (!chats.getUser_id().isEmpty()){
                        data.add(chats);
                    }


                }
                if (!snapshot.exists()){
                    binding.noMsg.setVisibility(View.VISIBLE);
                }else {
                    binding.noMsg.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}