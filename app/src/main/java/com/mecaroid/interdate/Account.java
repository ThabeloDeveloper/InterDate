package com.mecaroid.interdate;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mecaroid.interdate.databinding.ActivityAccountBinding;
import com.mecaroid.interdate.databinding.LanguagesBinding;
import com.mecaroid.interdate.databinding.PurchaseListBinding;
import com.mecaroid.interdate.databinding.SubscriptionListBinding;
import com.mecaroid.interdate.databinding.SubscriptionPurchaseBinding;
import com.mecaroid.interdate.databinding.SwitchThemeBinding;

import java.util.Locale;
import java.util.Objects;

public class Account extends AppCompatActivity {


    ActivityAccountBinding binding;
    String imageProfile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        binding.username.setText(intent.getStringExtra("username"));
        Glide.with(this).load(intent.getStringExtra("profile")).into(binding.profileImage);
        binding.currentLanguage.setText(currentLanguage());
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference referenceUsername = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("username");
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("profile");
        DatabaseReference referenceAbout = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("about_user");
        loadData(referenceUsername,false,binding.username,null,null);
        loadData(referenceProfile,true,null,null,binding.profileImage);
        referenceAbout.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.about.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HelpActivity.class);
                startActivity(intent);
            }
        });
        binding.purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PurchaseOnClick();

            }
        });
        binding.cardTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchThemeBinding switchThemeBinding = SwitchThemeBinding.inflate(getLayoutInflater());
                BottomSheetDialog themeChanger = new BottomSheetDialog(v.getContext());
                themeChanger.setContentView(switchThemeBinding.getRoot());
                themeChanger.show();
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM){
                    switchThemeBinding.autoSwitch.setChecked(true);
                } else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                    switchThemeBinding.lightSwitch.setChecked(true);

                } else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    switchThemeBinding.darkSwitch.setChecked(true);
                }
                themeChanger.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (!switchThemeBinding.lightSwitch.isChecked() && !switchThemeBinding.darkSwitch.isChecked() &&
                                !switchThemeBinding.autoSwitch.isChecked()){
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                        }
                    }
                });
                switchThemeBinding.autoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            switchThemeBinding.darkSwitch.setChecked(false);
                            switchThemeBinding.lightSwitch.setChecked(false);
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                            SharedPreferences sharedPreferences = getSharedPreferences("AppTheme",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("currentTheme","auto");
                            editor.apply();
                        }
                    }
                });
                switchThemeBinding.darkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            switchThemeBinding.lightSwitch.setChecked(false);
                            switchThemeBinding.autoSwitch.setChecked(false);
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            SharedPreferences sharedPreferences = getSharedPreferences("AppTheme",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("currentTheme","dark");
                            editor.apply();
                        }
                    }
                });
                switchThemeBinding.lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            switchThemeBinding.darkSwitch.setChecked(false);
                            switchThemeBinding.autoSwitch.setChecked(false);
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            SharedPreferences sharedPreferences = getSharedPreferences("AppTheme",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("currentTheme","light");
                            editor.apply();

                        }
                    }
                });




            }
        });

        binding.language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localisationSet();
            }
        });
        binding.cardPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Account.this,Private_Blocklist.class);
                startActivity(intent1);

            }
        });
        binding.changesMails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(),Update_Account_Info.class);
                if (imageProfile != null){
                    intent1.putExtra("profile",imageProfile);
                }
                intent1.putExtra("username",binding.username.getText().toString());
                startActivity(intent1);


            }
        });
        binding.tooBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.updatePublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(),Update_User_Public_Info.class);
                startActivity(intent1);
            }
        });


    }


    private void loadData(@NonNull DatabaseReference reference, boolean type,@Nullable TextView textView,@Nullable TextView textAbout,@Nullable ImageView imageView){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (type) {
                    imageProfile = snapshot.getValue(String.class);
                    if (imageView != null) {
                        Glide.with(Account.this).load(snapshot.getValue(String.class)).into(imageView);
                    }
                } else {
                    if (textView != null) {
                        textView.setText(snapshot.getValue(String.class));
                    }if (textAbout !=null){
                        textAbout.setText(snapshot.getValue(String.class));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void PurchaseOnClick(){
        SubscriptionPurchaseBinding subscriptionBinding = SubscriptionPurchaseBinding.inflate(getLayoutInflater());
        BottomSheetDialog sheetDialog = new BottomSheetDialog(this);
        sheetDialog.setCancelable(true);
        sheetDialog.setContentView(subscriptionBinding.getRoot());
        sheetDialog.show();
        subscriptionBinding.subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubscriptionListBinding subscriptionListBinding = SubscriptionListBinding.inflate(getLayoutInflater());
                BottomSheetDialog subscribeSheet = new BottomSheetDialog(Account.this);
                subscribeSheet.setContentView(subscriptionListBinding.getRoot());
                subscribeSheet.setCancelable(true);
                subscribeSheet.show();
                sheetDialog.dismiss();
                AnimationOnClick(subscriptionListBinding.annual,"interdateannual");
                AnimationOnClick(subscriptionListBinding.monthly,"interdatemonthly");
                AnimationOnClick(subscriptionListBinding.weekly,"interdateweekly");
                AnimationOnClick(subscriptionListBinding.daily,"interdatedaily");


            }
        });
        subscriptionBinding.Purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetDialog.dismiss();
                PurchaseListBinding listBinding = PurchaseListBinding.inflate(getLayoutInflater());
                sheetDialog.setContentView(listBinding.getRoot());
                listBinding.minorBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                sheetDialog.show();

            }
        });

    }
    private void AnimationOnClick(LinearLayout layout,String sub_id){
        float scale = 0.9f;
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = new ScaleAnimation(0.2f,scale,0.2f,scale,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                animation.setDuration(100);
                animation.setRepeatCount(0);
                animation.setRepeatMode(Animation.ABSOLUTE);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName() + "&sku=" + sub_id));
                        startActivity(playStoreIntent);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                layout.setAnimation(animation);
                layout.startAnimation(animation);

            }
        });

    }

    BottomSheetDialog localizeSheet;
    private void localisationSet(){

        localizeSheet = new BottomSheetDialog(Account.this);
        LanguagesBinding languagesBinding = LanguagesBinding.inflate(getLayoutInflater());
        localizeSheet.setContentView(languagesBinding.getRoot());
        languagesBinding.currentLanguage.setChecked(true);
        localizeSheet.setCancelable(false);
        checkedChangeDetector(languagesBinding.afrikaans,languagesBinding);
        checkedChangeDetector(languagesBinding.chineseSimplified,languagesBinding);
        checkedChangeDetector(languagesBinding.chineseTraditional,languagesBinding);
        checkedChangeDetector(languagesBinding.czech,languagesBinding);
        checkedChangeDetector(languagesBinding.english,languagesBinding);
        checkedChangeDetector(languagesBinding.franch,languagesBinding);
        checkedChangeDetector(languagesBinding.german,languagesBinding);
        checkedChangeDetector(languagesBinding.italian,languagesBinding);
        checkedChangeDetector(languagesBinding.japanese,languagesBinding);
        checkedChangeDetector(languagesBinding.korean,languagesBinding);
        checkedChangeDetector(languagesBinding.polish,languagesBinding);
        checkedChangeDetector(languagesBinding.portuguese,languagesBinding);
        checkedChangeDetector(languagesBinding.russian,languagesBinding);
        checkedChangeDetector(languagesBinding.spanish,languagesBinding);
        checkedChangeDetector(languagesBinding.turkish,languagesBinding);
        languagesBinding.dismiss.setOnClickListener(v->{
            languagesBinding.dismiss.animate().scaleX(0f).scaleY(0f).setDuration(500).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(@NonNull Animator animator) {

                }

                @Override
                public void onAnimationEnd(@NonNull Animator animator) {
                    localizeSheet.dismiss();

                }

                @Override
                public void onAnimationCancel(@NonNull Animator animator) {

                }

                @Override
                public void onAnimationRepeat(@NonNull Animator animator) {

                }
            }).start();

        });

        if(Locale.getDefault().getLanguage().equals("af")){
            languagesBinding.afrikaans.setChecked(true);
            languagesBinding.currentLanguage.setText(getString(R.string.afrikaans) + " ("+getString(R.string.current_language)+")");
        }else if(Locale.getDefault().getLanguage().equals("cs")){
            languagesBinding.czech.setChecked(true);
            languagesBinding.currentLanguage.setText(getString(R.string.czech) + " ("+getString(R.string.current_language)+")");
        }else if(Locale.getDefault().getLanguage().equals("de")){
            languagesBinding.german.setChecked(true);
            languagesBinding.currentLanguage.setText(getString(R.string.german) + " ("+getString(R.string.current_language)+")");

        }else if(Locale.getDefault().getLanguage().equals("en")){
            languagesBinding.english.setChecked(true);
            languagesBinding.currentLanguage.setText(getString(R.string.english) + " ("+getString(R.string.current_language)+")");
        }else if(Locale.getDefault().getLanguage().equals("es")){
            languagesBinding.spanish.setChecked(true);
            languagesBinding.currentLanguage.setText(getString(R.string.spanish) + " ("+getString(R.string.current_language)+")");
        }else if(Locale.getDefault().getLanguage().equals("fr")){
            languagesBinding.franch.setChecked(true);
            languagesBinding.currentLanguage.setText(getString(R.string.french) + " ("+getString(R.string.current_language)+")");

        }else if(Locale.getDefault().getLanguage().equals("it")){
            languagesBinding.italian.setChecked(true);
            languagesBinding.currentLanguage.setText(getString(R.string.italian) + " ("+getString(R.string.current_language)+")");

        }else if(Locale.getDefault().getLanguage().equals("ja")){
            languagesBinding.japanese.setChecked(true);
            languagesBinding.currentLanguage.setText(getString(R.string.japanese) + " ("+getString(R.string.current_language)+")");

        }else if(Locale.getDefault().getLanguage().equals("ko")){
            languagesBinding.korean.setChecked(true);
            languagesBinding.currentLanguage.setText(getString(R.string.korean) + " ("+getString(R.string.current_language)+")");

        }else if(Locale.getDefault().getLanguage().equals("pl")){
            languagesBinding.polish.setChecked(true);
            languagesBinding.currentLanguage.setText(getString(R.string.polish) + " ("+getString(R.string.current_language)+")");

        }else if(Locale.getDefault().getLanguage().equals("pt")){
            languagesBinding.portuguese.setChecked(true);
            languagesBinding.currentLanguage.setText(getString(R.string.portuguese) + " ("+getString(R.string.current_language)+")");

        }else if(Locale.getDefault().getLanguage().equals("ru")){
            languagesBinding.russian.setChecked(true);
            languagesBinding.currentLanguage.setText(getString(R.string.russian) + " ("+getString(R.string.current_language)+")");

        }else if(Locale.getDefault().getLanguage().equals("tr")){
            languagesBinding.turkish.setChecked(true);
            languagesBinding.currentLanguage.setText(getString(R.string.turkish) + " ("+getString(R.string.current_language)+")");

        }
        localizeSheet.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                languagesBinding.dismiss.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(350).start();
            }
        });
        SharedPreferences.Editor editor = getSharedPreferences("Localization",MODE_PRIVATE).edit();
        languagesBinding.dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (languagesBinding.afrikaans.isChecked()){
                    changeLanguages("af");
                    editor.putString("currentLanguage","af");
                    editor.apply();
                    editor.commit();
                }else if(languagesBinding.chineseSimplified.isChecked()){
                    changeLanguages("zh");
                    editor.putString("currentLanguage","zh");
                    editor.apply();
                    editor.commit();
                } else if (languagesBinding.chineseTraditional.isChecked()) {
                    changeLanguages("zh");
                    editor.putString("currentLanguage","zh");
                    editor.apply();
                    editor.commit();
                }else if (languagesBinding.czech.isChecked()){
                    changeLanguages("cs");
                    editor.putString("currentLanguage","cs");
                    editor.apply();
                    editor.commit();
                }else if (languagesBinding.english.isChecked()){
                    changeLanguages("en");
                    editor.putString("currentLanguage","en");
                    editor.apply();
                    editor.commit();
                }else if (languagesBinding.franch.isChecked()){
                    changeLanguages("fr");
                    editor.putString("currentLanguage","fr");
                    editor.apply();
                    editor.commit();
                }else if (languagesBinding.german.isChecked()){
                    changeLanguages("de");
                    editor.putString("currentLanguage","de");
                    editor.apply();
                    editor.commit();
                }else if (languagesBinding.italian.isChecked()){
                    editor.putString("currentLanguage","it");
                    editor.apply();
                    editor.commit();
                    changeLanguages("it");
                }else if (languagesBinding.japanese.isChecked()){
                    changeLanguages("ja");
                    editor.putString("currentLanguage","ja");
                    editor.apply();
                    editor.commit();
                }else if (languagesBinding.korean.isChecked()){
                    changeLanguages("ko");
                    editor.putString("currentLanguage","ko");
                    editor.apply();
                    editor.commit();
                }else if (languagesBinding.polish.isChecked()){
                    changeLanguages("pl");
                    editor.putString("currentLanguage","pl");
                    editor.apply();
                    editor.commit();
                }else if (languagesBinding.portuguese.isChecked()){
                    changeLanguages("pt");
                    editor.putString("currentLanguage","pt");
                    editor.apply();
                    editor.commit();
                }else if (languagesBinding.russian.isChecked()){
                    changeLanguages("ru");
                    editor.putString("currentLanguage","ru");
                    editor.apply();
                    editor.commit();
                }else if (languagesBinding.spanish.isChecked()){
                    changeLanguages("es");
                    editor.putString("currentLanguage","es");
                    editor.apply();
                    editor.commit();
                }else if (languagesBinding.turkish.isChecked()){
                    changeLanguages("tr");
                    editor.putString("currentLanguage","tr");
                    editor.apply();
                    editor.commit();

                }
            }
        });


        localizeSheet.show();

    }
    private void checkedChangeDetector(MaterialSwitch materialSwitch, LanguagesBinding languagesBinding){
        materialSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            /////////AFRIKAANS LANGUAGE///////////

            if( materialSwitch == languagesBinding.afrikaans && isChecked){
                languagesBinding.chineseSimplified.setChecked(false);
                languagesBinding.chineseTraditional.setChecked(false);
                languagesBinding.czech.setChecked(false);
                languagesBinding.english.setChecked(false);
                languagesBinding.franch.setChecked(false);
                languagesBinding.german.setChecked(false);
                languagesBinding.italian.setChecked(false);
                languagesBinding.japanese.setChecked(false);
                languagesBinding.korean.setChecked(false);
                languagesBinding.polish.setChecked(false);
                languagesBinding.portuguese.setChecked(false);
                languagesBinding.russian.setChecked(false);
                languagesBinding.spanish.setChecked(false);
                languagesBinding.turkish.setChecked(false);



                /////////CHINESE SIMPLIFIED LANGUAGE///////////
            }else if (materialSwitch == languagesBinding.chineseSimplified && isChecked){
                languagesBinding.afrikaans.setChecked(false);
                languagesBinding.chineseTraditional.setChecked(false);
                languagesBinding.czech.setChecked(false);
                languagesBinding.english.setChecked(false);
                languagesBinding.franch.setChecked(false);
                languagesBinding.german.setChecked(false);
                languagesBinding.italian.setChecked(false);
                languagesBinding.japanese.setChecked(false);
                languagesBinding.korean.setChecked(false);
                languagesBinding.polish.setChecked(false);
                languagesBinding.portuguese.setChecked(false);
                languagesBinding.russian.setChecked(false);
                languagesBinding.spanish.setChecked(false);
                languagesBinding.turkish.setChecked(false);

                /////////CHINESE TRADITIONAL LANGUAGE///////////
            }else if (materialSwitch == languagesBinding.chineseTraditional && isChecked){
                languagesBinding.chineseSimplified.setChecked(false);
                languagesBinding.afrikaans.setChecked(false);
                languagesBinding.czech.setChecked(false);
                languagesBinding.english.setChecked(false);
                languagesBinding.franch.setChecked(false);
                languagesBinding.german.setChecked(false);
                languagesBinding.italian.setChecked(false);
                languagesBinding.japanese.setChecked(false);
                languagesBinding.korean.setChecked(false);
                languagesBinding.polish.setChecked(false);
                languagesBinding.portuguese.setChecked(false);
                languagesBinding.russian.setChecked(false);
                languagesBinding.spanish.setChecked(false);
                languagesBinding.turkish.setChecked(false);

                /////////CZECH LANGUAGE///////////
            }else if (materialSwitch == languagesBinding.chineseSimplified && isChecked){
                languagesBinding.chineseSimplified.setChecked(false);
                languagesBinding.chineseTraditional.setChecked(false);
                languagesBinding.czech.setChecked(false);
                languagesBinding.english.setChecked(false);
                languagesBinding.franch.setChecked(false);
                languagesBinding.german.setChecked(false);
                languagesBinding.italian.setChecked(false);
                languagesBinding.japanese.setChecked(false);
                languagesBinding.korean.setChecked(false);
                languagesBinding.polish.setChecked(false);
                languagesBinding.portuguese.setChecked(false);
                languagesBinding.russian.setChecked(false);
                languagesBinding.spanish.setChecked(false);
                languagesBinding.turkish.setChecked(false);


                /////////ENGLISH LANGUAGE///////////
            }else if (materialSwitch == languagesBinding.english && isChecked){
                languagesBinding.chineseSimplified.setChecked(false);
                languagesBinding.chineseTraditional.setChecked(false);
                languagesBinding.czech.setChecked(false);
                languagesBinding.afrikaans.setChecked(false);
                languagesBinding.franch.setChecked(false);
                languagesBinding.german.setChecked(false);
                languagesBinding.italian.setChecked(false);
                languagesBinding.japanese.setChecked(false);
                languagesBinding.korean.setChecked(false);
                languagesBinding.polish.setChecked(false);
                languagesBinding.portuguese.setChecked(false);
                languagesBinding.russian.setChecked(false);
                languagesBinding.spanish.setChecked(false);
                languagesBinding.turkish.setChecked(false);

                ////////FRENCH LANGUAGES////////////
            }else if (materialSwitch == languagesBinding.czech && isChecked){
                languagesBinding.chineseSimplified.setChecked(false);
                languagesBinding.chineseTraditional.setChecked(false);
                languagesBinding.afrikaans.setChecked(false);
                languagesBinding.english.setChecked(false);
                languagesBinding.franch.setChecked(false);
                languagesBinding.german.setChecked(false);
                languagesBinding.italian.setChecked(false);
                languagesBinding.japanese.setChecked(false);
                languagesBinding.korean.setChecked(false);
                languagesBinding.polish.setChecked(false);
                languagesBinding.portuguese.setChecked(false);
                languagesBinding.russian.setChecked(false);
                languagesBinding.spanish.setChecked(false);
                languagesBinding.turkish.setChecked(false);

                //////////GERMAN LANGUAGE /////////////////
            }else if (materialSwitch == languagesBinding.franch && isChecked){
                languagesBinding.chineseSimplified.setChecked(false);
                languagesBinding.chineseTraditional.setChecked(false);
                languagesBinding.czech.setChecked(false);
                languagesBinding.english.setChecked(false);
                languagesBinding.afrikaans.setChecked(false);
                languagesBinding.german.setChecked(false);
                languagesBinding.italian.setChecked(false);
                languagesBinding.japanese.setChecked(false);
                languagesBinding.korean.setChecked(false);
                languagesBinding.polish.setChecked(false);
                languagesBinding.portuguese.setChecked(false);
                languagesBinding.russian.setChecked(false);
                languagesBinding.spanish.setChecked(false);
                languagesBinding.turkish.setChecked(false);


            }else if (materialSwitch == languagesBinding.german && isChecked){
                languagesBinding.chineseSimplified.setChecked(false);
                languagesBinding.chineseTraditional.setChecked(false);
                languagesBinding.czech.setChecked(false);
                languagesBinding.english.setChecked(false);
                languagesBinding.franch.setChecked(false);
                languagesBinding.afrikaans.setChecked(false);
                languagesBinding.italian.setChecked(false);
                languagesBinding.japanese.setChecked(false);
                languagesBinding.korean.setChecked(false);
                languagesBinding.polish.setChecked(false);
                languagesBinding.portuguese.setChecked(false);
                languagesBinding.russian.setChecked(false);
                languagesBinding.spanish.setChecked(false);
                languagesBinding.turkish.setChecked(false);


            }else if (materialSwitch == languagesBinding.italian && isChecked){
                languagesBinding.chineseSimplified.setChecked(false);
                languagesBinding.chineseTraditional.setChecked(false);
                languagesBinding.czech.setChecked(false);
                languagesBinding.english.setChecked(false);
                languagesBinding.franch.setChecked(false);
                languagesBinding.german.setChecked(false);
                languagesBinding.afrikaans.setChecked(false);
                languagesBinding.japanese.setChecked(false);
                languagesBinding.korean.setChecked(false);
                languagesBinding.polish.setChecked(false);
                languagesBinding.portuguese.setChecked(false);
                languagesBinding.russian.setChecked(false);
                languagesBinding.spanish.setChecked(false);
                languagesBinding.turkish.setChecked(false);


            }else if (materialSwitch == languagesBinding.japanese && isChecked){
                languagesBinding.chineseSimplified.setChecked(false);
                languagesBinding.chineseTraditional.setChecked(false);
                languagesBinding.czech.setChecked(false);
                languagesBinding.english.setChecked(false);
                languagesBinding.franch.setChecked(false);
                languagesBinding.german.setChecked(false);
                languagesBinding.italian.setChecked(false);
                languagesBinding.afrikaans.setChecked(false);
                languagesBinding.korean.setChecked(false);
                languagesBinding.polish.setChecked(false);
                languagesBinding.portuguese.setChecked(false);
                languagesBinding.russian.setChecked(false);
                languagesBinding.spanish.setChecked(false);
                languagesBinding.turkish.setChecked(false);


            }else if (materialSwitch == languagesBinding.korean && isChecked){
                languagesBinding.chineseSimplified.setChecked(false);
                languagesBinding.chineseTraditional.setChecked(false);
                languagesBinding.czech.setChecked(false);
                languagesBinding.english.setChecked(false);
                languagesBinding.franch.setChecked(false);
                languagesBinding.german.setChecked(false);
                languagesBinding.italian.setChecked(false);
                languagesBinding.japanese.setChecked(false);
                languagesBinding.afrikaans.setChecked(false);
                languagesBinding.polish.setChecked(false);
                languagesBinding.portuguese.setChecked(false);
                languagesBinding.russian.setChecked(false);
                languagesBinding.spanish.setChecked(false);
                languagesBinding.turkish.setChecked(false);


            }else if (materialSwitch == languagesBinding.polish && isChecked){
                languagesBinding.chineseSimplified.setChecked(false);
                languagesBinding.chineseTraditional.setChecked(false);
                languagesBinding.czech.setChecked(false);
                languagesBinding.english.setChecked(false);
                languagesBinding.franch.setChecked(false);
                languagesBinding.german.setChecked(false);
                languagesBinding.italian.setChecked(false);
                languagesBinding.japanese.setChecked(false);
                languagesBinding.korean.setChecked(false);
                languagesBinding.afrikaans.setChecked(false);
                languagesBinding.portuguese.setChecked(false);
                languagesBinding.russian.setChecked(false);
                languagesBinding.spanish.setChecked(false);
                languagesBinding.turkish.setChecked(false);


            }else if (materialSwitch == languagesBinding.portuguese && isChecked){
                languagesBinding.chineseSimplified.setChecked(false);
                languagesBinding.chineseTraditional.setChecked(false);
                languagesBinding.czech.setChecked(false);
                languagesBinding.english.setChecked(false);
                languagesBinding.franch.setChecked(false);
                languagesBinding.german.setChecked(false);
                languagesBinding.italian.setChecked(false);
                languagesBinding.japanese.setChecked(false);
                languagesBinding.korean.setChecked(false);
                languagesBinding.polish.setChecked(false);
                languagesBinding.russian.setChecked(false);
                languagesBinding.afrikaans.setChecked(false);
                languagesBinding.spanish.setChecked(false);
                languagesBinding.turkish.setChecked(false);


            }else if (materialSwitch == languagesBinding.russian && isChecked){
                languagesBinding.chineseSimplified.setChecked(false);
                languagesBinding.chineseTraditional.setChecked(false);
                languagesBinding.czech.setChecked(false);
                languagesBinding.english.setChecked(false);
                languagesBinding.franch.setChecked(false);
                languagesBinding.german.setChecked(false);
                languagesBinding.italian.setChecked(false);
                languagesBinding.japanese.setChecked(false);
                languagesBinding.korean.setChecked(false);
                languagesBinding.polish.setChecked(false);
                languagesBinding.portuguese.setChecked(false);
                languagesBinding.afrikaans.setChecked(false);
                languagesBinding.spanish.setChecked(false);
                languagesBinding.turkish.setChecked(false);


            }else if (materialSwitch == languagesBinding.spanish && isChecked){
                languagesBinding.chineseSimplified.setChecked(false);
                languagesBinding.chineseTraditional.setChecked(false);
                languagesBinding.czech.setChecked(false);
                languagesBinding.english.setChecked(false);
                languagesBinding.franch.setChecked(false);
                languagesBinding.german.setChecked(false);
                languagesBinding.italian.setChecked(false);
                languagesBinding.japanese.setChecked(false);
                languagesBinding.korean.setChecked(false);
                languagesBinding.polish.setChecked(false);
                languagesBinding.portuguese.setChecked(false);
                languagesBinding.russian.setChecked(false);
                languagesBinding.afrikaans.setChecked(false);
                languagesBinding.turkish.setChecked(false);


            }else if (materialSwitch == languagesBinding.turkish && isChecked){
                languagesBinding.chineseSimplified.setChecked(false);
                languagesBinding.afrikaans.setChecked(false);
                languagesBinding.chineseTraditional.setChecked(false);
                languagesBinding.czech.setChecked(false);
                languagesBinding.english.setChecked(false);
                languagesBinding.franch.setChecked(false);
                languagesBinding.german.setChecked(false);
                languagesBinding.italian.setChecked(false);
                languagesBinding.japanese.setChecked(false);
                languagesBinding.korean.setChecked(false);
                languagesBinding.polish.setChecked(false);
                languagesBinding.portuguese.setChecked(false);
                languagesBinding.russian.setChecked(false);
                languagesBinding.spanish.setChecked(false);


            }
        });
    }

    private void changeLanguages(String code){
        setLocaleShared(code);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1){
            Locale locale1 = new Locale(code);
            Locale.setDefault(locale1);
            recreate();
        }else{
            Locale locale = new Locale(code);
            Locale.setDefault(locale);
            Configuration config =new Configuration();
            config.setLocale(locale);
            config.locale = locale;
            config.setLocale(locale);
            getResources().updateConfiguration(config,getResources().getDisplayMetrics());
            recreate();

        }

    }
    private void setLocaleShared(String code){
        SharedPreferences.Editor editor = getSharedPreferences("Localization",MODE_PRIVATE).edit();
        editor.putString("currentLanguage",code);
        editor.apply();
        editor.commit();

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    String currentLanguage(){
        String currentLang = getString(R.string.english) + " (" + getString(R.string.current_language)+ ")";
        if(Locale.getDefault().getLanguage().equals("af")){
            currentLang = getString(R.string.afrikaans) + " ("+getString(R.string.current_language)+")";
        }else if(Locale.getDefault().getLanguage().equals("cs")){
            currentLang = getString(R.string.czech) + " ("+getString(R.string.current_language)+")";
        }else if(Locale.getDefault().getLanguage().equals("de")){
            currentLang = getString(R.string.german) + " ("+getString(R.string.current_language)+")";
        }else if(Locale.getDefault().getLanguage().equals("en")){
            currentLang = getString(R.string.english) + " ("+getString(R.string.current_language)+")";
        }else if(Locale.getDefault().getLanguage().equals("es")){
            currentLang = getString(R.string.spanish) + " ("+getString(R.string.current_language)+")";
        }else if(Locale.getDefault().getLanguage().equals("fr")){
            currentLang = getString(R.string.french) + " ("+getString(R.string.current_language)+")";
        }else if(Locale.getDefault().getLanguage().equals("it")){
            currentLang = getString(R.string.italian) + " ("+getString(R.string.current_language)+")";
        }else if(Locale.getDefault().getLanguage().equals("ja")){
            currentLang = getString(R.string.japanese) + " ("+getString(R.string.current_language)+")";
        }else if(Locale.getDefault().getLanguage().equals("ko")){
            currentLang = getString(R.string.korean) + " ("+getString(R.string.current_language)+")";
        }else if(Locale.getDefault().getLanguage().equals("pl")){
            currentLang =getString(R.string.polish) + " ("+getString(R.string.current_language)+")";
        }else if(Locale.getDefault().getLanguage().equals("pt")){
            currentLang = getString(R.string.portuguese) + " ("+getString(R.string.current_language)+")";
        }else if(Locale.getDefault().getLanguage().equals("ru")){
            currentLang = getString(R.string.russian) + " ("+getString(R.string.current_language)+")";
        }else if(Locale.getDefault().getLanguage().equals("tr")){
            currentLang = getString(R.string.turkish) + " ("+getString(R.string.current_language)+")";

        }
        return currentLang;

    }





}