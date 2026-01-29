package com.mecaroid.interdate.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mecaroid.interdate.Adapters.Recycler.CallsRecyclerAdaper;
import com.mecaroid.interdate.Models.CallSModel;
import com.mecaroid.interdate.R;
import com.mecaroid.interdate.databinding.FragmentCallsBinding;

import java.util.ArrayList;
import java.util.Objects;


public class Calls extends Fragment {



    public Calls() {
        // Required empty public constructor
    }
    FragmentCallsBinding binding;
    CallsRecyclerAdaper adapter;
    ArrayList<CallSModel> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCallsBinding.inflate(getLayoutInflater());
        data = new ArrayList<>();
        adapter = new CallsRecyclerAdaper(data);
        binding.shima.startShimmer();
        binding.chatsRecycler.setItemAnimator(new DefaultItemAnimator());
        binding.chatsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.chatsRecycler.setAdapter(adapter);
        binding.deleteCalls.setImageTintList(ColorStateList.valueOf(getContext().getColor(R.color.white)));
        binding.deleteCalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder deleteAlert = new AlertDialog.Builder(getContext(),R.style.CustomProgressDialogStyle);
                deleteAlert.setTitle(R.string.delete_your_side_msg);
                deleteAlert.setMessage(getContext().getString(R.string.about_to_delete_call_history));
                deleteAlert.setPositiveButton(getContext().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                deleteAlert.setNegativeButton(getContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProgressDialog deleteProgress = new ProgressDialog(getContext());
                        deleteProgress.setCancelable(false);
                        deleteProgress.setMessage(getContext().getString(R.string.please_wait));
                        deleteProgress.show();
                        dialog.dismiss();
                        DatabaseReference MylastSent = FirebaseDatabase.getInstance().getReference("Calls")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getUid()));
                        MylastSent.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        deleteProgress.dismiss();
                                        dialog.dismiss();
                                        Toast.makeText(getContext(), getContext().getString(R.string.delete_succesfull), Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        deleteProgress.dismiss();
                                        Toast.makeText(getContext(), getContext().getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                deleteAlert.create();
                deleteAlert.show();
            }
        });
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Calls")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.shima.stopShimmer();
                binding.shima.setVisibility(View.GONE);
                data.clear();
                for (DataSnapshot datata : snapshot.getChildren()){
                    CallSModel calls = datata.getValue(CallSModel.class);
                    data.add(calls);
                }
                if (!snapshot.exists()){
                    binding.deleteCalls.setVisibility(View.GONE);
                    binding.noMsg.setVisibility(View.VISIBLE);
                    binding.noMsg.setText(getString(R.string.your_call_here));
                }else {
                    binding.noMsg.setVisibility(View.GONE);
                    binding.deleteCalls.setVisibility(View.VISIBLE);
                    binding.noMsg.setText(getString(R.string.your_call_here));
                }
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