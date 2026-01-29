package com.mecaroid.interdate.InformationPages;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.android.material.textfield.TextInputEditText;
import com.mecaroid.interdate.Models.Shared.SharedViewModel;
import com.mecaroid.interdate.R;
import com.mecaroid.interdate.databinding.FragmentEducationInformationBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class EducationInformation extends Fragment {



    public EducationInformation() {
        // Required empty public constructor
    }
    FragmentEducationInformationBinding binding;
    SharedViewModel sharedViewModel;
    Map<String,String> map = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        map.put("student?","No");
        sharedViewModel.setData(map);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEducationInformationBinding.inflate(getLayoutInflater());
        binding.studentHolder.setVisibility(View.GONE);
        binding.NotStudentCheck.setChecked(false);
        map.put("student?","No");
        sharedViewModel.setData(map);
        binding.studentCheck.setChecked(true);
        binding.studentHolder.setVisibility(View.VISIBLE);
        binding.NotStudentCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.studentHolder.setVisibility(View.GONE);
                    binding.studentCheck.setChecked(false);
                    map.put("student?","No");
                    sharedViewModel.setData(map);
                }
            }
        });
        binding.studentCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.NotStudentCheck.setChecked(false);
                    binding.studentHolder.setVisibility(View.VISIBLE);
                    map.put("student?","");
                    sharedViewModel.setData(map);


                }
            }
        });

        // Inflate the layout for this fragment
        TextEditorChanges(binding.where,"student?");
        TextEditorChanges(binding.qualifications,"qualifications");
        return binding.getRoot();


    }
    private void TextEditorChanges(TextInputEditText editText, String value){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Objects.equals(value,"student?")){
                    map.put(value,charSequence.toString());
                    sharedViewModel.setData(map);

                } else if (Objects.equals(value,"qualifications")) {
                    map.put(value,charSequence.toString());
                    sharedViewModel.setData(map);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

}