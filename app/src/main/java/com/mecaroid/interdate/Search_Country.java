package com.mecaroid.interdate;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mecaroid.interdate.Adapters.Recycler.MingleRecyclerAdapter;
import com.mecaroid.interdate.Models.MingleModel;
import com.mecaroid.interdate.databinding.ActivitySearchCountryBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

public class Search_Country extends AppCompatActivity {
    ActivitySearchCountryBinding binding;
    ArrayList<MingleModel> data;
    MingleRecyclerAdapter adapter;

    private  String currentTown;
    private  String currentCity;
    private  String currentProvince;
    private  String currentCountry;
    String proTown,proCity,proProvince,proCountry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchCountryBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        showAdBanner();
        data = new ArrayList<>();
        adapter = new MingleRecyclerAdapter(data,getApplicationContext());
        binding.recycler.setItemAnimator(new DefaultItemAnimator());
        binding.recycler.setLayoutManager(new GridLayoutManager(this,2));
        binding.recycler.setAdapter(adapter);
        binding.query.setMaxLines(1);
        getLocation();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("preGenderCode");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String preGenderCode = snapshot.getValue(String.class);
                binding.search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!binding.checkboxtown.isChecked() && !binding.checklocation.isChecked() &&
                                !binding.checkunivcol.isChecked() && !binding.checkboxcity.isChecked() &&
                                !binding.checkboxstate.isChecked() && !binding.checkboxcountry.isChecked()){
                            Toast.makeText(Search_Country.this, getString(R.string.please_check_your_search_target), Toast.LENGTH_SHORT).show();
                        }
                        if (binding.query.getText().toString().isEmpty() && !binding.checklocation.isChecked()){
                            Toast.makeText(Search_Country.this, getString(R.string.where_are_you_looking), Toast.LENGTH_SHORT).show();
                        }else if (binding.checkboxcountry.isChecked()){
                            searchCountry(preGenderCode);
                        }else if (binding.checkboxstate.isChecked()){
                            searchState(preGenderCode);
                        } else if (binding.checkboxcity.isChecked()) {
                            searchCity(preGenderCode);
                        } else if (binding.checkboxtown.isChecked()) {
                            searchTown(preGenderCode);
                        } else if (binding.checkunivcol.isChecked()){
                            searchUniversityOrCollege(preGenderCode);
                        }

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.checkboxcountry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.checkboxstate.setChecked(false);
                    binding.checkboxtown.setChecked(false);
                    binding.checkboxcity.setChecked(false);
                    binding.checkunivcol.setChecked(false);
                    binding.checklocation.setChecked(false);
                }
            }
        });
        binding.checkboxtown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.checkboxstate.setChecked(false);
                    binding.checkboxcountry.setChecked(false);
                    binding.checkboxcity.setChecked(false);
                    binding.checkunivcol.setChecked(false);
                    binding.checklocation.setChecked(false);
                }
            }
        });
        binding.checkboxcity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.checkboxstate.setChecked(false);
                    binding.checkboxtown.setChecked(false);
                    binding.checkboxcountry.setChecked(false);
                    binding.checkunivcol.setChecked(false);
                    binding.checklocation.setChecked(false);
                }
            }
        });
        binding.checkboxstate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.checkboxcountry.setChecked(false);
                    binding.checkboxtown.setChecked(false);
                    binding.checkboxcity.setChecked(false);
                    binding.checkunivcol.setChecked(false);
                    binding.checklocation.setChecked(false);
                }
            }
        });
        binding.checklocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.checkboxcountry.setChecked(false);
                    binding.checkboxtown.setChecked(false);
                    binding.checkboxstate.setChecked(false);
                    binding.checkboxcity.setChecked(false);
                    binding.checkunivcol.setChecked(false);
                    binding.query.setEnabled(false);
                    binding.query.setText(currentTown);


                }else{
                    binding.query.setEnabled(true);
                    binding.query.setText("");
                }
            }
        });
        binding.checkunivcol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.checkboxcountry.setChecked(false);
                    binding.checkboxstate.setChecked(false);
                    binding.checkboxtown.setChecked(false);
                    binding.checkboxcity.setChecked(false);
                    binding.checklocation.setChecked(false);

                }
            }
        });


    }
    protected void showAdBanner(){
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
    }
    protected void searchCountry(String pre){
        binding.ProgressCard.setVisibility(View.VISIBLE);
        binding.checkScroll.setVisibility(View.GONE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = reference.orderByChild("country").equalTo(binding.query.getText().toString().trim());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                binding.ProgressCard.setVisibility(View.GONE);
                binding.checkScroll.setVisibility(View.VISIBLE);
                if (snapshot.exists()){
                    for (DataSnapshot date : snapshot.getChildren()){
                        MingleModel model = date.getValue(MingleModel.class);
                        if (Objects.equals(model.getGenderCode(), pre) && !Objects.equals(model.getUser_id(), FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            Collections.shuffle(data);
                            data.add(model);
                        }
                        snapshot.getRef().onDisconnect();


                    }
                }else{
                    binding.noDataInLocation.setVisibility(View.VISIBLE);
                    binding.errorImage.setImageDrawable(getResources().getDrawable(R.drawable.sentiment_dissatisfied));
                    binding.errorMsg.setText(getString(R.string.noUsers_withmatch));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    protected void searchUniversityOrCollege(String pre){
        binding.ProgressCard.setVisibility(View.VISIBLE);
        binding.checkScroll.setVisibility(View.GONE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = reference.orderByChild("studentAt").startAt(binding.query.getText().toString().trim()).endAt(binding.query.getText().toString().trim()  + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                binding.ProgressCard.setVisibility(View.GONE);
                binding.checkScroll.setVisibility(View.VISIBLE);
                if (snapshot.exists()){
                    for (DataSnapshot date : snapshot.getChildren()){
                        MingleModel model = date.getValue(MingleModel.class);
                        if (Objects.equals(model.getGenderCode(), pre) && !Objects.equals(model.getUser_id(), FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            Collections.shuffle(data);
                            data.add(model);
                        }
                    }
                }else{
                    binding.noDataInLocation.setVisibility(View.VISIBLE);
                    binding.errorImage.setImageDrawable(getResources().getDrawable(R.drawable.sentiment_dissatisfied));
                    binding.errorMsg.setText(getString(R.string.noUsers_withmatch));

                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    protected void searchState(String pre){

        binding.ProgressCard.setVisibility(View.VISIBLE);
        binding.checkScroll.setVisibility(View.GONE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = reference.orderByChild("province").equalTo(binding.query.getText().toString().trim());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                binding.ProgressCard.setVisibility(View.GONE);
                binding.checkScroll.setVisibility(View.VISIBLE);
                if (snapshot.exists()){
                    for (DataSnapshot date : snapshot.getChildren()){
                        MingleModel model = date.getValue(MingleModel.class);

                        if (model !=null){
                            Collections.shuffle(data);
                        }
                        if (Objects.equals(model.getGenderCode(), pre) && !Objects.equals(model.getUser_id(), FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            data.add(model);
                        }


                        snapshot.getRef().onDisconnect();


                    }

                }else{
                    binding.noDataInLocation.setVisibility(View.VISIBLE);
                    binding.errorImage.setImageDrawable(getResources().getDrawable(R.drawable.sentiment_dissatisfied));
                    binding.errorMsg.setText(getString(R.string.noUsers_withmatch));

                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    protected void searchCity(String pre){

        binding.ProgressCard.setVisibility(View.VISIBLE);
        binding.checkScroll.setVisibility(View.GONE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = reference.orderByChild("city").equalTo(binding.query.getText().toString().trim());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                binding.ProgressCard.setVisibility(View.GONE);
                binding.checkScroll.setVisibility(View.VISIBLE);
                if(snapshot.exists()){
                    for (DataSnapshot date : snapshot.getChildren()){
                        MingleModel model = date.getValue(MingleModel.class);

                        Collections.shuffle(data);
                        if (Objects.equals(model.getGenderCode(), pre) && !Objects.equals(model.getUser_id(), FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            data.add(model);
                        }
                        snapshot.getRef().onDisconnect();


                    }
                }else{
                    binding.noDataInLocation.setVisibility(View.VISIBLE);
                    binding.errorImage.setImageDrawable(getResources().getDrawable(R.drawable.sentiment_dissatisfied));
                    binding.errorMsg.setText(getString(R.string.noUsers_withmatch));

                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    protected void searchTown(String pre){
        binding.ProgressCard.setVisibility(View.VISIBLE);
        binding.checkScroll.setVisibility(View.GONE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = reference.orderByChild("town").equalTo(binding.query.getText().toString().trim());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                binding.ProgressCard.setVisibility(View.GONE);
                binding.checkScroll.setVisibility(View.VISIBLE);
                if (snapshot.exists()){
                    for (DataSnapshot date : snapshot.getChildren()){
                        MingleModel model = date.getValue(MingleModel.class);
                        Collections.shuffle(data);
                        if (Objects.equals(model.getGenderCode(), pre) && !Objects.equals(model.getUser_id(), FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            data.add(model);
                        }
                        snapshot.getRef().onDisconnect();


                    }

                }else {
                    binding.noDataInLocation.setVisibility(View.VISIBLE);
                    binding.errorImage.setImageDrawable(getResources().getDrawable(R.drawable.sentiment_dissatisfied));
                    binding.errorMsg.setText(getString(R.string.noUsers_withmatch));

                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    LocationManager locationManager;
    private void getLocation(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        int REQUEST_LOCATION = 100;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }else {
            Location locationGps;


            locationGps = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (locationGps !=null){
                double latitude = locationGps.getLatitude();
                double longitude = locationGps.getLongitude();
                Geocoder geocoder = new Geocoder(this);
                try {
                    Address addresses = Objects.requireNonNull(geocoder.getFromLocation(latitude, longitude, 1)).get(0);
                    if (addresses != null) {
                        binding.checklocation.setEnabled(true);
                        String county = addresses.getCountryName();
                        String province = addresses.getAdminArea();
                        String city = addresses.getSubAdminArea();
                        String town = addresses.getLocality();
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("preGenderCode");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String preGenderCode = snapshot.getValue(String.class);
                                binding.search.setOnClickListener(view->{
                                    if(binding.checklocation.isChecked()){
                                        getUserByCurrentLocation(county,province,city,town,preGenderCode);

                                    }

                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        // Use the location information as needed
                        // ...
                        // Example: Log the location details
                        String locationDetails = String.format(Locale.getDefault(), "County: %s, Province: %s, City: %s, Town: %s",
                                county, province, city, town);
                        Log.d("Location", locationDetails);
                    }else{
                        binding.checklocation.setEnabled(false);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    private void getUserByCurrentLocation(String country,String provinve,String city,String town,String preGenderCode){

        if (town !=null && !Objects.equals(getIntent().getStringExtra("town"), town)){
            getUserByCurrentTown(town,city,provinve,country,preGenderCode);

        }else if (city !=null && !Objects.equals(city,getIntent().getStringExtra("city"))){
            getUserByCurrentCity(city,provinve,country,preGenderCode);

        } else if (provinve !=null && !Objects.equals(provinve,getIntent().getStringExtra("province"))) {
            getUserByCurrentProvince(provinve,country,preGenderCode);
        } else if (country !=null && !Objects.equals(country,getIntent().getStringExtra("country"))) {
            getUserByCurrentCountry(country,preGenderCode);
        }
    }


    private void getUserByCurrentCountry(String country,String preGenderCode){
        if (!(country ==null)){
            binding.ProgressCard.setVisibility(View.VISIBLE);
            binding.checkScroll.setVisibility(View.GONE);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            Query query = reference.orderByChild("country").equalTo(country);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
                        binding.noDataInLocation.setVisibility(View.VISIBLE);
                        binding.errorImage.setImageDrawable(getResources().getDrawable(R.drawable.sentiment_dissatisfied));
                        binding.errorMsg.setText(getString(R.string.noUsers_withmatch));
                    }else{
                        binding.noDataInLocation.setVisibility(View.GONE);
                        binding.errorImage.setImageDrawable(getResources().getDrawable(R.drawable.sentiment_dissatisfied));
                        binding.errorMsg.setText(getString(R.string.noUsers_withmatch));
                    }
                    data.clear();
                    binding.ProgressCard.setVisibility(View.GONE);
                    binding.checkScroll.setVisibility(View.VISIBLE);
                    for (DataSnapshot date : snapshot.getChildren()){
                        MingleModel model = date.getValue(MingleModel.class);
                        Collections.shuffle(data);
                        if (Objects.equals(model.getGenderCode(), preGenderCode) && !Objects.equals(model.getUser_id(), FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            data.add(model);
                        }
                        snapshot.getRef().onDisconnect();

                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void getUserByCurrentProvince(String provinve,String country, String preGenderCode){
        if (!provinve.isEmpty()){
            binding.ProgressCard.setVisibility(View.VISIBLE);
            binding.checkScroll.setVisibility(View.GONE);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            Query query = reference.orderByChild("province").equalTo(provinve);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    data.clear();
                    binding.ProgressCard.setVisibility(View.GONE);
                    binding.checkScroll.setVisibility(View.VISIBLE);
                    if (snapshot.exists()){
                        for (DataSnapshot date : snapshot.getChildren()){
                            MingleModel model = date.getValue(MingleModel.class);
                            int age = Integer.parseInt(model.getAge());
                            Collections.shuffle(data);

                            if (Objects.equals(model.getGenderCode(), preGenderCode) && !Objects.equals(model.getUser_id(), FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                data.add(model);
                            }
                            snapshot.getRef().onDisconnect();

                        }
                    }else {
                        getUserByCurrentCountry(country,preGenderCode);
                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    private void getUserByCurrentTown(String town,String city,String provinve,String country,String preGenderCode){
        if (!town.isEmpty()){
            binding.ProgressCard.setVisibility(View.VISIBLE);
            binding.checkScroll.setVisibility(View.GONE);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            Query query = reference.orderByChild("town").equalTo(town);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    data.clear();
                    binding.ProgressCard.setVisibility(View.GONE);
                    binding.checkScroll.setVisibility(View.VISIBLE);
                    if (snapshot.exists()){
                        for (DataSnapshot date : snapshot.getChildren()){
                            MingleModel model = date.getValue(MingleModel.class);

                            Collections.shuffle(data);
                            if ( Objects.equals(model.getGenderCode(), preGenderCode) && !Objects.equals(model.getUser_id(), FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                data.add(model);
                            }
                            snapshot.getRef().onDisconnect();


                        }
                    }else {
                        getUserByCurrentCity(city,provinve,country,preGenderCode);
                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }


    private void getUserByCurrentCity(String city,String province,String country,String preGenderCode){
        if (!city.isEmpty()){
            binding.ProgressCard.setVisibility(View.VISIBLE);
            binding.checkScroll.setVisibility(View.GONE);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            Query query = reference.orderByChild("city").equalTo(city);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    data.clear();
                    binding.ProgressCard.setVisibility(View.GONE);
                    binding.checkScroll.setVisibility(View.VISIBLE);
                    if (snapshot.exists()){
                        for (DataSnapshot date : snapshot.getChildren()){
                            MingleModel model = date.getValue(MingleModel.class);

                            Collections.shuffle(data);
                            if (Objects.equals(model.getGenderCode(), preGenderCode) && !Objects.equals(model.getUser_id(), FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                data.add(model);
                            }snapshot.getRef().onDisconnect();


                        }

                    }else{
                        getUserByCurrentProvince(province,country,preGenderCode);

                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            binding.noDataInLocation.setVisibility(View.VISIBLE);
            binding.errorImage.setImageDrawable(getResources().getDrawable(R.drawable.sentiment_dissatisfied));
            binding.errorMsg.setText(getString(R.string.noUsers_withmatch));
        }
    }
}