package com.mecaroid.interdate.InformationPages;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.mecaroid.interdate.Models.Shared.SharedViewModel;
import com.mecaroid.interdate.R;
import com.mecaroid.interdate.databinding.FragmentLocationInformationBinding;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class LocationInformation extends Fragment {



    public LocationInformation() {
        // Required empty public constructor
    }

    FragmentLocationInformationBinding binding;
    LocationManager locationManager;
    SharedViewModel sharedViewModel;
    Map<String,String> map;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        map = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLocationInformationBinding.inflate(getLayoutInflater());
        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        setData();
        binding.YesRelocate.setChecked(true);
        map.put("user_relocate", getString(R.string.yes));
        sharedViewModel.setData(map);

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            getLocation();
        }
        binding.YesRelocate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.NoRelocating.setChecked(false);
                    map.put("user_relocate", getString(R.string.yes));
                    sharedViewModel.setData(map);
                }else{
                    binding.NoRelocating.setChecked(true);
                    map.put("user_relocate", getString(R.string.no));
                    sharedViewModel.setData(map);
                }

            }
        });
        binding.NoRelocating.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    binding.YesRelocate.setChecked(false);
                    map.put("user_relocate", getString(R.string.no));
                    sharedViewModel.setData(map);
                }else{
                    binding.YesRelocate.setChecked(true);
                    map.put("user_relocate", getString(R.string.yes));
                    sharedViewModel.setData(map);
                }
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
    private void getLocation(){
        int REQUEST_LOCATION = 100;
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }else {
            Location locationGps;


            locationGps = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (locationGps !=null){
                double latitude = locationGps.getLatitude();
                double longitude = locationGps.getLongitude();
                Geocoder geocoder = new Geocoder(requireContext());
                try {
                    Address addresses = Objects.requireNonNull(geocoder.getFromLocation(latitude, longitude, 1)).get(0);
                    if (addresses != null) {
                        String county = addresses.getCountryName();
                        String province = addresses.getAdminArea();
                        String city = addresses.getSubAdminArea();
                        String town = addresses.getLocality();
                        binding.town.setText(town);
                        binding.city.setText(city);
                        binding.state.setText(province);
                        binding.country.setText(county);
                        // Use the location information as needed
                        // ...
                        // Example: Log the location details
                        String locationDetails = String.format(Locale.getDefault(), "County: %s, Province: %s, City: %s, Town: %s",
                                county, province, city, town);
                        Log.d("Location", locationDetails);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext(),R.style.CustomProgressDialogStyle);
                alertDialog.setCancelable(false);
                alertDialog.setMessage(getString(R.string.unable_to_get_location));
                alertDialog.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                alertDialog.create();
                alertDialog.show();

            }

        }
    }
    private void setData(){
        setText(binding.country,"country");
        setText(binding.state,"state");
        setText(binding.city,"city");
        setText(binding.town,"town");

    }


    private void setText(TextInputEditText editText, String title){

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                map.put(title,charSequence.toString());
                sharedViewModel.setData(map);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}