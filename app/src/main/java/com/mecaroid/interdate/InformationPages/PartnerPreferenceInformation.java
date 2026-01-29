package com.mecaroid.interdate.InformationPages;

import android.os.Bundle;

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
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.mecaroid.interdate.Models.Shared.SharedViewModel;
import com.mecaroid.interdate.R;
import com.mecaroid.interdate.databinding.FragmentPartnerPreferenceInformationBinding;
import com.mecaroid.interdate.databinding.UserchoicesBinding;

import java.util.HashMap;
import java.util.Map;


public class PartnerPreferenceInformation extends Fragment {

    SharedViewModel sharedViewModel;
    FragmentPartnerPreferenceInformationBinding binding;
    Map<String,String> map = new HashMap<>();


    public PartnerPreferenceInformation() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPartnerPreferenceInformationBinding.inflate(getLayoutInflater());
        setData();
        setActions();

        // Inflate the layout for this fragment
        return binding.getRoot();
    }


    private void setData(){
        getData(binding.ageFrom,"preAgeFrom");
        getData(binding.ageTo,"preAgeTo");
        getData(binding.preferedGender,"preGender");
        getData(binding.prefferedRace,"preRace");
        getData(binding.preffredReligion,"preReligion");
        getData(binding.preffredReLocate,"preRelocate");
        getData(binding.bacicallyLookingFor,"preBasics");


    }

    private void getData( TextInputEditText editText,@Nullable String title){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals(getString(R.string.female))){
                    map.put(title,getString(R.string.non_female));
                }else if (charSequence.toString().equals(getString(R.string.male))){
                    map.put(title,getString(R.string.non_male));
                }if (charSequence.toString().equals(getString(R.string.black))){
                    map.put(title,getString(R.string.non_black));
                }if (charSequence.toString().equals(getString(R.string.coloured))){
                    map.put(title,getString(R.string.non_coloured));
                }if (charSequence.toString().equals(getString(R.string.indian))){
                    map.put(title,getString(R.string.non_indian));
                }if (charSequence.toString().equals(getString(R.string.white))){
                    map.put(title,getString(R.string.non_white));
                }else{
                    map.put(title,charSequence.toString().trim());
                }

                sharedViewModel.setData(map);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


    private void setActions(){
        binding.preferedGen.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.female) {
                    binding.preferedGender.setText(getString(R.string.female));
                } else if (item.getItemId() == R.id.male) {
                    binding.preferedGender.setText(getString(R.string.male));
                } else if(item.getItemId() == R.id.others) {
                    UserchoicesBinding userchoicesBinding = UserchoicesBinding.inflate(getLayoutInflater());
                    BottomSheetDialog specieficRace = new BottomSheetDialog(requireContext());
                    specieficRace.setCancelable(true);
                    specieficRace.setContentView(userchoicesBinding.getRoot());
                    specieficRace.show();
                    userchoicesBinding.ChoiceHint.setHint(getString(R.string.gender));
                    userchoicesBinding.specify.setText(getString(R.string.specify_gender));
                    userchoicesBinding.submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (userchoicesBinding.choice.getText().toString().isEmpty()) {
                                Toast.makeText(requireContext(), getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                            } else {
                                binding.preferedGender.setText(userchoicesBinding.choice.getText().toString());
                                specieficRace.dismiss();
                            }

                        }
                    });

                }
                return true;
            }
        });
        binding.preffereace.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.black) {
                    binding.prefferedRace.setText(getString(R.string.black));
                } else if (item.getItemId() == R.id.coloured) {
                    binding.prefferedRace.setText(getString(R.string.coloured));

                } else if (item.getItemId() == R.id.indian) {
                    binding.prefferedRace.setText(getString(R.string.indian));
                } else if (item.getItemId() == R.id.white) {
                    binding.prefferedRace.setText(getString(R.string.white));
                } else if (item.getItemId() == R.id.any) {
                    binding.prefferedRace.setText(getString(R.string.any));
                } else if (item.getItemId() == R.id.others) {
                    UserchoicesBinding userchoicesBinding = UserchoicesBinding.inflate(getLayoutInflater());
                    BottomSheetDialog specieficRace = new BottomSheetDialog(requireContext());
                    specieficRace.setCancelable(true);
                    specieficRace.setContentView(userchoicesBinding.getRoot());
                    specieficRace.show();
                    userchoicesBinding.ChoiceHint.setHint(getString(R.string.race));
                    userchoicesBinding.specify.setText(getString(R.string.specify_race));
                    userchoicesBinding.submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (userchoicesBinding.choice.getText().toString().isEmpty()) {
                                Toast.makeText(requireContext(), getString(R.string.enterrace), Toast.LENGTH_SHORT).show();
                            } else {
                                binding.prefferedRace.setText(userchoicesBinding.choice.getText().toString());
                                specieficRace.dismiss();
                            }

                        }
                    });

                }
                return true;
            }
        });


    }
}