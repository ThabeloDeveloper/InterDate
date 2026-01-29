package com.mecaroid.interdate.Fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import com.mecaroid.interdate.databinding.ErrorMessagesNoUsersBinding;
import com.mecaroid.interdate.databinding.ErrorMessagesNoUsersFoundTownBinding;
import com.mecaroid.interdate.databinding.FragmentMingleBinding;

import java.util.ArrayList;
import java.util.Objects;


public class Mingle extends Fragment {
    ArrayList<MingleModel> datadata;
    ArrayList<ShimaModel> shimaModelArrayList;
    MingleRecyclerAdapter adapter;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

    ////////////////////////////////////////////////////////////




    public Mingle() {
        // Required empty public constructor
    }
    FragmentMingleBinding binding;
    String ageFrom,ageTo,Gender,Race,Religion,town,city,province,country;
    ShimaGridAdapter adaapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMingleBinding.inflate(getLayoutInflater());
        datadata = new ArrayList<>();
        shimaModelArrayList = new ArrayList<>();
        shimaModelArrayList.clear();
        ShimaModel model = new ShimaModel();model.setGridCounter(1);
        ShimaModel model1 = new ShimaModel();model1.setGridCounter(2);
        ShimaModel model2 = new ShimaModel();model.setGridCounter(1);
        ShimaModel model3 = new ShimaModel();model1.setGridCounter(2);
        ShimaModel model4 = new ShimaModel();model.setGridCounter(1);
        ShimaModel model5 = new ShimaModel();model1.setGridCounter(2);
        ShimaModel model6 = new ShimaModel();model1.setGridCounter(2);
        shimaModelArrayList.add(model);shimaModelArrayList.add(model1);shimaModelArrayList.add(model2);
        shimaModelArrayList.add(model3);shimaModelArrayList.add(model4);shimaModelArrayList.add(model5);shimaModelArrayList.add(model6);
        adaapter = new ShimaGridAdapter(shimaModelArrayList);
        if (binding.RecyclerShima !=null){
            binding.RecyclerShima.setItemAnimator(new DefaultItemAnimator());
            binding.RecyclerShima.setLayoutManager(new GridLayoutManager(getContext(),2));
            binding.RecyclerShima.setAdapter(adaapter);
        }
        if (binding.mingleGrid !=null){
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
            binding.mingleGrid.setLayoutManager(layoutManager);
            binding.mingleGrid.setItemAnimator(new DefaultItemAnimator());
        }


        ////Tablet Qualifies ////////////
        //Data//
        if (binding.mingleGridTab !=null){
            binding.mingleGridTab.setAdapter(adapter);
            binding.mingleGridTab.setItemAnimator(new DefaultItemAnimator());
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
            binding.mingleGridTab.setLayoutManager(gridLayoutManager);
        }

        //Shimmer//
        if (binding.RecyclerShimaTab !=null){
            GridLayoutManager gridLayoutManagerr = new GridLayoutManager(getContext(),3);
            binding.RecyclerShimaTab.setAdapter(adaapter);
            binding.RecyclerShimaTab.setItemAnimator(new DefaultItemAnimator());
            binding.RecyclerShimaTab.setLayoutManager(gridLayoutManagerr);
        }


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = reference.orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()){
                    String country = data.child("country").getValue(String.class);
                    String province = data.child("province").getValue(String.class);
                    String city = data.child("city").getValue(String.class);
                    String town = data.child("town").getValue(String.class);
                    String preferredAgeMin = data.child("preferredAgeMin").getValue(String.class);
                    String preferredAgeMax = data.child("preferredAgeMax").getValue(String.class);
                    String preferredGender = data.child("preGenderCode").getValue(String.class);


                    /////////////////////////////////////Location///////////////////////////

                    adapter = new MingleRecyclerAdapter(datadata,getContext());
                    if (binding.mingleGrid !=null){
                        binding.mingleGrid.setAdapter(adapter);
                    }


                    searchByCountry(country,town,city,province,preferredGender,preferredAgeMin,preferredAgeMax);




                }

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







    @SuppressLint("SetTextI18n")
    private void searchByCountry(String country,String town,String city,String province,String genderCode,String minAge,String maxAge){

        ErrorMessagesNoUsersBinding usersBinding = ErrorMessagesNoUsersBinding.inflate(getLayoutInflater());
        BottomSheetDialog noUsers = new BottomSheetDialog(getContext());
        ErrorMessagesNoUsersFoundTownBinding foundTownBinding = ErrorMessagesNoUsersFoundTownBinding.inflate(getLayoutInflater());
        BottomSheetDialog NoInTown = new BottomSheetDialog(getContext());
        int max = Integer.parseInt(maxAge);
        binding.searchFocus.setText(getString(R.string.searching_in) +" " + town);
        Query query = databaseReference.orderByChild("town").equalTo(town);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    datadata.clear();
                }
                for (DataSnapshot userSnap : snapshot.getChildren()){
                    MingleModel user = userSnap.getValue(MingleModel.class);
                    assert user != null;
                    int age = Integer.parseInt(user.getAge());
                    if (max>age&& Objects.equals(genderCode, user.getGenderCode()) && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUser_id())){

                        datadata.add(user);


                    }if (!snapshot.exists()){
                        binding.searchFocus.setText(getString(R.string.searching_in) +" " + city);
                        Query query = databaseReference.orderByChild("city").equalTo(city);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    datadata.clear();
                                }
                                for (DataSnapshot userCity : snapshot.getChildren()){
                                    MingleModel user = userCity.getValue(MingleModel.class);
                                    int age = Integer.parseInt(user.getAge());
                                    assert user != null;
                                    if (max>age&& Objects.equals(genderCode, user.getGenderCode()) && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUser_id())){
                                        if (snapshot.exists()){
                                            datadata.clear();
                                        }
                                    }if (!snapshot.exists()){
                                        binding.searchFocus.setText(getString(R.string.searching_in) +" " + province);
                                        Query query = databaseReference.orderByChild("province").equalTo(province);
                                        query.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()){
                                                    datadata.clear();
                                                }
                                                for (DataSnapshot snapProvince : snapshot.getChildren()){
                                                    MingleModel user = snapProvince.getValue(MingleModel.class);
                                                    int age = Integer.parseInt(user.getAge());
                                                    assert user != null;
                                                    if (max>age&& Objects.equals(genderCode, user.getGenderCode()) && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUser_id())){
                                                        datadata.add(user);


                                                    }if (!snapshot.exists()){
                                                        binding.searchFocus.setText(getString(R.string.searching_in) +" " + country);
                                                        Query query = databaseReference.orderByChild("country").equalTo(country);
                                                        query.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if (snapshot.exists()){
                                                                    datadata.clear();
                                                                }

                                                                for (DataSnapshot snapCountry : snapshot.getChildren()){
                                                                    MingleModel user = snapCountry.getValue(MingleModel.class);
                                                                    int age = Integer.parseInt(user.getAge());
                                                                    assert user != null;
                                                                    if (max>age&& Objects.equals(genderCode, user.getGenderCode()) && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUser_id())){
                                                                        datadata.add(user);


                                                                    }if (!snapshot.exists()){
                                                                        databaseReference.onDisconnect();
                                                                        noUsers.dismiss();
                                                                        noUsers.setContentView(usersBinding.getRoot());
                                                                        noUsers.setCancelable(true);
                                                                        usersBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) +" " + country+". "+
                                                                                getString(R.string.try_global_instead));
                                                                        usersBinding.Ok.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                noUsers.dismiss();
                                                                            }
                                                                        });
                                                                        if (!noUsers.isShowing()){
                                                                            noUsers.show();
                                                                        }

                                                                        binding.searchStatus.setVisibility(View.GONE);
                                                                        binding.mingleShimma.stopShimmer();
                                                                        binding.mingleShimma.setVisibility(View.GONE);
                                                                        binding.noDataInLocation.setVisibility(View.VISIBLE);

                                                                    }else if (snapshot.exists()){
                                                                        noUsers.dismiss();
                                                                        databaseReference.onDisconnect();
                                                                        noUsers.setContentView(usersBinding.getRoot());
                                                                        noUsers.setCancelable(true);
                                                                        usersBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) +" " + country+". "+
                                                                                getString(R.string.this_is_as_far_as_i_go) +", "+getString(R.string.try_global_instead));
                                                                        usersBinding.Ok.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                noUsers.dismiss();
                                                                            }
                                                                        });
                                                                        if (!noUsers.isShowing()){
                                                                            noUsers.show();
                                                                        }
                                                                        binding.searchStatus.setVisibility(View.GONE);
                                                                        binding.mingleShimma.stopShimmer();
                                                                        binding.mingleShimma.setVisibility(View.GONE);
                                                                        binding.noDataInLocation.setVisibility(View.GONE);


                                                                    }

                                                                }
                                                                adapter.notifyDataSetChanged();

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });

                                                    }if (snapshot.exists()) {
                                                        NoInTown.dismiss();
                                                        NoInTown.setCancelable(true);
                                                        NoInTown.setContentView(foundTownBinding.getRoot());
                                                        foundTownBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) + " "+ province+". "+
                                                                getString(R.string.do_you_want_to_search_futher) +" "+ country +"?");
                                                        foundTownBinding.no.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                NoInTown.dismiss();
                                                            }
                                                        });
                                                        foundTownBinding.yes.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                NoInTown.dismiss();
                                                                datadata.clear();
                                                                binding.searchStatus.setVisibility(View.VISIBLE);
                                                                binding.mingleShimma.startShimmer();
                                                                binding.mingleShimma.setVisibility(View.VISIBLE);
                                                                binding.noDataInLocation.setVisibility(View.GONE);
                                                                binding.searchFocus.setText(getString(R.string.searching_in) +" " + country);
                                                                Query query = databaseReference.orderByChild("country").equalTo(country);
                                                                query.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        if (snapshot.exists()){
                                                                            datadata.clear();
                                                                        }
                                                                        for (DataSnapshot snapCountry : snapshot.getChildren()){
                                                                            MingleModel user = snapCountry.getValue(MingleModel.class);
                                                                            int age = Integer.parseInt(user.getAge());
                                                                            assert user != null;
                                                                            if (max>age&& Objects.equals(genderCode, user.getGenderCode()) && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUser_id())){
                                                                                datadata.add(user);


                                                                            }
                                                                            if (!snapshot.exists()){
                                                                                noUsers.dismiss();
                                                                                noUsers.setContentView(usersBinding.getRoot());
                                                                                noUsers.setCancelable(true);
                                                                                usersBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in)+" " + country+". "+
                                                                                        getString(R.string.try_global_instead));
                                                                                usersBinding.Ok.setOnClickListener(new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {
                                                                                        noUsers.dismiss();
                                                                                    }
                                                                                });
                                                                                if (!noUsers.isShowing()){
                                                                                    noUsers.show();
                                                                                }
                                                                                binding.searchStatus.setVisibility(View.GONE);
                                                                                binding.mingleShimma.stopShimmer();
                                                                                binding.mingleShimma.setVisibility(View.GONE);
                                                                                binding.noDataInLocation.setVisibility(View.VISIBLE);

                                                                            }if (snapshot.exists()){
                                                                                noUsers.dismiss();
                                                                                noUsers.setContentView(usersBinding.getRoot());
                                                                                noUsers.setCancelable(true);
                                                                                usersBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) +" "+ country+". "+
                                                                                        getString(R.string.this_is_as_far_as_i_go) +", "+getString(R.string.try_global_instead));
                                                                                usersBinding.Ok.setOnClickListener(new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {
                                                                                        noUsers.dismiss();
                                                                                    }
                                                                                });
                                                                                if (!noUsers.isShowing()){
                                                                                    noUsers.show();
                                                                                }
                                                                                binding.searchStatus.setVisibility(View.GONE);
                                                                                binding.mingleShimma.stopShimmer();
                                                                                binding.mingleShimma.setVisibility(View.GONE);
                                                                                binding.noDataInLocation.setVisibility(View.GONE);


                                                                            }

                                                                        }
                                                                        adapter.notifyDataSetChanged();

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });
                                                            }
                                                        });
                                                        NoInTown.show();


                                                        binding.searchStatus.setVisibility(View.GONE);
                                                        binding.mingleShimma.stopShimmer();
                                                        binding.mingleShimma.setVisibility(View.GONE);
                                                        binding.noDataInLocation.setVisibility(View.GONE);

                                                    }

                                                }
                                                adapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }else if (snapshot.exists()){
                                        NoInTown.dismiss();
                                        NoInTown.setCancelable(true);
                                        NoInTown.setContentView(foundTownBinding.getRoot());
                                        foundTownBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) + " "+ city+". "+
                                                getString(R.string.do_you_want_to_search_futher) +" " + province +"?");
                                        foundTownBinding.no.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                NoInTown.dismiss();
                                            }
                                        });
                                        foundTownBinding.yes.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                NoInTown.dismiss();
                                                datadata.clear();
                                                binding.searchStatus.setVisibility(View.VISIBLE);
                                                binding.mingleShimma.startShimmer();
                                                binding.mingleShimma.setVisibility(View.VISIBLE);
                                                binding.noDataInLocation.setVisibility(View.VISIBLE);
                                                binding.searchFocus.setText(getString(R.string.searching_in) +" " + province);
                                                Query query = databaseReference.orderByChild("province").equalTo(province);
                                                query.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()){
                                                            datadata.clear();
                                                        }
                                                        for (DataSnapshot snapProvince : snapshot.getChildren()){
                                                            MingleModel user = snapProvince.getValue(MingleModel.class);
                                                            int age = Integer.parseInt(user.getAge());
                                                            assert user != null;
                                                            if (max>age&& Objects.equals(genderCode, user.getGenderCode()) && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUser_id())){
                                                                datadata.add(user);


                                                            }if (!snapshot.exists()){
                                                                binding.searchFocus.setText(getString(R.string.searching_in) +" " + country);
                                                                Query query = databaseReference.orderByChild("country").equalTo(country);
                                                                query.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        if (snapshot.exists()){
                                                                            datadata.clear();
                                                                        }
                                                                        for (DataSnapshot snapCountry : snapshot.getChildren()){
                                                                            MingleModel user = snapCountry.getValue(MingleModel.class);
                                                                            int age = Integer.parseInt(user.getAge());
                                                                            assert user != null;
                                                                            if (max>age&& Objects.equals(genderCode, user.getGenderCode()) && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUser_id())){
                                                                                datadata.add(user);


                                                                            }if (!snapshot.exists()){
                                                                                noUsers.dismiss();
                                                                                noUsers.setContentView(usersBinding.getRoot());
                                                                                noUsers.setCancelable(true);
                                                                                usersBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) + " "+ country+". "+
                                                                                        getString(R.string.try_global_instead));
                                                                                usersBinding.Ok.setOnClickListener(new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {
                                                                                        noUsers.dismiss();
                                                                                    }
                                                                                });
                                                                                noUsers.show();
                                                                                binding.searchStatus.setVisibility(View.GONE);
                                                                                binding.mingleShimma.stopShimmer();
                                                                                binding.mingleShimma.setVisibility(View.GONE);
                                                                                binding.noDataInLocation.setVisibility(View.VISIBLE);

                                                                            }else if (snapshot.exists()) {
                                                                                noUsers.dismiss();
                                                                                noUsers.setContentView(usersBinding.getRoot());
                                                                                noUsers.setCancelable(true);
                                                                                usersBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) + " "+ country+". "+
                                                                                        getString(R.string.this_is_as_far_as_i_go) +", "+getString(R.string.try_global_instead));
                                                                                usersBinding.Ok.setOnClickListener(new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {
                                                                                        noUsers.dismiss();
                                                                                    }
                                                                                });
                                                                                if (!noUsers.isShowing()){
                                                                                    noUsers.show();
                                                                                }
                                                                                binding.searchStatus.setVisibility(View.GONE);
                                                                                binding.mingleShimma.stopShimmer();
                                                                                binding.mingleShimma.setVisibility(View.GONE);
                                                                                binding.noDataInLocation.setVisibility(View.GONE);

                                                                            }

                                                                        }
                                                                        adapter.notifyDataSetChanged();

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });

                                                            }else if (snapshot.exists()) {
                                                                NoInTown.dismiss();
                                                                NoInTown.setCancelable(true);
                                                                NoInTown.setContentView(foundTownBinding.getRoot());
                                                                foundTownBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) + " "+ province+". "+
                                                                        getString(R.string.do_you_want_to_search_futher) + " "+ country +"?");
                                                                foundTownBinding.no.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        NoInTown.dismiss();
                                                                    }
                                                                });
                                                                foundTownBinding.yes.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        NoInTown.dismiss();
                                                                        datadata.clear();
                                                                        binding.searchStatus.setVisibility(View.VISIBLE);
                                                                        binding.mingleShimma.startShimmer();
                                                                        binding.mingleShimma.setVisibility(View.VISIBLE);
                                                                        binding.noDataInLocation.setVisibility(View.GONE);
                                                                        binding.searchFocus.setText(getString(R.string.searching_in) +" " + country);
                                                                        Query query = databaseReference.orderByChild("country").equalTo(country);
                                                                        query.addValueEventListener(new ValueEventListener() {
                                                                            @SuppressLint("SetTextI18n")
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                if (snapshot.exists()){
                                                                                    datadata.clear();
                                                                                }
                                                                                for (DataSnapshot snapCountry : snapshot.getChildren()){
                                                                                    MingleModel user = snapCountry.getValue(MingleModel.class);
                                                                                    int age = Integer.parseInt(user.getAge());
                                                                                    assert user != null;
                                                                                    if (max>age&& Objects.equals(genderCode, user.getGenderCode()) && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUser_id())){
                                                                                        datadata.add(user);


                                                                                    }if (!snapshot.exists()){
                                                                                        noUsers.dismiss();
                                                                                        noUsers.setContentView(usersBinding.getRoot());
                                                                                        noUsers.setCancelable(true);
                                                                                        usersBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) + " "+ country+". "+
                                                                                                getString(R.string.try_global_instead));
                                                                                        usersBinding.Ok.setOnClickListener(new View.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(View v) {
                                                                                                noUsers.dismiss();
                                                                                            }
                                                                                        });
                                                                                        if (!noUsers.isShowing()){
                                                                                            noUsers.show();
                                                                                        }
                                                                                        binding.searchStatus.setVisibility(View.GONE);
                                                                                        binding.mingleShimma.stopShimmer();
                                                                                        binding.mingleShimma.setVisibility(View.GONE);
                                                                                        binding.noDataInLocation.setVisibility(View.VISIBLE);

                                                                                    }else if (snapshot.exists()){
                                                                                        noUsers.dismiss();
                                                                                        noUsers.setContentView(usersBinding.getRoot());
                                                                                        noUsers.setCancelable(true);
                                                                                        usersBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) + " "+ country+". "+
                                                                                                getString(R.string.this_is_as_far_as_i_go) +", "+getString(R.string.try_global_instead));
                                                                                        usersBinding.Ok.setOnClickListener(new View.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(View v) {
                                                                                                noUsers.dismiss();
                                                                                            }
                                                                                        });
                                                                                        if (!noUsers.isShowing()){
                                                                                            noUsers.show();
                                                                                        }
                                                                                        binding.searchStatus.setVisibility(View.GONE);
                                                                                        binding.mingleShimma.stopShimmer();
                                                                                        binding.mingleShimma.setVisibility(View.GONE);
                                                                                        binding.noDataInLocation.setVisibility(View.GONE);


                                                                                    }

                                                                                }
                                                                                adapter.notifyDataSetChanged();

                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                                NoInTown.show();


                                                                binding.searchStatus.setVisibility(View.GONE);
                                                                binding.mingleShimma.stopShimmer();
                                                                binding.mingleShimma.setVisibility(View.GONE);
                                                                binding.noDataInLocation.setVisibility(View.GONE);

                                                            }

                                                        }
                                                        adapter.notifyDataSetChanged();
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                            }
                                        });

                                        NoInTown.show();
                                        binding.searchStatus.setVisibility(View.GONE);
                                        binding.mingleShimma.stopShimmer();
                                        binding.mingleShimma.setVisibility(View.GONE);
                                        binding.noDataInLocation.setVisibility(View.GONE);

                                    }

                                }
                                adapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                    }else if (snapshot.exists()){
                        NoInTown.dismiss();
                        NoInTown.setCancelable(true);
                        NoInTown.setContentView(foundTownBinding.getRoot());
                        foundTownBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) +" " + town+". "+
                                getString(R.string.do_you_want_to_search_futher) +" "+ city +"?");
                        foundTownBinding.no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                NoInTown.dismiss();
                            }
                        });
                        NoInTown.show();
                        foundTownBinding.yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                datadata.clear();
                                NoInTown.dismiss();

                                binding.searchStatus.setVisibility(View.VISIBLE);
                                binding.mingleShimma.startShimmer();
                                binding.mingleShimma.setVisibility(View.VISIBLE);
                                binding.noDataInLocation.setVisibility(View.GONE);
                                binding.searchFocus.setText(getString(R.string.searching_in) +" " + city);
                                Query query = databaseReference.orderByChild("city").equalTo(city);
                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            datadata.clear();
                                        }
                                        for (DataSnapshot userCity : snapshot.getChildren()){
                                            MingleModel user = userCity.getValue(MingleModel.class);
                                            int age = Integer.parseInt(user.getAge());
                                            assert user != null;
                                            if (max>age&& Objects.equals(genderCode, user.getGenderCode()) && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUser_id())){
                                                datadata.add(user);


                                            }if (!snapshot.exists()){
                                                binding.searchFocus.setText(getString(R.string.searching_in) +" " + province);
                                                Query query = databaseReference.orderByChild("province").equalTo(province);
                                                query.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        datadata.clear();
                                                        for (DataSnapshot snapProvince : snapshot.getChildren()){
                                                            MingleModel user = snapProvince.getValue(MingleModel.class);
                                                            int age = Integer.parseInt(user.getAge());
                                                            assert user != null;
                                                            if (max>age&& Objects.equals(genderCode, user.getGenderCode()) && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUser_id())){
                                                                datadata.add(user);


                                                            }if (!snapshot.exists()){
                                                                binding.searchFocus.setText(getString(R.string.searching_in) +" " + country);
                                                                Query query = databaseReference.orderByChild("country").equalTo(country);
                                                                query.addValueEventListener(new ValueEventListener() {
                                                                    @SuppressLint("SetTextI18n")
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        datadata.clear();
                                                                        for (DataSnapshot snapCountry : snapshot.getChildren()){
                                                                            MingleModel user = snapCountry.getValue(MingleModel.class);
                                                                            int age = Integer.parseInt(user.getAge());
                                                                            assert user != null;
                                                                            if (max>age&& Objects.equals(genderCode, user.getGenderCode()) && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUser_id())){
                                                                                datadata.add(user);


                                                                            }if (!snapshot.exists()){
                                                                                noUsers.dismiss();
                                                                                noUsers.setContentView(usersBinding.getRoot());
                                                                                noUsers.setCancelable(true);
                                                                                usersBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) + " "+ country+". "+
                                                                                        getString(R.string.try_global_instead));
                                                                                usersBinding.Ok.setOnClickListener(new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {
                                                                                        noUsers.dismiss();
                                                                                    }
                                                                                });
                                                                                if (!noUsers.isShowing()){
                                                                                    noUsers.show();
                                                                                }
                                                                                binding.searchStatus.setVisibility(View.GONE);
                                                                                binding.mingleShimma.stopShimmer();
                                                                                binding.mingleShimma.setVisibility(View.GONE);
                                                                                binding.noDataInLocation.setVisibility(View.VISIBLE);

                                                                            }else if (snapshot.exists()) {
                                                                                noUsers.dismiss();
                                                                                noUsers.setContentView(usersBinding.getRoot());
                                                                                noUsers.setCancelable(true);
                                                                                usersBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) + " "+ country+". "+
                                                                                        getString(R.string.this_is_as_far_as_i_go) +", "+getString(R.string.try_global_instead));
                                                                                usersBinding.Ok.setOnClickListener(new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {
                                                                                        noUsers.dismiss();
                                                                                    }
                                                                                });
                                                                                if (!noUsers.isShowing()){
                                                                                    noUsers.show();
                                                                                }
                                                                                binding.searchStatus.setVisibility(View.GONE);
                                                                                binding.mingleShimma.stopShimmer();
                                                                                binding.mingleShimma.setVisibility(View.GONE);
                                                                                binding.noDataInLocation.setVisibility(View.GONE);
                                                                            }

                                                                        }
                                                                        adapter.notifyDataSetChanged();

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });

                                                            }else if (adapter.getItemCount() >0) {
                                                                NoInTown.dismiss();
                                                                NoInTown.setCancelable(true);
                                                                NoInTown.setContentView(foundTownBinding.getRoot());
                                                                foundTownBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) + " "+ province+". "+
                                                                        getString(R.string.do_you_want_to_search_futher) + " "+ country +"?");
                                                                foundTownBinding.no.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        NoInTown.dismiss();
                                                                    }
                                                                });
                                                                foundTownBinding.yes.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        NoInTown.dismiss();
                                                                        datadata.clear();
                                                                        binding.searchStatus.setVisibility(View.VISIBLE);
                                                                        binding.mingleShimma.startShimmer();
                                                                        binding.mingleShimma.setVisibility(View.VISIBLE);
                                                                        binding.noDataInLocation.setVisibility(View.VISIBLE);
                                                                        binding.searchFocus.setText(getString(R.string.searching_in) +" " + country);
                                                                        Query query = databaseReference.orderByChild("country").equalTo(country);
                                                                        query.addValueEventListener(new ValueEventListener() {
                                                                            @SuppressLint("SetTextI18n")
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                datadata.clear();
                                                                                for (DataSnapshot snapCountry : snapshot.getChildren()){
                                                                                    MingleModel user = snapCountry.getValue(MingleModel.class);
                                                                                    int age = Integer.parseInt(user.getAge());
                                                                                    assert user != null;
                                                                                    if (max>age&& Objects.equals(genderCode, user.getGenderCode()) && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUser_id())){
                                                                                        datadata.add(user);


                                                                                    }if (!snapshot.exists()){
                                                                                        noUsers.dismiss();
                                                                                        noUsers.setContentView(usersBinding.getRoot());
                                                                                        noUsers.setCancelable(true);
                                                                                        usersBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) + " "+ country+". "+
                                                                                                getString(R.string.try_global_instead));
                                                                                        usersBinding.Ok.setOnClickListener(new View.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(View v) {
                                                                                                noUsers.dismiss();
                                                                                            }
                                                                                        });
                                                                                        noUsers.show();
                                                                                        binding.searchStatus.setVisibility(View.GONE);
                                                                                        binding.mingleShimma.stopShimmer();
                                                                                        binding.mingleShimma.setVisibility(View.GONE);
                                                                                        binding.noDataInLocation.setVisibility(View.VISIBLE);

                                                                                    }else if (snapshot.exists()){
                                                                                        noUsers.dismiss();
                                                                                        noUsers.setContentView(usersBinding.getRoot());
                                                                                        noUsers.setCancelable(true);
                                                                                        usersBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) + " "+ country+". "+
                                                                                                getString(R.string.this_is_as_far_as_i_go) +", "+getString(R.string.try_global_instead));
                                                                                        usersBinding.Ok.setOnClickListener(new View.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(View v) {
                                                                                                noUsers.dismiss();
                                                                                            }
                                                                                        });
                                                                                        if (!noUsers.isShowing()){
                                                                                            noUsers.show();
                                                                                        }
                                                                                        binding.searchStatus.setVisibility(View.GONE);
                                                                                        binding.mingleShimma.stopShimmer();
                                                                                        binding.mingleShimma.setVisibility(View.GONE);
                                                                                        binding.noDataInLocation.setVisibility(View.GONE);


                                                                                    }

                                                                                }
                                                                                adapter.notifyDataSetChanged();

                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                                NoInTown.show();


                                                                binding.searchStatus.setVisibility(View.GONE);
                                                                binding.mingleShimma.stopShimmer();
                                                                binding.mingleShimma.setVisibility(View.GONE);
                                                                binding.noDataInLocation.setVisibility(View.GONE);

                                                            }

                                                        }
                                                        adapter.notifyDataSetChanged();
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });


                                            }else if (snapshot.exists()){
                                                NoInTown.dismiss();
                                                NoInTown.setCancelable(true);
                                                NoInTown.setContentView(foundTownBinding.getRoot());
                                                foundTownBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) +" "+ city+". "+
                                                        getString(R.string.do_you_want_to_search_futher) +" "+ province +"?");
                                                foundTownBinding.no.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        NoInTown.dismiss();
                                                    }
                                                });
                                                foundTownBinding.yes.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        NoInTown.dismiss();
                                                        datadata.clear();
                                                        binding.searchStatus.setVisibility(View.VISIBLE);
                                                        binding.mingleShimma.startShimmer();
                                                        binding.mingleShimma.setVisibility(View.VISIBLE);
                                                        binding.noDataInLocation.setVisibility(View.GONE);
                                                        binding.searchFocus.setText(getString(R.string.searching_in) +" " + province);
                                                        Query query = databaseReference.orderByChild("province").equalTo(province);
                                                        query.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                datadata.clear();
                                                                for (DataSnapshot snapProvince : snapshot.getChildren()){
                                                                    MingleModel user = snapProvince.getValue(MingleModel.class);
                                                                    int age = Integer.parseInt(user.getAge());
                                                                    assert user != null;
                                                                    if (max>age&& Objects.equals(genderCode, user.getGenderCode()) && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUser_id())){
                                                                        datadata.add(user);


                                                                    }if (!snapshot.exists()){
                                                                        binding.searchFocus.setText(getString(R.string.searching_in) +" " + country);
                                                                        Query query = databaseReference.orderByChild("country").equalTo(country);
                                                                        query.addValueEventListener(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                datadata.clear();
                                                                                for (DataSnapshot snapCountry : snapshot.getChildren()){
                                                                                    MingleModel user = snapCountry.getValue(MingleModel.class);
                                                                                    int age = Integer.parseInt(user.getAge());
                                                                                    assert user != null;
                                                                                    if (max>age&& Objects.equals(genderCode, user.getGenderCode()) && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUser_id())){
                                                                                        datadata.add(user);


                                                                                    }if (!snapshot.exists()){
                                                                                        noUsers.dismiss();
                                                                                        noUsers.setContentView(usersBinding.getRoot());
                                                                                        noUsers.setCancelable(true);
                                                                                        usersBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) + " " + country+". "+
                                                                                                getString(R.string.try_global_instead));
                                                                                        usersBinding.Ok.setOnClickListener(new View.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(View v) {
                                                                                                noUsers.dismiss();
                                                                                            }
                                                                                        });
                                                                                        noUsers.show();
                                                                                        binding.searchStatus.setVisibility(View.GONE);
                                                                                        binding.mingleShimma.stopShimmer();
                                                                                        binding.mingleShimma.setVisibility(View.GONE);
                                                                                        binding.noDataInLocation.setVisibility(View.VISIBLE);

                                                                                    }else if (snapshot.exists()) {
                                                                                        noUsers.dismiss();
                                                                                        noUsers.setContentView(usersBinding.getRoot());
                                                                                        noUsers.setCancelable(true);
                                                                                        usersBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) +" "+ country+". "+
                                                                                                getString(R.string.this_is_as_far_as_i_go) +", "+getString(R.string.try_global_instead));
                                                                                        usersBinding.Ok.setOnClickListener(new View.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(View v) {
                                                                                                noUsers.dismiss();
                                                                                            }
                                                                                        });
                                                                                        if (!noUsers.isShowing()){
                                                                                            noUsers.show();
                                                                                        }
                                                                                        binding.searchStatus.setVisibility(View.GONE);
                                                                                        binding.mingleShimma.stopShimmer();
                                                                                        binding.mingleShimma.setVisibility(View.GONE);
                                                                                        binding.noDataInLocation.setVisibility(View.GONE);

                                                                                    }

                                                                                }
                                                                                adapter.notifyDataSetChanged();

                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                            }
                                                                        });

                                                                    }else if (snapshot.exists()) {
                                                                        NoInTown.dismiss();
                                                                        NoInTown.setCancelable(true);
                                                                        NoInTown.setContentView(foundTownBinding.getRoot());
                                                                        foundTownBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) +" "+ province+". "+
                                                                                getString(R.string.do_you_want_to_search_futher) + " "+ country +"?");
                                                                        foundTownBinding.no.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                NoInTown.dismiss();
                                                                            }
                                                                        });
                                                                        foundTownBinding.yes.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                NoInTown.dismiss();
                                                                                datadata.clear();
                                                                                binding.searchStatus.setVisibility(View.VISIBLE);
                                                                                binding.mingleShimma.startShimmer();
                                                                                binding.mingleShimma.setVisibility(View.VISIBLE);
                                                                                binding.noDataInLocation.setVisibility(View.GONE);
                                                                                binding.searchFocus.setText(getString(R.string.searching_in) +" " + country);
                                                                                Query query = databaseReference.orderByChild("country").equalTo(country);
                                                                                query.addValueEventListener(new ValueEventListener() {
                                                                                    @SuppressLint("SetTextI18n")
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                        datadata.clear();
                                                                                        for (DataSnapshot snapCountry : snapshot.getChildren()){
                                                                                            MingleModel user = snapCountry.getValue(MingleModel.class);
                                                                                            int age = Integer.parseInt(user.getAge());
                                                                                            assert user != null;
                                                                                            if (max>age&& Objects.equals(genderCode, user.getGenderCode()) && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUser_id())){
                                                                                                datadata.add(user);


                                                                                            }if (!snapshot.exists()){
                                                                                                noUsers.dismiss();
                                                                                                noUsers.setContentView(usersBinding.getRoot());
                                                                                                noUsers.setCancelable(true);
                                                                                                usersBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) +" " + country+". "+
                                                                                                        getString(R.string.try_global_instead));
                                                                                                usersBinding.Ok.setOnClickListener(new View.OnClickListener() {
                                                                                                    @Override
                                                                                                    public void onClick(View v) {
                                                                                                        noUsers.dismiss();
                                                                                                    }
                                                                                                });
                                                                                                if (!noUsers.isShowing()){
                                                                                                    noUsers.show();
                                                                                                }


                                                                                                binding.searchStatus.setVisibility(View.GONE);
                                                                                                binding.mingleShimma.stopShimmer();
                                                                                                binding.mingleShimma.setVisibility(View.GONE);
                                                                                                binding.noDataInLocation.setVisibility(View.VISIBLE);

                                                                                            }else if (snapshot.exists()){
                                                                                                noUsers.dismiss();
                                                                                                noUsers.setContentView(usersBinding.getRoot());
                                                                                                noUsers.setCancelable(true);
                                                                                                usersBinding.Msg.setText(adapter.getItemCount() +" " + getString(R.string.users_found_in) +" "+ country+". "+
                                                                                                        getString(R.string.this_is_as_far_as_i_go) +", "+getString(R.string.try_global_instead));
                                                                                                usersBinding.Ok.setOnClickListener(new View.OnClickListener() {
                                                                                                    @Override
                                                                                                    public void onClick(View v) {
                                                                                                        noUsers.dismiss();
                                                                                                    }
                                                                                                });
                                                                                                if (!noUsers.isShowing()){
                                                                                                    noUsers.show();
                                                                                                }
                                                                                                binding.searchStatus.setVisibility(View.GONE);
                                                                                                binding.mingleShimma.stopShimmer();
                                                                                                binding.mingleShimma.setVisibility(View.GONE);
                                                                                                binding.noDataInLocation.setVisibility(View.GONE);

                                                                                            }

                                                                                        }
                                                                                        adapter.notifyDataSetChanged();

                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                                    }
                                                                                });

                                                                            }
                                                                        });
                                                                        NoInTown.show();


                                                                        binding.searchStatus.setVisibility(View.GONE);
                                                                        binding.mingleShimma.stopShimmer();
                                                                        binding.mingleShimma.setVisibility(View.GONE);
                                                                        binding.noDataInLocation.setVisibility(View.GONE);

                                                                    }

                                                                }
                                                                adapter.notifyDataSetChanged();
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });

                                                    }
                                                });
                                                NoInTown.show();
                                                binding.searchStatus.setVisibility(View.GONE);
                                                binding.mingleShimma.stopShimmer();
                                                binding.mingleShimma.setVisibility(View.GONE);
                                                binding.noDataInLocation.setVisibility(View.GONE);
                                            }

                                        }
                                        adapter.notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });

                        binding.searchStatus.setVisibility(View.GONE);
                        binding.mingleShimma.stopShimmer();
                        binding.mingleShimma.setVisibility(View.GONE);
                        binding.noDataInLocation.setVisibility(View.GONE);
                    }
                }
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }




}