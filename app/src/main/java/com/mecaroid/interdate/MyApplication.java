package com.mecaroid.interdate;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.paypal.checkout.PayPalCheckout;
//import com.paypal.checkout.config.CheckoutConfig;
//import com.paypal.checkout.config.Environment;
//import com.paypal.checkout.createorder.CurrencyCode;
//import com.paypal.checkout.createorder.UserAction;
//import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService;
//import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
//import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MyApplication extends Application
        implements ActivityLifecycleCallbacks, DefaultLifecycleObserver {


    DatabaseReference reference;



    @Override
    public void onCreate() {
        super.onCreate();
        this.registerActivityLifecycleCallbacks(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        reference = FirebaseDatabase.getInstance().getReference("Presence");
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        initializeApp();
        LocalisedApp();
        sendGeographic();
        SharedPreferences sharedPreferences = getSharedPreferences("AppTheme",MODE_PRIVATE);
        String currentTheme = sharedPreferences.getString("currentTheme","null");
        switch (Objects.requireNonNull(currentTheme)) {
            case "dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }


    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onStart(owner);
        LocalisedApp();
    }

    LocationManager locationManager;
    private void sendGeographic(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED ){

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
                        Map<String,Object> map = new HashMap<>();
                        map.put("latitude",latitude);
                        map.put("longitude",longitude);
                        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Locations").child(uid);
                        reference.setValue(map);





                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }



    private void LocalisedApp(){
        SharedPreferences editor = getSharedPreferences("Localization",MODE_PRIVATE);
        String code = editor.getString("currentLanguage","");
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Configuration config =new Configuration();
        config.setLocale(locale);
        config.locale = locale;
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());

        if(Locale.getDefault().getLanguage().equals("af")){
            changeLanguages("af");
            getResources().updateConfiguration(config,getResources().getDisplayMetrics());
        }else if(Locale.getDefault().getLanguage().equals("cs")){
            changeLanguages("cs");
        }else if(Locale.getDefault().getLanguage().equals("de")){
            changeLanguages("de");

        }else if(Locale.getDefault().getLanguage().equals("en")){
            changeLanguages("en");
        }else if(Locale.getDefault().getLanguage().equals("es")){
            changeLanguages("es");
        }else if(Locale.getDefault().getLanguage().equals("fr")){
            changeLanguages("fr");

        }else if(Locale.getDefault().getLanguage().equals("it")){
            changeLanguages("it");

        }else if(Locale.getDefault().getLanguage().equals("ja")){
            changeLanguages("ja");

        }else if(Locale.getDefault().getLanguage().equals("ko")){
            changeLanguages("ko");

        }else if(Locale.getDefault().getLanguage().equals("pl")){
            changeLanguages("pl");

        }else if(Locale.getDefault().getLanguage().equals("pt")){
            changeLanguages("pt");

        }else if(Locale.getDefault().getLanguage().equals("ru")){
            changeLanguages("ru");

        }else if(Locale.getDefault().getLanguage().equals("tr")){
            changeLanguages("tr");

        }


    }
    private void changeLanguages(String code){
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Configuration config =new Configuration();
        config.setLocale(locale);
        config.locale = locale;
        config.setLocale(locale);
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());


    }
    private void initializeApp()
    {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                // on below line displaying a log that admob ads has been initialized.
                Log.i("Admob", "Admob Initialized.");
            }
        });
    }


    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onResume(owner);

    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {}

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            reference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue("online");
        }



    }

    @Override
    public void onTerminate() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            reference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue("offline");
        }
        super.onTerminate();
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {


    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {}

    @Override
    public void onActivityStopped(@NonNull Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {}

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {}



    String CLIENT_ID = "AZPGFue1ktGHqH9VBayNdDMcFepYBQpe-XO6wi6VaINdqfkYB9r3UYV32KRbJwZz5YdHP216ELJb67es";
//    private void PayPalPayInitialisation(){
//        PayPalCheckout.setConfig(new CheckoutConfig(
//                this,
//                CLIENT_ID,
//                Environment.SANDBOX,
//                CurrencyCode.USD,
//                UserAction.PAY_NOW,
//                "com.mecaroid.interdate://paypalpay"
//
//        ));
//    }



}
