package com.mecaroid.interdate;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mecaroid.interdate.Adapters.Pagers.PersonalInformationAdapter;
import com.mecaroid.interdate.Models.GetInformationModel;
import com.mecaroid.interdate.Models.Shared.FragmentsSharedViewModel;
import com.mecaroid.interdate.Models.Shared.SharedViewModel;
import com.mecaroid.interdate.databinding.ActivityGetInfomationBinding;
import com.mecaroid.interdate.databinding.LanguagesBinding;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class GetInformation extends AppCompatActivity {

    ActivityGetInfomationBinding binding;
    PersonalInformationAdapter adapter;
    SharedViewModel sharedViewModel;
    FragmentsSharedViewModel fragmentsSharedViewModel;
    String imageUri, names, age, gender, race, religion, languages, occupation, hobbies, about_user, kids,
            qualifications, country, state, city, town, User_Relocate, preAgeFrom, preAgeTo,
            preGender, preRace, preReligion, preRelocate, preBasics;
    String studentAt = "Non";

    ArrayList<GetInformationModel> arrayList;
    GetInformationModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        EdgeToEdge.enable(this);
        binding = ActivityGetInfomationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestPermissions();
        binding.imageUri.setVisibility(View.INVISIBLE);

        model = new GetInformationModel();
        arrayList = new ArrayList<>();
        GetInformationModel information = new GetInformationModel();
        arrayList.add(information);
        if (binding.pager.getCurrentItem() == 0) {
            binding.backBtn.setVisibility(View.GONE);
        }
        binding.languages.setOnClickListener(view -> {
            localisationSet();

        });
        binding.privary.setOnClickListener(privacy ->{
            startActivityURL();
        });
        binding.terms.setOnClickListener(terms ->{
            startActivityURL();
        });

        fragmentsSharedViewModel = new ViewModelProvider(this).get(FragmentsSharedViewModel.class);
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        sharedViewModel.getData().observe(this, dataMap -> {
            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                imageUri = dataMap.get("imageUri");
                names = dataMap.get("username");
                age = dataMap.get("age");
                gender = dataMap.get("gender");
                race = dataMap.get("race");
                religion = dataMap.get("religion");
                languages = dataMap.get("languages");
                occupation = dataMap.get("occupations");
                hobbies = dataMap.get("hobbies");
                about_user = dataMap.get("about_user");
                kids = dataMap.get("kids");
                ////////////EducationInformation/////////////
                studentAt = dataMap.get("student?");
                qualifications = dataMap.get("qualifications");
                ////////////LocationInformation/////////////
                country = dataMap.get("country");
                state = dataMap.get("state");
                city = dataMap.get("city");
                town = dataMap.get("town");
                User_Relocate = dataMap.get("user_relocate");
                ///////////PartnerPreference///////////
                preAgeFrom = dataMap.get("preAgeFrom");
                preAgeTo = dataMap.get("preAgeTo");
                preGender = dataMap.get("preGender");
                preRace = dataMap.get("preRace");
                preReligion = dataMap.get("preReligion");
                preRelocate = dataMap.get("preRelocate");
                preBasics = dataMap.get("preBasics");


                if (imageUri != null && !imageUri.isEmpty()) {
                    binding.imageUri.setText(imageUri);
                }
                if (!binding.imageUri.getText().toString().isEmpty()) {
                    addToList();
                }
            }
        });
        adapter = new PersonalInformationAdapter(getSupportFragmentManager());
        binding.pager.setSaveEnabled(true);
        binding.pager.setAdapter(adapter);
        binding.pager.setPadding(10, 10, 10, 10);
        binding.index1.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.darkGreen)));
        binding.index2.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.DarkTop)));
        binding.index3.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.DarkTop)));
        binding.index4.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.DarkTop)));
        binding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ChangeColors(position);
                if (position != 0) {
                    binding.backBtn.setVisibility(View.VISIBLE);
                    binding.backBtn.setOnClickListener(view -> binding.pager.setCurrentItem(position - 1, true));
                }else{
                    binding.backBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.Button.setOnClickListener(view -> {
            if (binding.pager.getCurrentItem() == 0) {
                binding.pager.setCurrentItem(1, true);
            } else if (binding.pager.getCurrentItem() == 1) {
                binding.pager.setCurrentItem(2, true);
            } else if (binding.pager.getCurrentItem() == 2) {
                binding.pager.setCurrentItem(3, true);
            } else if (binding.pager.getCurrentItem() == 3) {
                handleSubmission();
            }
        });


    }
    private void startActivityURL(){
        Uri uri = Uri.parse(getString(R.string.privacy_policy_link));
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }


    private void ChangeColors(int position) {
        if (position == 0) {

            binding.index1.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.darkGreen)));
            binding.index2.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.DarkTop)));
            binding.index3.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.DarkTop)));
            binding.index4.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.DarkTop)));

        } else if (position == 1) {
            binding.index1.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.darkGreen)));
            binding.index2.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.darkGreen)));
            binding.index3.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.DarkTop)));
            binding.index4.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.DarkTop)));
            CheckPersonal();
        } else if (position == 2) {
            binding.index1.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.darkGreen)));
            binding.index2.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.darkGreen)));
            binding.index3.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.darkGreen)));
            binding.index4.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.DarkTop)));
            CheckEducation();
        } else if (position == 3) {
            binding.index1.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.darkGreen)));
            binding.index2.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.darkGreen)));
            binding.index3.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.darkGreen)));
            binding.index4.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.darkGreen)));
            CheckLocation();
        }
        if (position == 3) {
            binding.Button.setText(getString(R.string.continuea));
        } else {
            binding.Button.setText(getString(R.string.next));
        }
    }

    private static final int PERMISSION_REQUEST_CODE = 1;
    private final String[] permissions = {
            android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_SETTINGS, android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.CAMERA, android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
            android.Manifest.permission.ACCESS_WIFI_STATE, android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_CONNECT
    };

    private boolean arePermissionGranted() {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }

        }
        return true;
    }

    private void requestPermissions() {
        if (!arePermissionGranted()) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }
    }

    private void CheckPersonal() {
        if (arrayList.get(0).getPersonalImageUri() == null || arrayList.get(0).getPersonalImageUri().isEmpty()) {
            binding.pager.setCurrentItem(0, true);
            Toast.makeText(this, getString(R.string.pic_image), Toast.LENGTH_SHORT).show();
        } else if (arrayList.get(0).getPersonalNames() == null || arrayList.get(0).getPersonalNames().isEmpty()) {
            binding.pager.setCurrentItem(0, true);
            Toast.makeText(this, getString(R.string.entername), Toast.LENGTH_SHORT).show();

        } else if (arrayList.get(0).getPersonalAge() == 0) {
            binding.pager.setCurrentItem(0, true);
            Toast.makeText(this, getString(R.string.enterage), Toast.LENGTH_SHORT).show();

        } else if (arrayList.get(0).getPersonalAge() < 18) {
            binding.pager.setCurrentItem(0, true);
            Toast.makeText(this, getString(R.string.age_not_allowed), Toast.LENGTH_SHORT).show();

        } else if (arrayList.get(0).getPersonalGender() == null || arrayList.get(0).getPersonalGender().isEmpty()) {
            binding.pager.setCurrentItem(0, true);
            Toast.makeText(this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();

        } else if (arrayList.get(0).getPersonalRace() == null || arrayList.get(0).getPersonalRace().isEmpty()) {
            binding.pager.setCurrentItem(0, true);
            Toast.makeText(this, getString(R.string.enterrace), Toast.LENGTH_SHORT).show();

        } else if (arrayList.get(0).getPersonalReligion() == null || arrayList.get(0).getPersonalReligion().isEmpty()) {
            binding.pager.setCurrentItem(0, true);
            Toast.makeText(this, getString(R.string.enterreligion), Toast.LENGTH_SHORT).show();

        } else if (arrayList.get(0).getPersonalLanguages() == null || arrayList.get(0).getPersonalLanguages().isEmpty()) {
            binding.pager.setCurrentItem(0, true);
            Toast.makeText(this, getString(R.string.enterlanguages), Toast.LENGTH_SHORT).show();

        } else if (arrayList.get(0).getPersonalOccupations() == null || arrayList.get(0).getPersonalOccupations().isEmpty()) {
            binding.pager.setCurrentItem(0, true);
            Toast.makeText(this, getString(R.string.enterocupations), Toast.LENGTH_SHORT).show();

        } else if (arrayList.get(0).getPersonalHobbies() == null || arrayList.get(0).getPersonalHobbies().isEmpty()) {
            binding.pager.setCurrentItem(0, true);
            Toast.makeText(this, getString(R.string.enterhobbies), Toast.LENGTH_SHORT).show();

        } else if (arrayList.get(0).getPersonalAbout() == null || arrayList.get(0).getPersonalAbout().isEmpty()) {
            binding.pager.setCurrentItem(0, true);
            Toast.makeText(this, getString(R.string.enterabout), Toast.LENGTH_SHORT).show();

        } else if (arrayList.get(0).getPersonalKids() == null || arrayList.get(0).getPersonalKids().isEmpty()) {
            binding.pager.setCurrentItem(0, true);
            Toast.makeText(this, getString(R.string.how_many), Toast.LENGTH_SHORT).show();

        }
    }

    private void CheckEducation() {
        if (arrayList.get(0).getEducationAt() == null || arrayList.get(0).getEducationAt().isEmpty()) {
            binding.pager.setCurrentItem(1, true);
            Toast.makeText(this, getString(R.string.where_are_you_attending), Toast.LENGTH_SHORT).show();
        } else if (arrayList.get(0).getEducationQualifications() == null || arrayList.get(0).getEducationQualifications().isEmpty()) {
            binding.pager.setCurrentItem(1, true);
            Toast.makeText(this, getString(R.string.enterqualifications), Toast.LENGTH_SHORT).show();
        }

    }

    private void CheckLocation() {
        if (arrayList.get(0).getLocationCountry() == null || arrayList.get(0).getLocationCountry().isEmpty()) {
            binding.pager.setCurrentItem(2, true);
            Toast.makeText(this, getString(R.string.entercountry), Toast.LENGTH_SHORT).show();
        } else if (arrayList.get(0).getLocationProvince() == null || arrayList.get(0).getLocationProvince().isEmpty()) {
            binding.pager.setCurrentItem(2, true);
            Toast.makeText(this, getString(R.string.enterprovince), Toast.LENGTH_SHORT).show();
        } else if (arrayList.get(0).getLocationCity() == null || arrayList.get(0).getLocationCity().isEmpty()) {
            binding.pager.setCurrentItem(2, true);
            Toast.makeText(this, getString(R.string.entercity), Toast.LENGTH_SHORT).show();
        } else if (arrayList.get(0).getLocationTown() == null || arrayList.get(0).getLocationTown().isEmpty()) {
            binding.pager.setCurrentItem(2, true);
            Toast.makeText(this, getString(R.string.entertown), Toast.LENGTH_SHORT).show();
        } else if (arrayList.get(0).getLocationRelocate() == null || arrayList.get(0).getLocationRelocate().isEmpty()) {
            binding.pager.setCurrentItem(2, true);
            Toast.makeText(this, getString(R.string.arewillrelo), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isCheckPreferred() {
        boolean checkingSolo = false;
        if (arrayList.get(0).getPreferenceAgeFrom() < 1) {
            binding.pager.setCurrentItem(2, true);
            Toast.makeText(this, getString(R.string.enterminpreage), Toast.LENGTH_SHORT).show();

        } else if (arrayList.get(0).getPreferenceAgeTo() < 1) {
            binding.pager.setCurrentItem(2, true);
            Toast.makeText(this, getString(R.string.entermaxpreage), Toast.LENGTH_SHORT).show();
        } else if (arrayList.get(0).getPreferenceGender() == null || arrayList.get(0).getPreferenceGender().isEmpty()) {
            binding.pager.setCurrentItem(2, true);
            Toast.makeText(this, getString(R.string.enterpregender), Toast.LENGTH_SHORT).show();
        } else if (arrayList.get(0).getPreferenceRace() == null || arrayList.get(0).getPreferenceRace().isEmpty()) {
            binding.pager.setCurrentItem(2, true);
            Toast.makeText(this, getString(R.string.enterprerace), Toast.LENGTH_SHORT).show();
        } else if (arrayList.get(0).getPreferenceReligion() == null || arrayList.get(0).getPreferenceReligion().isEmpty()) {
            binding.pager.setCurrentItem(2, true);
            Toast.makeText(this, getString(R.string.enter_your_per_religion), Toast.LENGTH_SHORT).show();
        } else if (arrayList.get(0).getPreferenceRelocate() == null || arrayList.get(0).getPreferenceRelocate().isEmpty()) {
            binding.pager.setCurrentItem(2, true);
            Toast.makeText(this, getString(R.string.wantpatnerrelocate), Toast.LENGTH_SHORT).show();
        } else if (arrayList.get(0).getPreferenceBasics() == null || arrayList.get(0).getPreferenceBasics().isEmpty()) {
            binding.pager.setCurrentItem(2, true);
            Toast.makeText(this, getString(R.string.entermaxpreage), Toast.LENGTH_SHORT).show();
        } else {
            checkingSolo = true;
        }

        return checkingSolo;
    }

    private void addToList() {
        if (imageUri != null && !imageUri.isEmpty()) {
            model.setPersonalImageUri(imageUri);
            arrayList.set(0, model);
        }
        if (names != null && !names.isEmpty()) {
            model.setPersonalNames(names);
            arrayList.set(0, model);
        }
        if (age != null && !age.isEmpty()) {
            if (age.contains(",")) {
                age.replace(",", "");
            } else if (age.contains(".")) {
                age.replace(".", "");
            } else {
                model.setPersonalAge(Integer.parseInt(age));
                arrayList.set(0, model);
            }

        }
        if (gender != null && !gender.isEmpty()) {
            model.setPersonalGender(gender);
            arrayList.set(0, model);
        }
        if (race != null && !race.isEmpty()) {
            model.setPersonalRace(race);
            arrayList.set(0, model);
        }
        if (religion != null && !religion.isEmpty()) {
            model.setPersonalReligion(religion);
            arrayList.set(0, model);
        }
        if (languages != null && !languages.isEmpty()) {
            model.setPersonalLanguages(languages);
            arrayList.set(0, model);
        }
        if (occupation != null && !occupation.isEmpty()) {
            model.setPersonalOccupations(occupation);
            arrayList.set(0, model);
        }
        if (hobbies != null && !hobbies.isEmpty()) {
            model.setPersonalHobbies(hobbies);
            arrayList.set(0, model);
        }
        if (about_user != null && !about_user.isEmpty()) {
            model.setPersonalAbout(about_user);
            arrayList.set(0, model);
        }
        if (kids != null && !kids.isEmpty()) {
            model.setPersonalKids(kids);
            arrayList.set(0, model);
        }

        ///////////////////////Education////////////

        if (studentAt != null && !studentAt.isEmpty()) {
            model.setEducationAt(studentAt);
            arrayList.set(0, model);
        }
        if (qualifications != null && !qualifications.isEmpty()) {
            model.setEducationQualifications(qualifications);
            arrayList.set(0, model);
        }

        //////////////////////locationInformation/////////

        if (country != null && !country.isEmpty()) {
            model.setLocationCountry(country);
            arrayList.set(0, model);
        }
        if (state != null && !state.isEmpty()) {
            model.setLocationProvince(state);
            arrayList.set(0, model);
        }
        if (city != null && !city.isEmpty()) {
            model.setLocationCity(city);
            arrayList.set(0, model);
        }
        if (town != null && !town.isEmpty()) {
            model.setLocationTown(town);
            arrayList.set(0, model);
        }
        if (User_Relocate != null && !User_Relocate.isEmpty()) {
            model.setLocationRelocate(User_Relocate);
            arrayList.set(0, model);
        }

        ////////PartnerPreference///////
        if (preAgeFrom != null && !preAgeFrom.isEmpty()) {
            model.setPreferenceAgeFrom(Integer.parseInt(preAgeFrom.trim()));
            arrayList.set(0, model);
        }
        if (preAgeTo != null && !preAgeTo.isEmpty()) {
            model.setPreferenceAgeTo(Integer.parseInt(preAgeTo));
            arrayList.set(0, model);
        }
        if (preGender != null && !preGender.isEmpty()) {
            model.setPreferenceGender(preGender);
            arrayList.set(0, model);
        }
        if (preRace != null && !preRace.isEmpty()) {
            model.setPreferenceRace(preRace);
            arrayList.set(0, model);
        }
        if (preReligion != null && !preReligion.isEmpty()) {
            model.setPreferenceReligion(preReligion);
            arrayList.set(0, model);
        }
        if (preRelocate != null && !preRelocate.isEmpty()) {
            model.setPreferenceRelocate(preRelocate);
            arrayList.set(0, model);
        }
        if (preBasics != null && !preBasics.isEmpty()) {
            model.setPreferenceBasics(preBasics);
            arrayList.set(0, model);
        }


    }

    private void handleSubmission() {
        if (isCheckPreferred()){
            submitInformation();
        }else{
            Toast.makeText(this, "Missing Information", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitInformation(){
        Uri imageUri = Uri.parse(arrayList.get(0).getPersonalImageUri());
        binding.Button.setEnabled(false);
        ProgressDialog progressDialog = new ProgressDialog(this,R.style.CustomProgressDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();
        StorageReference reference = FirebaseStorage.getInstance().getReference("ImagesProfile").child(Objects.requireNonNull(imageUri.getLastPathSegment()));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            DatabaseReference EntriesReference = FirebaseDatabase.getInstance().getReference("Entries");
            EntriesReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue("5");
            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                binding.Button.setEnabled(true);
                String link = uri.toString();
                HashMap<String, Object> map = new HashMap<>();
                map.put("profile", link);
                map.put("username", StringUtils.capitalize(arrayList.get(0).getPersonalNames().trim()) );
                map.put("age", String.valueOf(arrayList.get(0).getPersonalAge()).trim());
                map.put("gender", StringUtils.capitalize(arrayList.get(0).getPersonalGender()));
                if (arrayList.get(0).getPersonalGender().trim().equals(getString(R.string.male))){
                    map.put("genderCode","6255533");
                } else if (arrayList.get(0).getPersonalGender().trim().equals(getString(R.string.female))) {
                    map.put("genderCode","333336255533");
                }else{
                    map.put("genderCode","66684433777");
                }
                map.put("race",StringUtils.capitalize(arrayList.get(0).getPersonalRace().trim()));
                map.put("religion",StringUtils.capitalize(arrayList.get(0).getPersonalReligion().trim()));
                map.put("languages", StringUtils.capitalize(arrayList.get(0).getPersonalLanguages().trim()));
                map.put("occupation", StringUtils.capitalize(arrayList.get(0).getPersonalOccupations().trim()));
                map.put("hobbies", StringUtils.capitalize(arrayList.get(0).getPersonalHobbies().trim()));
                map.put("about_user", StringUtils.capitalize(arrayList.get(0).getPersonalAbout().trim()));
                map.put("kids", StringUtils.capitalize(arrayList.get(0).getPersonalKids().trim()));

                map.put("studentAt",StringUtils.capitalize(arrayList.get(0).getEducationAt().trim()));
                map.put("qualifications", StringUtils.capitalize(arrayList.get(0).getEducationQualifications().trim()));

                map.put("country", StringUtils.capitalize(arrayList.get(0).getLocationCountry().trim()));
                map.put("province", StringUtils.capitalize(arrayList.get(0).getLocationProvince().trim()));
                map.put("city", StringUtils.capitalize(arrayList.get(0).getLocationCity().trim()));
                map.put("town", StringUtils.capitalize(arrayList.get(0).getLocationTown().trim()));
                map.put("usertorelocate", StringUtils.capitalize(arrayList.get(0).getLocationRelocate().trim()));

                map.put("preferredAgeMin", String.valueOf(arrayList.get(0).getPreferenceAgeFrom()).trim());
                map.put("preferredAgeMax", String.valueOf(arrayList.get(0).getPreferenceAgeTo()).trim());
                map.put("preferredGender", StringUtils.capitalize(arrayList.get(0).getPreferenceGender().trim()));
                if (arrayList.get(0).getPreferenceGender().trim().equals(getString(R.string.male))){
                    map.put("preGenderCode","6255533");
                } else if (arrayList.get(0).getPreferenceGender().trim().equals(getString(R.string.female))) {
                    map.put("preGenderCode","333336255533");
                }else{
                    map.put("preGenderCode","66684433777");
                }
                map.put("preferredRace", StringUtils.capitalize(arrayList.get(0).getPreferenceRace().trim()));
                map.put("preferredReligion",StringUtils.capitalize(arrayList.get(0).getPreferenceReligion().trim()));
                map.put("preferToRelocate", StringUtils.capitalize(arrayList.get(0).getPreferenceRelocate().trim()));
                map.put("basically",StringUtils.capitalize(arrayList.get(0).getPreferenceBasics().trim()));
                map.put("UserVerified", "false");
                map.put("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                database.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(map)
                        .addOnSuccessListener(unused -> {
                            progressDialog.dismiss();
                            startActivity(new Intent(GetInformation.this, MainActivity.class));
                            finish();

                        }).addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(GetInformation.this,R.style.CustomProgressDialogStyle);
                            alertDialog.setCancelable(false);
                            alertDialog.setMessage(e.getLocalizedMessage());
                            alertDialog.setPositiveButton(getString(R.string.Ok), (dialog, which) -> dialog.dismiss());
                            alertDialog.create();
                            alertDialog.show();

                        }).addOnCanceledListener(() -> {

                        });


            }).addOnFailureListener(e -> {
                binding.Button.setEnabled(true);
                progressDialog.dismiss();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(GetInformation.this,R.style.CustomProgressDialogStyle);
                alertDialog.setCancelable(false);
                alertDialog.setMessage(e.getLocalizedMessage());
                alertDialog.setPositiveButton(getString(R.string.Ok), (dialog, which) -> dialog.dismiss());
                alertDialog.create();
                alertDialog.show();

            });

        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(GetInformation.this,R.style.CustomProgressDialogStyle);
            alertDialog.setCancelable(false);
            alertDialog.setMessage(e.getLocalizedMessage());
            alertDialog.setPositiveButton(getString(R.string.Ok), (dialog, which) -> dialog.dismiss());
            alertDialog.create();
            alertDialog.show();
        });
    }
    BottomSheetDialog localizeSheet;
    private void localisationSet(){
        localizeSheet = new BottomSheetDialog(GetInformation.this);
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
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Configuration config =new Configuration();
        config.setLocale(locale);
        config.locale = locale;
        config.setLocale(locale);
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());
        recreate();

    }
    private void setLocaleShared(String code){
        SharedPreferences.Editor editor = getSharedPreferences("Localization",MODE_PRIVATE).edit();
        editor.putString("currentLanguage",code);
        editor.apply();
        editor.commit();

    }
}