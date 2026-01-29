package com.mecaroid.interdate;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mecaroid.interdate.Authentication.SignIn;
import com.mecaroid.interdate.databinding.ActivitySplashScreenBinding;

import java.util.Locale;
import java.util.Objects;

public class Splash_Screen extends AppCompatActivity {
    FirebaseAuth Auth =FirebaseAuth.getInstance();
    FirebaseUser user = Auth.getCurrentUser();
    ActivitySplashScreenBinding binding;
    ImageView imageBottom;
    int initialWidthBottom;
    int initialHeightBottom;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        handler.postDelayed(()->{
            if (user == null) {
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                finish();
            }else {
                  DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("username");
                  reference.addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                          if (snapshot.exists()){
                              Intent intent = new Intent(Splash_Screen.this, MainActivity.class);
                              startActivity(intent);
                              finish();


                          }else {
                              if (!isGetInformationStarted){
                                  isGetInformationStarted = true;
                                  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                  startActivity(new Intent(getApplicationContext(), GetInformation.class));
                                  finish();
                              }

                          }

                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {
                          android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getApplicationContext(),R.style.CustomProgressDialogStyle);
                          alertDialog.setCancelable(false);
                          alertDialog.setMessage(error.getMessage());
                          alertDialog.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  dialog.dismiss();
                                  finish();

                              }
                          });
                          alertDialog.create();
                          alertDialog.show();

                      }
                  });

              }

        },5000);
        initialWidthBottom = binding.imageBottom.getWidth();
        initialHeightBottom = binding.imageBottom.getHeight();



    }
    private void fullScreenContents(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            Objects.requireNonNull(getWindow().getInsetsController()).hide(WindowInsets.Type.navigationBars());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        switchLang();

    }

    void switchLang(){
        if(Locale.getDefault().getLanguage().equals("af")){
            changeLanguages("af");

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
    private boolean isGetInformationStarted = false;
    private boolean isMainActivityStarted = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}