package com.mecaroid.interdate.InformationPages;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.mecaroid.interdate.Models.Shared.FragmentsSharedViewModel;
import com.mecaroid.interdate.Models.Shared.SharedViewModel;
import com.mecaroid.interdate.R;
import com.mecaroid.interdate.databinding.FragmentPersonalInformationBinding;
import com.mecaroid.interdate.databinding.UserchoicesBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class PersonalInformation extends Fragment {
    Uri imageUri;
    SharedViewModel sharedViewModel;
    Map<String,String> map = new HashMap<>();
    FragmentsSharedViewModel fragmentsSharedViewModel;

    public PersonalInformation() {
        // Required empty public constructor
    }
    FragmentPersonalInformationBinding binding;
    String names,age,gender,race,religion,languages,occupation,hobbies,about_user,imageUriString;
    String kids = "Non";


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentsSharedViewModel = new ViewModelProvider(requireActivity()).get(FragmentsSharedViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getData().observe(this, dataMap->{
            for (Map.Entry<String,String> entry : dataMap.entrySet()){
                imageUriString = dataMap.get("imageUri");
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
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPersonalInformationBinding.inflate(getLayoutInflater());
        binding.kidsHolder.setVisibility(View.GONE);
        menuActions();
        getImage();
        SetInformation();
        binding.NothaveKid.setChecked(true);
        map.put("kids","Non");
        sharedViewModel.setData(map);
        CheckNullPointerFromActivity();
        
        return  binding.getRoot();
    }

    private void menuActions(){
        binding.userGender.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.female) {
                    binding.gender.setText(getString(R.string.female));
                } else if (item.getItemId() == R.id.male) {
                    binding.gender.setText(getString(R.string.male));
                } else if (item.getItemId() == R.id.others) {
                    UserchoicesBinding userchoicesBinding = UserchoicesBinding.inflate(getLayoutInflater());
                    BottomSheetDialog specieficGender = new BottomSheetDialog(requireContext());
                    specieficGender.setCancelable(true);
                    specieficGender.setContentView(userchoicesBinding.getRoot());
                    specieficGender.show();
                    userchoicesBinding.submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (userchoicesBinding.choice.getText().toString().isEmpty()) {
                                Toast.makeText(getContext(), getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                            } else {
                                binding.gender.setText(userchoicesBinding.choice.getText().toString());
                                specieficGender.dismiss();
                            }

                        }
                    });


                }
                return true;
            }
        });

        binding.userRace.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.black) {
                    binding.race.setText(getString(R.string.black));
                } else if (item.getItemId() == R.id.coloured) {
                    binding.race.setText(getString(R.string.coloured));

                } else if (item.getItemId() == R.id.indian) {
                    binding.race.setText(getString(R.string.indian));
                } else if (item.getItemId() == R.id.white) {
                    binding.race.setText(getString(R.string.white));
                } else if (item.getItemId() == R.id.others) {
                    UserchoicesBinding userchoicesBinding = UserchoicesBinding.inflate(getLayoutInflater());
                    BottomSheetDialog specieficRace = new BottomSheetDialog(getContext());
                    specieficRace.setCancelable(true);
                    specieficRace.setContentView(userchoicesBinding.getRoot());
                    specieficRace.show();
                    userchoicesBinding.ChoiceHint.setHint(getString(R.string.race));
                    userchoicesBinding.specify.setText(getString(R.string.specify_race));
                    userchoicesBinding.submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Objects.requireNonNull(userchoicesBinding.choice.getText()).toString().isEmpty()) {
                                Toast.makeText(getContext(), getString(R.string.enterrace), Toast.LENGTH_SHORT).show();
                            } else {
                                binding.race.setText(userchoicesBinding.choice.getText().toString());
                                specieficRace.dismiss();
                            }

                        }
                    });

                }
                return true;
            }
        });

        binding.haveKid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.NothaveKid.setChecked(false);
                    binding.kidsHolder.setVisibility(View.VISIBLE);
                    if (binding.kidsNumber.getText().toString().trim().isEmpty()){
                        map.put("kids","");
                    }else {
                        map.put("kids",binding.kidsNumber.getText().toString().trim());
                    }
                    sharedViewModel.setData(map);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        binding.scrollView.scrollToDescendant(binding.kidsHolder);
                    }else {
                        binding.scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                }else {
                    binding.NothaveKid.setChecked(true);
                    binding.kidsHolder.setVisibility(View.GONE);
                    map.put("kids","Non");
                    sharedViewModel.setData(map);
                }
            }
        });
        binding.NothaveKid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.haveKid.setChecked(false);
                    binding.kidsHolder.setVisibility(View.GONE);
                    map.put("kids","Non");
                    sharedViewModel.setData(map);
                }else{
                    binding.haveKid.setChecked(true);
                    binding.kidsHolder.setVisibility(View.VISIBLE);
                    if (binding.kidsNumber.getText().toString().trim().isEmpty()){
                        map.put("kids","");
                    }else {
                        map.put("kids",binding.kidsNumber.getText().toString().trim());
                    }
                    sharedViewModel.setData(map);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        binding.scrollView.scrollToDescendant(binding.kidsHolder);
                    }else {
                        binding.scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                }
            }
        });


    }
    private void getImage(){
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                o -> {
                    if (o.getResultCode() == RESULT_OK && o.getData() != null) {
                        Intent data = o.getData();
                        imageUri = data.getData();
                        binding.profilePic.setImageURI(imageUri);
                        getImageUrl(imageUri);
                    }
                }
        );
        binding.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    activityResultLauncher.launch(intent);
                } else {
                    startActivityForResult(intent, 1);
                }


            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            binding.profilePic.setImageURI(imageUri);
            getImageUrl(imageUri);


        }
    }


    private void SetInformation(){

        TextEditorChanges(binding.names,"username");
        TextEditorChanges(binding.age,"age");
        TextEditorChanges(binding.gender,"gender");
        TextEditorChanges(binding.race,"race");
        TextEditorChanges(binding.religion,"religion");
        TextEditorChanges(binding.languages,"languages");
        TextEditorChanges(binding.occupations,"occupations");
        TextEditorChanges(binding.hobbies,"hobbies");
        TextEditorChanges(binding.about,"about_user");

        binding.kidsNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                map.put("kids",charSequence.toString().trim());
                sharedViewModel.setData(map);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
    private void TextEditorChanges( TextInputEditText editText, String value){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Objects.equals(value,"kids") && binding.haveKid.isChecked()){
                    map.put(value,charSequence.toString());
                    sharedViewModel.setData(map);

                } else if (Objects.equals(value,"kids") && binding.NothaveKid.isChecked()) {
                    map.put(value,charSequence.toString());
                    sharedViewModel.setData(map);

                } else if (!charSequence.toString().isEmpty()){
                    map.put(value,charSequence.toString());
                    sharedViewModel.setData(map);
                }else if (charSequence.toString().equals(getString(R.string.female))){
                    map.put(value,getString(R.string.female));
                    sharedViewModel.setData(map);
                }else if (charSequence.toString().equals(getString(R.string.male))){
                    map.put(value,getString(R.string.male));
                    sharedViewModel.setData(map);
                }if (charSequence.toString().equals(getString(R.string.black))){
                    map.put(value,getString(R.string.black));
                    sharedViewModel.setData(map);
                }if (charSequence.toString().equals(getString(R.string.coloured))){
                    map.put(value,getString(R.string.coloured));
                    sharedViewModel.setData(map);
                }if (charSequence.toString().equals(getString(R.string.indian))){
                    map.put(value,getString(R.string.indian));
                    sharedViewModel.setData(map);
                }if (charSequence.toString().equals(getString(R.string.white))){
                    map.put(value,getString(R.string.white));
                    sharedViewModel.setData(map);
                }else{
                    map.put(value,charSequence.toString().trim());
                    sharedViewModel.setData(map);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    private void getImageUrl(Uri imageUri){
        map.put("imageUri",imageUri.toString());
        sharedViewModel.setData(map);

    }
    private void CheckNullPointerFromActivity(){
        if (imageUriString !=null){
            if (!imageUriString.isEmpty()){
                binding.profilePic.setImageURI(Uri.parse(imageUriString));

            }

        }if (names !=null){
            if (!names.isEmpty()){
                binding.names.setText(names.trim());
            }
        }if (age !=null){
            if (!age.isEmpty()){
                binding.age.setText(age.trim());
            }
        }if (gender !=null){
            if (!gender.isEmpty()){
                binding.gender.setText(gender.trim());
            }
        }if (race !=null){
            if (!race.isEmpty()){
                binding.race.setText(race.trim());
            }
        }if (religion !=null){
            if (!religion.isEmpty()){
                binding.religion.setText(religion.trim());
            }
        }if (languages !=null){
            if (!languages.isEmpty()){
                binding.languages.setText(languages.trim());
            }
        }if (occupation !=null){
            if (!occupation.isEmpty()){
                binding.occupations.setText(occupation.trim());
            }
        }if (hobbies !=null){
            if (!hobbies.isEmpty()){
                binding.hobbies.setText(hobbies.trim());
            }
        }if (about_user !=null){
            if (!about_user.isEmpty()){
                binding.about.setText(about_user.trim());
            }
        }if (kids !=null){
            if (!kids.isEmpty() && !Objects.equals(kids,"Non")&& binding.kidsHolder.getVisibility() == View.VISIBLE){
                binding.kidsNumber.setText(kids.trim());
            }
        }
    }
}