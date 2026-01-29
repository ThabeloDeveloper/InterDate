package com.mecaroid.interdate.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

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
import com.mecaroid.interdate.Adapters.Recycler.MingleRecyclerAdapter;
import com.mecaroid.interdate.Adapters.Recycler.ShimaGridAdapter;
import com.mecaroid.interdate.Models.MingleModel;
import com.mecaroid.interdate.Models.ShimaModel;
import com.mecaroid.interdate.R;
import com.mecaroid.interdate.databinding.FragmentGlobalBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class Global extends Fragment {



    public Global() {
        // Required empty public constructor
    }

    FragmentGlobalBinding binding;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    MingleRecyclerAdapter adapter;
    ArrayList<MingleModel> mingleModels;
    ShimaGridAdapter gridAdapter;
    ArrayList<ShimaModel> shimaModels;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGlobalBinding.inflate(getLayoutInflater());

        shimaModels = new ArrayList<>();
        shimaModels.clear();
        ShimaModel model = new ShimaModel();model.setGridCounter(1);
        ShimaModel model1 = new ShimaModel();model1.setGridCounter(2);
        ShimaModel model2 = new ShimaModel();model.setGridCounter(1);
        ShimaModel model3 = new ShimaModel();model1.setGridCounter(2);
        ShimaModel model4 = new ShimaModel();model.setGridCounter(1);
        ShimaModel model5 = new ShimaModel();model1.setGridCounter(2);
        ShimaModel model6 = new ShimaModel();model1.setGridCounter(2);
        shimaModels.add(model);shimaModels.add(model1);shimaModels.add(model2);
        shimaModels.add(model3);shimaModels.add(model4);shimaModels.add(model5);shimaModels.add(model6);
        gridAdapter = new ShimaGridAdapter(shimaModels);
        if (binding.RecyclerShima !=null){
            binding.RecyclerShima.setItemAnimator(new DefaultItemAnimator());
            binding.RecyclerShima.setLayoutManager(new GridLayoutManager(getContext(),2));
            binding.RecyclerShima.setAdapter(gridAdapter);
        }


        //////////////////////////////////////////////////////////////////
        mingleModels = new ArrayList<>();
        adapter = new MingleRecyclerAdapter(mingleModels,getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        if (binding.mingleGrid !=null){
            binding.mingleGrid.setLayoutManager(layoutManager);
            binding.mingleGrid.setItemAnimator(new DefaultItemAnimator());
            binding.mingleGrid.setAdapter(adapter);
        }


        ////Tablet Qualifies ////////////
        //Data//
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        if (binding.mingleGridTab !=null){
            binding.mingleGridTab.setAdapter(adapter);
            binding.mingleGridTab.setItemAnimator(new DefaultItemAnimator());

            binding.mingleGridTab.setLayoutManager(gridLayoutManager);
        }

        //Shimmer//
        if (binding.RecyclerShimaTab !=null){
            GridLayoutManager gridLayoutManagerr = new GridLayoutManager(getContext(),3);
            binding.RecyclerShimaTab.setAdapter(gridAdapter);
            binding.RecyclerShimaTab.setItemAnimator(new DefaultItemAnimator());
            binding.RecyclerShimaTab.setLayoutManager(gridLayoutManagerr);
        }

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("preGenderCode");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String preGenderCode = snapshot.getValue(String.class);
                    adapter = new MingleRecyclerAdapter(mingleModels,getActivity());
                    if (binding.mingleGrid !=null){
                        binding.mingleGrid.setAdapter(adapter);
                    }

                    Query query1 = databaseReference.orderByChild("genderCode").equalTo(preGenderCode);
                    query1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mingleModels.clear();
                            binding.mingleShimma.stopShimmer();
                            binding.mingleShimma.setVisibility(View.GONE);
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                MingleModel user = userSnapshot.getValue(MingleModel.class);
                                if (!Objects.equals(user.getUser_id(), FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                    Collections.shuffle(mingleModels);
                                    mingleModels.add(user);
                                }
                                if (adapter.getItemCount() > 0) {
                                    binding.mingleShimma.stopShimmer();
                                    binding.mingleShimma.setVisibility(View.GONE);
                                    binding.noDataInLocation.setVisibility(View.GONE);

                                }if (adapter.getItemCount() <1){
                                    binding.mingleShimma.setVisibility(View.GONE);
                                    binding.noDataInLocation.setVisibility(View.VISIBLE);
                                    binding.errorMsg.setText(getString(R.string.no_users_found_at_your_location));
                                    binding.errorImage.setImageDrawable(getResources().getDrawable(R.drawable.location_off));

                                }
                            }


                            adapter.notifyDataSetChanged();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getContext(),R.style.CustomProgressDialogStyle);
                alertDialog.setCancelable(false);
                alertDialog.setMessage(error.getMessage());
                alertDialog.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();


                    }
                });
                alertDialog.create();
                alertDialog.show();

            }
        });

        binding.mingleShimma.startShimmer();
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

}